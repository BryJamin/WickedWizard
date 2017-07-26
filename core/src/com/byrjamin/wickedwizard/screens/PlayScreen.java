package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.AllArenaStore;
import com.byrjamin.wickedwizard.factories.arenas.levels.TutorialFactory;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkGraySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.LightGraySkin;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemVitaminC;
import com.byrjamin.wickedwizard.screens.world.*;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;


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

    private BitmapFont currencyFont;

    private DeathScreenWorld deathScreenWorld;
    private com.byrjamin.wickedwizard.screens.world.PauseWorld pauseWorld;

    private boolean isPaused = false;

    private GestureDetector gestureDetector;
    private boolean gameOver;

    private Random random;
    private JigsawGenerator jg;

    private AdventureWorld adventureWorld;


    //TODO IF you ever click in the deck area don't cast any spells


    public PlayScreen(MainGame game, PlayScreenConfig playScreenConfig) {
        super(game);
        setUpGlobals();


        ArenaSkin arenaSkin = new LightGraySkin(atlas);


        switch (playScreenConfig.spawn){
            case TUTORIAL:
            default:
                Array<Arena> placedArenas = new Array<Arena>();

                TutorialFactory tutorialFactory = new TutorialFactory(game.manager, new LightGraySkin(atlas));

                Arena startingArena = tutorialFactory.groundMovementTutorial(new MapCoords(0,0));
                placedArenas.add(startingArena);
                placedArenas.add(tutorialFactory.jumpTutorial(new MapCoords(1, 0)));
                placedArenas.add(tutorialFactory.platformTutorial(new MapCoords(4,0)));
                placedArenas.add(tutorialFactory.grappleTutorial(new MapCoords(5,0)));
                placedArenas.add(tutorialFactory.enemyTurtorial(new MapCoords(5,3)));
                placedArenas.add(tutorialFactory.endTutorial(new MapCoords(6,3)));
                placedArenas.add(new ArenaShellFactory(game.manager, new LightGraySkin(atlas)).createOmniArenaHiddenGrapple(new MapCoords(7,3)));

                ArenaMap arenaMap = new ArenaMap(startingArena, placedArenas);


                jg = new JigsawGeneratorConfig(game.manager, random)
                        .noBattleRooms(5)
                        .startingMap(arenaMap)
                        .build();
                jg.generate(jg.getStartingMap());
                jg.cleanArenas();
                break;
            case BOSS:


                ArenaMap bossMap;

                try{

                    bossMap = new BossMaps(game.manager, playScreenConfig.id != 8 ? new LightGraySkin(atlas) : new DarkGraySkin(atlas)).getBossMapsArray().get(playScreenConfig.id);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    bossMap = new BossMaps(game.manager, arenaSkin).blobbaMapCreate().createBossMap(new BossTeleporterComponent(),new ItemVitaminC());

                }

                jg = new JigsawGeneratorConfig(game.manager, random)
                        .startingMap(bossMap)
                        .build();

                jg.cleanArenas();


                break;


            case ARENA:

                //TODO if id is negative one get all areans in that level

                AllArenaStore allArenaStore = new AllArenaStore(game.manager, arenaSkin, random);


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

                jg = new JigsawGeneratorConfig(game.manager, random)
                        .noBattleRooms(20)
                        .arenaCreates(arenaCreates)
                        .build();
                jg.generate();
                jg.cleanArenas();
                break;
        }




        createWorlds();
        Gdx.input.setCatchBackKey(true);
    }




    public PlayScreen(MainGame game) {
        super(game);
        setUpGlobals();

        jg = new ChangeLevelSystem(game.manager, random).getJigsawGenerator(ChangeLevelSystem.Level.ONE);
        jg.generate();
        jg.cleanArenas();

        createWorlds();
        Gdx.input.setCatchBackKey(true);
    }


    public void setUpGlobals(){
        gestureDetector = new GestureDetector(new PlayScreenGestures());
        manager = game.manager;
        atlas = game.manager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        Assets.initialize(game.manager);
        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gameport = new FitViewport(MainGame.GAME_WIDTH + MainGame.GAME_BORDER * 2, MainGame.GAME_HEIGHT + MainGame.GAME_BORDER * 2, gamecam);
        //gameport.setScreenPosition(-(int)MainGame.GAME_BORDER, -(int)MainGame.GAME_BORDER);

        //TODO Decide whetehr to have heath on the screen or have health off in like black space.
        //Moves the gamecamer to the (0,0) position instead of being in the center.
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

                    if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE){
                        if(!isPaused) {
                            adventureWorld.pauseWorld();
                            isPaused = true;
                            pauseWorld = new com.byrjamin.wickedwizard.screens.world.PauseWorld(game, game.batch, game.manager, gameport, adventureWorld.getWorld().getSystem(RoomTransitionSystem.class),
                                    adventureWorld.getWorld().getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class));
                        } else {
                            adventureWorld.unPauseWorld();
                            isPaused = false;
                        }
                    }

                    return super.keyDown(keycode);
                }
            });
        }

        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);
    }


    public void createWorlds(){
        //ItemStore itemStore = new ItemStore(random);

        Arena startingArena = jg.getStartingRoom();
        adventureWorld = new AdventureWorld(game.manager, game.batch, gameport, random);
        adventureWorld.setJigsawGenerator(jg);
        adventureWorld.setPlayer(new PlayerFactory(game.manager).playerBag(startingArena.getWidth() / 2, Measure.units(45f)));
        adventureWorld.createAdventureWorld();
        adventureWorld.getWorld().getSystem(MusicSystem.class).playLevelMusic(adventureWorld.getWorld().getSystem(ChangeLevelSystem.class).getLevel());


        StatComponent stats = BagSearch.getObjectOfTypeClass(StatComponent.class, adventureWorld.getPlayer());

        if(Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GODMODE, false)) {
            stats.damage = 10f;
            stats.accuracy = 99f;
            stats.fireRate = 10f;
            stats.speed = 1.5f;
        }

    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);

        if(adventureWorld.isGameOver()) adventureWorld.pauseWorld();

        handleInput(delta);
        adventureWorld.process(delta);


        if(adventureWorld.isGameOver() && deathScreenWorld == null) deathScreenWorld = new DeathScreenWorld(game, gameport);


        if(!game.batch.isDrawing()) game.batch.begin();

        drawScreenBorder(game.batch, atlas);

        if(!adventureWorld.isGameOver()) adventureWorld.drawMapAndHud(isPaused);

        game.batch.end();

        if(isPaused) pauseWorld.process(delta);
        if(adventureWorld.isGameOver() && deathScreenWorld != null) deathScreenWorld.process(delta);


    }

    private void drawScreenBorder(SpriteBatch batch, TextureAtlas atlas){
        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        batch.setColor(new Color(Color.BLACK));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY + gamecam.viewportHeight - Measure.units(5f), gamecam.viewportWidth, Measure.units(5f));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY, gamecam.viewportWidth, Measure.units(5f));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY, Measure.units(5f), gamecam.viewportHeight);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX + gamecam.viewportWidth - Measure.units(5f), camY, Measure.units(5f), gamecam.viewportHeight);
        batch.setColor(new Color(Color.WHITE));
    }


    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
    }

    @Override
    public void pause() {

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

            if (adventureWorld.isGameOver() && deathScreenWorld != null) {
                Vector3 touchInput = gameport.unproject(new Vector3(x, y, 0));
                deathScreenWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                return true;
            }

            if(isPaused) {
                Vector3 touchInput = gameport.unproject(new Vector3(x, y, 0));
                pauseWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
            }

            return true;
        }



    }

}


