package com.bryjamin.wickedwizard.screens;

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
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.PlayerIDs;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.bryjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.bryjamin.wickedwizard.factories.arenas.GameCreator;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.bryjamin.wickedwizard.factories.arenas.PresetGames;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.factories.arenas.levels.AllArenaStore;
import com.bryjamin.wickedwizard.factories.arenas.levels.TutorialFactory;
import com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.arenas.skins.LightGraySkin;
import com.bryjamin.wickedwizard.screens.world.AdventureWorld;
import com.bryjamin.wickedwizard.screens.world.AreYouSureWorld;
import com.bryjamin.wickedwizard.screens.world.play.DeathScreenWorld;
import com.bryjamin.wickedwizard.screens.world.play.PauseWorld;
import com.bryjamin.wickedwizard.screens.world.play.TipsMessageWorld;
import com.bryjamin.wickedwizard.screens.world.play.UnlockMessageWorld;
import com.bryjamin.wickedwizard.utils.AbstractGestureDectector;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by Home on 15/10/2016.
 */
public class PlayScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gameport;

    public TextureAtlas atlas;
    public AssetManager manager;

    private DeathScreenWorld deathScreenWorld;
    private PauseWorld pauseWorld;
    private AreYouSureWorld areYouSureWorld;
    private UnlockMessageWorld unlockMessageWorld;
    private TipsMessageWorld tipsMessageWorld;

    private Vector3 touchInput = new Vector3();

    private GestureDetector gestureDetector;

    private Random random;
    private AdventureWorld adventureWorld;

    public enum ScreenState {
        PLAY, PAUSE, ARE_YOU_SURE, UNLOCK, DEATH, TIPS;
    }

    private ScreenState state = ScreenState.PLAY;




    //TODO IF you ever click in the deck area don't cast any spells


    public PlayScreen(MainGame game, PlayScreenConfig playScreenConfig) {
        super(game);
        setUpGlobals();


        ArenaSkin arenaSkin = new LightGraySkin();

        GameCreator gameCreator;


        switch (playScreenConfig.spawn){
            case TUTORIAL:
            default:
                TutorialFactory tutorialFactory = new TutorialFactory(game.assetManager, Level.ONE.getArenaSkin());
                gameCreator = new GameCreator(GameCreator.GameType.TUTORIAL, new GameCreator.LevelCreator(new JigsawGeneratorConfig(game.assetManager, random)
                        .startingMap(tutorialFactory.tutorialMap()), false));
                break;
            case BOSS:

                String unlockId = ChallengesResource.TUTORIAL_COMPLETE;

                switch (playScreenConfig.id){
                    case 0:
                    case 1:
                        arenaSkin = Level.ONE.getArenaSkin();
                        unlockId = ChallengesResource.LEVEL_1_COMPLETE;
                        break;
                    case 2:
                    case 3:
                        arenaSkin = Level.TWO.getArenaSkin();
                        unlockId = ChallengesResource.LEVEL_2_COMPLETE;
                        break;
                    case 4:
                    case 5:
                        arenaSkin = Level.THREE.getArenaSkin();
                        unlockId = ChallengesResource.LEVEL_3_COMPLETE;
                        break;
                    case 6:
                    case 7:
                        arenaSkin = Level.FOUR.getArenaSkin();
                        unlockId = ChallengesResource.LEVEL_4_COMPLETE;
                        break;
                    case 8:
                        arenaSkin = Level.FIVE.getArenaSkin();
                        unlockId = ChallengesResource.LEVEL_5_COMPLETE;
                        break;
                }


                ArenaMap bossMap;
                try{

                    bossMap = new BossMaps(game.assetManager, arenaSkin).getBossMapsArray().get(playScreenConfig.id);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    bossMap = new BossMaps(game.assetManager, arenaSkin).blobbaMapCreate().createBossMap(new BossTeleporterComponent());
                }

                gameCreator = new GameCreator(GameCreator.GameType.ADVENTURE, new GameCreator.LevelCreator(new JigsawGeneratorConfig(game.assetManager, random)
                        .startingMap(bossMap), unlockId, false));
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

                gameCreator = new GameCreator(GameCreator.GameType.ADVENTURE, new GameCreator.LevelCreator(new JigsawGeneratorConfig(game.assetManager, random)
                        .noBattleRooms(20)
                        .arenaCreates(arenaCreates)));
                break;
        }

        createWorlds(gameCreator, PlayerIDs.LEAH_ID);
        Gdx.input.setCatchBackKey(true);
    }




    public PlayScreen(MainGame game) {
        super(game);
        setUpGlobals();
        createWorlds(PresetGames.DEFAULT_GAME(manager, random), PlayerIDs.LEAH_ID);
        Gdx.input.setCatchBackKey(true);
    }

    public PlayScreen(MainGame game, String playerId) {
        super(game);
        setUpGlobals();
        createWorlds(PresetGames.DEFAULT_GAME(manager, random), playerId);
        Gdx.input.setCatchBackKey(true);
    }

    public PlayScreen(MainGame game, GameCreator gameCreator) {
        super(game);
        setUpGlobals();
        createWorlds(gameCreator, PlayerIDs.LEAH_ID);
        Gdx.input.setCatchBackKey(true);
    }


    public void setUpGlobals(){

        gestureDetector = new GestureDetector(new PlayScreenGestures());
        manager = game.assetManager;
        atlas = game.assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);

        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameport = new FitViewport(MainGame.GAME_WIDTH + MainGame.GAME_BORDER * 2, MainGame.GAME_HEIGHT + MainGame.GAME_BORDER * 2, gamecam);
        gameport.apply();

        gamecam.setToOrtho(false);

        gamecam.update();
        random = new Random();
        MathUtils.random = random;

    }




    public TextureAtlas getAtlas() {
        return atlas;
    }


    public void handleInput(float dt) {

        InputMultiplexer multiplexer = new InputMultiplexer();

        if(state == ScreenState.PLAY) {
            multiplexer.addProcessor(adventureWorld);
            multiplexer.addProcessor(adventureWorld.getWorld().getSystem(PlayerInputSystem.class).getPlayerInput());
        }

        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                //TODO on pc pushing escape probably shouldn't quit the game (But a pc version isn't happening)

                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {

                    switch (state) {
                        case PLAY: pause();
                            break;
                        case PAUSE: startAreYouSure();
                            break;
                        case ARE_YOU_SURE:
                            game.getScreen().dispose();
                            game.setScreen(new MenuScreen(game));
                            break;
                        case UNLOCK:
                        case TIPS:
                            unpause();
                            break;
                        case DEATH:
                            game.getScreen().dispose();
                            game.setScreen(new MenuScreen(game));
                            break;
                    }

                }

                return super.keyDown(keycode);
            }
        });

        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);
    }


    public void createWorlds(GameCreator gameCreator, String playerId){
        adventureWorld = new AdventureWorld(game, gameport,gameCreator,playerId, random);
        unlockMessageWorld = new UnlockMessageWorld(game, gameport);
        tipsMessageWorld = new TipsMessageWorld(game, gameport);
    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);

        handleInput(delta);

        adventureWorld.process(delta);

        switch (state){
            case PAUSE:
                pauseWorld.process(delta);
                break;
            case ARE_YOU_SURE:
                areYouSureWorld.process(delta);
                break;
            case DEATH:
                deathScreenWorld.process(delta);
                break;
            case UNLOCK:
                unlockMessageWorld.process(delta);
                break;
            case TIPS:
                tipsMessageWorld.process(delta);
                break;
        }

        gamecam.update();


        if(game.batch.isDrawing()){
            game.batch.end();
        }

    }


    public void startGameOver(){
        state = ScreenState.DEATH;
        deathScreenWorld = new DeathScreenWorld(game, adventureWorld, gameport);
    }



    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
    }

    @Override
    public void pause() {

        switch (state){
            case PLAY:
                adventureWorld.pauseWorld();
                state = ScreenState.PAUSE;
                pauseWorld = new PauseWorld(game, game.batch, game.assetManager, gameport, adventureWorld.getWorld().getSystem(RoomTransitionSystem.class),
                        adventureWorld.getWorld().getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class));
                break;
        }
    }


    public void unlock(String... strings){

        unlockMessageWorld = new UnlockMessageWorld(game, gameport);

        if(unlockMessageWorld.createUnlockMessage(strings)){
            adventureWorld.pauseWorld();
            state = ScreenState.UNLOCK;
        };
    }

    public void displayTip(String id){

        tipsMessageWorld = new TipsMessageWorld(game, gameport);

        if(tipsMessageWorld.createTip(id)){
            adventureWorld.pauseWorld();
            state = ScreenState.TIPS;
        };
    }



    public void unpause(){
        adventureWorld.unPauseWorld();
        state = ScreenState.PLAY;
    }

    public void startAreYouSure(){

        state = ScreenState.ARE_YOU_SURE;

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
                        state = ScreenState.PAUSE;
                    }
                });
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
        public boolean touchDown(float x, float y, int pointer, int button) {
            touchInput = gameport.unproject(touchInput.set(x, y, 0));

            switch (state){

                case ARE_YOU_SURE:
                    areYouSureWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;
                case DEATH:
                    deathScreenWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;
                case PAUSE:
                    pauseWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;
                case UNLOCK:
                    unlockMessageWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;
                case TIPS:
                    tipsMessageWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;

            }

            return true;
        }
/*
        @Override
        public boolean tap(float x, float y, int count, int button) {

            touchInput = gameport.unproject(touchInput.set(x, y, 0));

            switch (state){

                case ARE_YOU_SURE:
                    areYouSureWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;
                case DEATH:
                    deathScreenWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;
                case PAUSE:
                    pauseWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;
                case UNLOCK:
                    unlockMessageWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;
                case TIPS:
                    tipsMessageWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
                    break;

            }

            return true;
        }*/



    }

}


