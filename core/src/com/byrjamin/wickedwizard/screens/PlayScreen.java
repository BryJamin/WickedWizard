package com.byrjamin.wickedwizard.screens;

import com.artemis.BaseSystem;
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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.MusicStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.systems.SoundSystem;
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
import com.byrjamin.wickedwizard.factories.items.ItemStore;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.factories.items.pickups.KeyUp;
import com.byrjamin.wickedwizard.screens.world.AdventureWorld;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
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

    private World world;
    private World deathWorld;


    private PauseWorld pauseWorld;

    private boolean isPaused = false;

    private CurrencyComponent currencyComponent;
    private StatComponent stats;

    GestureDetector gestureDetector;
    private boolean gameOver = false;
    private boolean isTutorial;

    private ArenaGUI arenaGUI;
    private ArenaGUI pauseArenaGUI;
    private Random random;
    private JigsawGenerator jg;
    private Array<Arena> arenaArray;


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
                        .currentLevel(ChangeLevelSystem.Level.ONE)
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
                    bossMap = new BossMaps(game.manager, arenaSkin).blobbaMap(new BossTeleporterComponent());

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
                arenaCreates.add(allArenaStore.getArenaGen(playScreenConfig.id, playScreenConfig.roomid));

                jg = new JigsawGeneratorConfig(game.manager, random)
                        .noBattleRooms(10)
                        .arenaCreates(arenaCreates)
                        .build();
                jg.generate();
                jg.cleanArenas();
                break;
        }




        createWorld();
        Gdx.input.setCatchBackKey(true);
    }




    public PlayScreen(MainGame game) {
        super(game);
        setUpGlobals();

         /*       jg =new JigsawGenerator(game.manager,new LightGraySkin(atlas), 5 , itemStore, random);
        jg.generateTutorial = isTutorial;
        jg.generate();
*/
        jg = new JigsawGeneratorConfig(game.manager, random)
                .noBattleRooms(5)
                .currentLevel(ChangeLevelSystem.Level.ONE)
                .startingMap(new ArenaMap(new ArenaShellFactory(game.manager, new LightGraySkin(atlas)).createOmniArenaHiddenGrapple(new MapCoords())))
                .build();
        jg.generate();
        jg.cleanArenas();

        createWorld();
        Gdx.input.setCatchBackKey(true);
    }


    public void setUpGlobals(){
        gestureDetector = new GestureDetector(new gestures());
        manager = game.manager;
        atlas = game.manager.get("sprite.atlas", TextureAtlas.class);
        Assets.initialize(game.manager);
        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gameport = new FitViewport(MainGame.GAME_WIDTH + MainGame.GAME_BORDER * 2, MainGame.GAME_HEIGHT + MainGame.GAME_BORDER * 2, gamecam);
        //gameport.setScreenPosition(-(int)MainGame.GAME_BORDER, -(int)MainGame.GAME_BORDER);

        //TODO Decide whetehr to have heath on the screen or have health off in like black space.
        //Moves the gamecamer to the (0,0) position instead of being in the center.
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);
        random = new Random();
        currencyFont = game.manager.get(Assets.small, BitmapFont.class);// font size 12 pixels

    }




    public TextureAtlas getAtlas() {
        return atlas;
    }


    public void handleInput(float dt) {

        InputMultiplexer multiplexer = new InputMultiplexer();

        if (!gameOver) {
            if(world.getSystem(PlayerInputSystem.class).isEnabled()) {
                multiplexer.addProcessor(world.getSystem(PlayerInputSystem.class).getPlayerInput());
            }
            multiplexer.addProcessor(new InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {

                    if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE){
                        if(!isPaused) {
                            endGame(world);

                            pauseWorld = new PauseWorld(game.batch, game.manager, gameport);
                            pauseWorld.startWorld(world.getSystem(FindPlayerSystem.class).getPC(StatComponent.class));

                            RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);

                            pauseArenaGUI = new ArenaGUI(0, 0, Measure.units(2.5f), 8, rts.getRoomArray(), rts.getCurrentArena(), atlas);

                            isPaused = true;
                        } else {
                            unpauseWorld(world);
                            isPaused = false;
                        }
                    }

                    return super.keyDown(keycode);
                }
            });
        } else {
            multiplexer.addProcessor(gestureDetector);
        }



        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);
    }



    public void createDeathScreenWorld(){
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        //new FindPlayerSystem(player),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, manager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();

        deathWorld = new World(config);

        FadeComponent fc = new FadeComponent(2.0f, false);
        fc.fadeIn = true;

        Entity e = deathWorld.createEntity();
        e.edit().add(new PositionComponent(gamecam.position.x
                ,gamecam.position.y - gameport.getWorldHeight() / 2 + 800));
        TextureFontComponent tfc = new TextureFontComponent("Oh dear, you seem to have died \n\n Tap to restart");
        e.edit().add(tfc);
        e.edit().add(fc);


        e = deathWorld.createEntity();
        e.edit().add(new PositionComponent(gamecam.position.x - gameport.getWorldWidth() / 2,
                gamecam.position.y - gameport.getWorldHeight() / 2));
        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), gamecam.viewportWidth, gamecam.viewportHeight, TextureRegionComponent.FOREGROUND_LAYER_NEAR);
        trc.color = new Color(Color.BLACK);
        trc.color.a = 0.5f;
        trc.layer = -1;

        e.edit().add(trc);
        e.edit().add(fc);

    }


    public void createWorld(){
        ItemStore itemStore = new ItemStore(random);

        Arena startingArena = jg.getStartingRoom();
        AdventureWorld adventureWorld = new AdventureWorld(game.manager, game.batch, gameport, random);
        adventureWorld.setJigsawGenerator(jg);
        adventureWorld.setPlayer(new PlayerFactory(game.manager).playerBag(startingArena.getWidth() / 2, Measure.units(45f)));
        world = adventureWorld.createAdventureWorld();

        world.getSystem(SoundSystem.class).playMusic(MusicStrings.song8);

        stats = BagSearch.getObjectOfTypeClass(StatComponent.class, adventureWorld.getPlayer());

        if(Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GODMODE, false)) {
            stats.damage = 10f;
            stats.accuracy = 99f;
            stats.fireRate = 10f;
            stats.speed = 1.5f;
        }

        currencyComponent = BagSearch.getObjectOfTypeClass(CurrencyComponent.class, adventureWorld.getPlayer());
        arenaGUI = new ArenaGUI(0, 0, jg.getStartingMap().getRoomArray(), startingArena, atlas);
    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {

        //Sets the background color if nothing is on the screen.
        Gdx.gl.glClearColor(0, 0, 0, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gamecam.combined);

        if (delta < 0.02f) {
            world.setDelta(delta);
        } else {
            world.setDelta(0.017f);
        }

        //System.out.println(world.getAspectSubscriptionManager().get(Aspect.all()).getEntities().size());

        //System.out.println(world.delta);

        if(gameOver){
            endGame(world);
        }


        handleInput(world.delta);
        world.process();


        if(stats.health <= 0 && !gameOver){
            gameOver = true;
            createDeathScreenWorld();
        }



        if(!game.batch.isDrawing()) {
            game.batch.begin();
        }

        drawScreenBorder(game.batch, atlas);

        if(!gameOver) {
            if(!isPaused) {
                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                arenaGUI.update(world.delta,
                        gamecam.position.x + Measure.units(45),
                        gamecam.position.y + Measure.units(25),
                        rts.getVisitedArenas(),
                        rts.getUnvisitedButAdjacentArenas(),
                        rts.getCurrentArena(),
                        rts.getCurrentPlayerLocation());
            }



            //Map of the world
            drawHUD(world, gamecam);
            arenaGUI.draw(game.batch);

            //HUD

        }

        game.batch.end();


        if(isPaused){

            if (delta < 0.02f) {
                pauseWorld.getWorld().setDelta(delta);
            } else {
                pauseWorld.getWorld().setDelta(0.02f);
            }


            pauseWorld.process();

            RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);

            float camX = gamecam.position.x - gamecam.viewportWidth / 2;
            float camY = gamecam.position.y - gamecam.viewportHeight / 2;

            pauseArenaGUI.update(pauseWorld.getWorld().delta,
                    camX + Measure.units(75f),
                    camY + Measure.units(35f),
                    rts.getVisitedArenas(),
                    rts.getUnvisitedButAdjacentArenas(),
                    rts.getCurrentArena(),
                    rts.getCurrentPlayerLocation());

            if(!game.batch.isDrawing()) {
                game.batch.begin();
            }

            pauseArenaGUI.draw(game.batch);

            game.batch.end();
        }




        if(gameOver){

            if (delta < 0.030f) {
                deathWorld.setDelta(delta);
            } else {
                deathWorld.setDelta(0.030f);
            }


            deathWorld.process();
        }


/*

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all());
        IntBag entityIds = subscription.getEntities();
        System.out.println("Number of entities of screen is + " + entityIds.size());

        System.out.println("Frame rate is : " + Gdx.graphics.getFramesPerSecond());*/


    }




    public void endGame(World world){
        for(BaseSystem s: world.getSystems()){
            if(!(s instanceof RenderingSystem)) {
                s.setEnabled(false);
            }
        }
    }

    public void unpauseWorld(World world){

        for(BaseSystem s: world.getSystems()){
                s.setEnabled(true);
        }

    }

    public void drawScreenBorder(SpriteBatch batch, TextureAtlas atlas){
        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        batch.setColor(new Color(Color.BLACK));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY + gamecam.viewportHeight - Measure.units(5f), gamecam.viewportWidth, Measure.units(5f));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY, gamecam.viewportWidth, Measure.units(5f));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX, camY, Measure.units(5f), gamecam.viewportHeight);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), camX + gamecam.viewportWidth - Measure.units(5f), camY, Measure.units(5f), gamecam.viewportHeight);
        batch.setColor(new Color(Color.WHITE));
    }


    public void drawHUD(World world, OrthographicCamera gamecam){

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;


        //BORDER


        Array<TextureRegion> healthRegions = new Array<TextureRegion>();

        for(int i = 1; i <= stats.health; i++){
            if(i <= stats.health && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("item/heart", 0));
            } else if(stats.health % 2 != 0 && i == stats.health){
                healthRegions.add(atlas.findRegion("item/heart", 1));
            }
        }

        int emptyHealth = stats.maxHealth - stats.health;
        emptyHealth = (emptyHealth % 2 == 0) ? emptyHealth : emptyHealth - 1;

        for(int i = 1; i <= emptyHealth; i++) {
            if(i <= emptyHealth && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("item/heart", 2));
            }
        }

        float screenoffset = Measure.units(0f);

        int count = 0;

        for(int i = 0; i < healthRegions.size; i++) {
            game.batch.draw(healthRegions.get(i),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (110 * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(5f),
                    MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
            count++;
        }

        for(int i = count; i < stats.armor + count; i++) {
            game.batch.draw(atlas.findRegion("item/armor"),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (110 * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(5f),
                    MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
            //count++;
        }

        PickUp p = new MoneyPlus1();

        game.batch.draw(atlas.findRegion(p.getRegionName().getLeft(), p.getRegionName().getRight()),
                gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset,
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(11f),
                Measure.units(2f), Measure.units(2f));

        currencyFont.draw(game.batch, "" + currencyComponent.money,
                gamecam.position.x - (gamecam.viewportWidth / 2) + Measure.units(5f),
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(9f),
                Measure.units(7f), Align.left, true);

        p = new KeyUp();

        game.batch.draw(atlas.findRegion(p.getRegionName().getLeft(), p.getRegionName().getRight()),
                gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset,
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(15f),
                Measure.units(3f), Measure.units(3f));

        currencyFont.draw(game.batch, "" + currencyComponent.keys,
                gamecam.position.x - (gamecam.viewportWidth / 2) + Measure.units(5f),
                gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(12.3f),
                Measure.units(5f), Align.center, true);
    }

    @Override
    public void resize(int width, int height) {
        //Updates the view port to the designated width and height.
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
        world.dispose();
    }

    public class gestures extends AbstractGestureDectector {

        @Override
        public boolean tap(float x, float y, int count, int button) {

            if (gameOver) {
                game.setScreen(new MenuScreen(game));
                world.dispose();
                gameOver = false;
                return true;
            }



            if(isPaused) {

                Vector3 touchInput = new Vector3(x, y, 0);
                gameport.unproject(touchInput);

                System.out.println(pauseWorld.isReturnToMainMenuTouched(touchInput.x, touchInput.y));

                if(pauseWorld.isReturnToMainMenuTouched(touchInput.x, touchInput.y)){
                    game.setScreen(new MenuScreen(game));
                    world.dispose();
                }

            }

            return true;
        }



    }

}


