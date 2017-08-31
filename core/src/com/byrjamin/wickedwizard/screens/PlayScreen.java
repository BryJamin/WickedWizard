package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.PresetGames;
import com.byrjamin.wickedwizard.factories.arenas.levels.AllArenaStore;
import com.byrjamin.wickedwizard.factories.arenas.levels.TutorialFactory;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkGraySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.LightGraySkin;
import com.byrjamin.wickedwizard.screens.world.*;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.MapCoords;


import java.util.Random;


//TODO


/**
 * Created by Home on 15/10/2016.
 */
public class PlayScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gameport;


    public TextureAtlas atlas;
    public AssetManager manager;

    private DeathScreenWorld deathScreenWorld;
    private com.byrjamin.wickedwizard.screens.world.PauseWorld pauseWorld;
    private AreYouSureWorld areYouSureWorld;



    private boolean isPaused = false;

    private GestureDetector gestureDetector;

    private Random random;
    private AdventureWorld adventureWorld;



    //TODO IF you ever click in the deck area don't cast any spells


    public PlayScreen(MainGame game, PlayScreenConfig playScreenConfig) {
        super(game);
        setUpGlobals();


        ArenaSkin arenaSkin = new LightGraySkin();

        GameCreator gameCreator;


        switch (playScreenConfig.spawn){
            case TUTORIAL:
            default:
                TutorialFactory tutorialFactory = new TutorialFactory(game.assetManager, new LightGraySkin());
                gameCreator = new GameCreator(new GameCreator.LevelCreator(new JigsawGeneratorConfig(game.assetManager, random)
                        .startingMap(tutorialFactory.tutorialMap()), false));
                break;
            case BOSS:


                ArenaMap bossMap;

                try{

                    bossMap = new BossMaps(game.assetManager, playScreenConfig.id != 8 ? new LightGraySkin() : new DarkGraySkin()).getBossMapsArray().get(playScreenConfig.id);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    bossMap = new BossMaps(game.assetManager, arenaSkin).blobbaMapCreate().createBossMap(new BossTeleporterComponent());

                }

                gameCreator = new GameCreator(new GameCreator.LevelCreator(new JigsawGeneratorConfig(game.assetManager, random)
                        .startingMap(bossMap), false));
                break;


            case ARENA:

                AllArenaStore allArenaStore = new AllArenaStore(game.assetManager, arenaSkin, random);


                Array<ArenaCreate> arenaCreates = new Array<ArenaCreate>();

                try {

                    if (playScreenConfig.roomid >= 0) {
                        arenaCreates.add(allArenaStore.getArenaGen(playScreenConfig.id, playScreenConfig.roomid));
                        boolean hasMandatory = allArenaStore.getArenaGen(playScreenConfig.id, playScreenConfig.roomid).createArena(new MapCoords()).mandatoryDoors.size > 0;
                        if (hasMandatory) arenaCreates.add(allArenaStore.blank);
                    } else {
                        arenaCreates.addAll(allArenaStore.allLevels.get(playScreenConfig.id).getAllArenas());
                    }

                } catch(IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    game.setScreen(new MenuScreen(game));

                }

                gameCreator = new GameCreator(new GameCreator.LevelCreator(new JigsawGeneratorConfig(game.assetManager, random)
                        .noBattleRooms(20)
                        .arenaCreates(arenaCreates)));
                break;
        }

        createWorlds(gameCreator);
        Gdx.input.setCatchBackKey(true);
    }




    public PlayScreen(MainGame game) {
        super(game);
        setUpGlobals();
        createWorlds(PresetGames.DEFAULT_GAME(manager, random));
        Gdx.input.setCatchBackKey(true);
    }

    public PlayScreen(MainGame game, GameCreator gameCreator) {
        super(game);
        setUpGlobals();
        createWorlds(gameCreator);
        Gdx.input.setCatchBackKey(true);
    }


    public void setUpGlobals(){
        gestureDetector = new GestureDetector(new PlayScreenGestures());
        manager = game.assetManager;
        atlas = game.assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        Assets.initialize(game.assetManager);
        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameport = new FitViewport(MainGame.GAME_WIDTH + MainGame.GAME_BORDER * 2, MainGame.GAME_HEIGHT + MainGame.GAME_BORDER * 2, gamecam);
        //gameport.setScreenPosition(-(int)MainGame.GAME_BORDER, -(int)MainGame.GAME_BORDER);

        //TODO Decide whetehr to have heath on the screen or have health off in like black space.
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);
        gamecam.update();
        random = new Random();
        MathUtils.random = random;

    }




    public TextureAtlas getAtlas() {
        return atlas;
    }


    public void handleInput(float dt) {

        InputMultiplexer multiplexer = new InputMultiplexer();

        if (!adventureWorld.isGameOver()) {
            if(adventureWorld.getWorld().getSystem(PlayerInputSystem.class).isEnabled()) {
                multiplexer.addProcessor(adventureWorld.getWorld().getSystem(PlayerInputSystem.class).getPlayerInput());
            }

            multiplexer.addProcessor(new InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {

                    if(areYouSureWorld == null) {
                        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
                            if (!isPaused) {
                                pause();
                            } else {

                                if (keycode == Input.Keys.BACK) {
                                    startAreYouSure();
                                } else {
                                    unpause();
                                }
                            }
                        }
                    } else {
                        if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE){
                            game.getScreen().dispose();
                            game.setScreen(new MenuScreen(game));
                        }
                    }

                    return super.keyDown(keycode);
                }
            });
        } else if(deathScreenWorld != null){
            multiplexer.addProcessor(new InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {

                    if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE){
                        game.getScreen().dispose();
                        game.setScreen(new MenuScreen(game));
                    }


                    return super.keyDown(keycode);
                }
            });
        }

        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);
    }


    public void createWorlds(GameCreator gameCreator){

        adventureWorld = new AdventureWorld(game, gameport,gameCreator, random);


        StatComponent stats = BagSearch.getObjectOfTypeClass(StatComponent.class, adventureWorld.getPlayer());

        if(Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY).getBoolean(PreferenceStrings.DEV_GODMODE, false)) {
            stats.damage = 99f;
            stats.accuracy = 99f;
            stats.fireRate = 99f;
            stats.speed = 0.5f;
            stats.luck = 99f;
        };

    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);

        //if(adventureWorld.isGameOver()) adventureWorld.pauseWorld();

        handleInput(delta);
        adventureWorld.process(delta);



        if(adventureWorld.isGameOver() && deathScreenWorld == null) deathScreenWorld = new DeathScreenWorld(game, gameport);
        if(isPaused) pauseWorld.process(delta);

        if(areYouSureWorld != null) {
            areYouSureWorld.process(delta);
        }

        if(adventureWorld.isGameOver() && deathScreenWorld != null) deathScreenWorld.process(delta);

        if(!adventureWorld.isGameOver()) {
            if(!game.batch.isDrawing()) game.batch.begin();
            game.batch.end();
        }

        gamecam.update();



    }


    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
    }

    @Override
    public void pause() {
        adventureWorld.pauseWorld();
        isPaused = true;
        //pause();
        pauseWorld = new com.byrjamin.wickedwizard.screens.world.PauseWorld(game, game.batch, game.assetManager, gameport, adventureWorld.getWorld().getSystem(RoomTransitionSystem.class),
                adventureWorld.getWorld().getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class));
        //QuickSave.saveGame(world);
    }

    public void unpause(){
        adventureWorld.unPauseWorld();
        isPaused = false;
    }

    public void startAreYouSure(){
        areYouSureWorld = new AreYouSureWorld(game, gameport, MenuStrings.ARE_YOU_SURE_EXIT_GAME,
                new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.getScreen().dispose();
                        game.setScreen(new MenuScreen(game));
                    }
                },

                new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        PlayScreen playScreen = (PlayScreen) game.getScreen();
                        playScreen.escapeAreYouSure();
                    }
                });
    }

    public void escapeAreYouSure(){
        areYouSureWorld = null;
    }


    @Override
    public void resume() {


    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        adventureWorld.getWorld().dispose();
    }

    public class PlayScreenGestures extends AbstractGestureDectector {

        @Override
        public boolean tap(float x, float y, int count, int button) {


            if (areYouSureWorld != null) {
                Vector3 touchInput = gameport.unproject(new Vector3(x, y, 0));
                areYouSureWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                return true;
            }

            if (adventureWorld.isGameOver() && deathScreenWorld != null) {
                Vector3 touchInput = gameport.unproject(new Vector3(x, y, 0));
                deathScreenWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                return true;
            }

            if(isPaused && areYouSureWorld == null) {
                Vector3 touchInput = gameport.unproject(new Vector3(x, y, 0));
                pauseWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                return true;
            }

            return true;
        }



    }

}


