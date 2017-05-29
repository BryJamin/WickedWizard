package com.byrjamin.wickedwizard.screens;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.systems.ExplosionSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ActionAfterTimeSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ConditionalActionSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ExpiryRangeSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.InCombatSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.LevelItemSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.ClearCollisionsSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GroundCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.OrbitalSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.PlatformSystem;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.factories.items.pickups.KeyUp;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ProximitySystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActiveOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.ExpireSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.JumpSystem;
import com.byrjamin.wickedwizard.ecs.systems.LockSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrappleSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.PhaseSystem;
import com.byrjamin.wickedwizard.ecs.systems.PickUpSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTypeSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.SpawnerSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ShoppingSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.FrictionSystem;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BlinkSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.BounceCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.BulletSystem;
import com.byrjamin.wickedwizard.ecs.systems.DoorSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.EnemyCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.FiringAISystem;
import com.byrjamin.wickedwizard.ecs.systems.input.GrapplePointSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GravitySystem;
import com.byrjamin.wickedwizard.ecs.systems.HealthSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.MoveToPlayerAISystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.CollisionSystem;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.RoomTransition;


import java.util.Random;


//TODO


/**
 * Created by Home on 15/10/2016.
 */
public class PlayScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gameport;

    public final TextureAtlas atlas;
    public AssetManager manager;

    private BitmapFont currencyFont;

    private World world;
    private World deathWorld;


    private PauseWorld pauseWorld;

    private boolean isPaused = false;

    private CurrencyComponent currencyComponent;
    private HealthComponent healthResource;
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

    public PlayScreen(MainGame game, boolean isTutorial) {
        super(game);
        this.isTutorial = isTutorial;
        gestureDetector = new GestureDetector(new gestures());
        manager = game.manager;
        atlas = game.manager.get("sprite.atlas", TextureAtlas.class);
        Assets.initialize(game.manager);
        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //TODO Decide whetehr to have heath on the screen or have health off in like black space.
        gameport = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);
        //Moves the gamecamer to the (0,0) position instead of being in the center.
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);
        random = new Random();
        currencyFont = game.manager.get(Assets.small, BitmapFont.class);// font size 12 pixels
        createWorld();


        Gdx.input.setCatchBackKey(true);
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

                            pauseWorld = new PauseWorld(game.batch, game.manager, gamecam);
                            pauseWorld.startWorld(world.getSystem(FindPlayerSystem.class).getPC(StatComponent.class));

                            RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);

                            pauseArenaGUI = new ArenaGUI(0, 0, Measure.units(2.5f), 8, rts.getRoomArray(), rts.getCurrentArena());

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
                        new RenderingSystem(game.batch, manager, gamecam),
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
        ShapeComponent sc = new ShapeComponent(gamecam.viewportWidth, gamecam.viewportHeight, TextureRegionComponent.FOREGROUND_LAYER_NEAR);
        sc.color = new Color(Color.BLACK);
        sc.color.a = 0.5f;
        sc.layer = -1;

        e.edit().add(sc);
        e.edit().add(fc);

    }


    public void createWorld(){

        LevelItemSystem lis = new LevelItemSystem(random);

        jg =new JigsawGenerator(game.manager,new SolitarySkin(atlas), 2 ,lis.getItemPool(), random);
        currencyFont = game.manager.get(Assets.small, BitmapFont.class);// font size 12 pixels


        jg.generateTutorial = isTutorial;

        arenaArray = jg.generate();
        Arena startingArena = jg.getStartingRoom();

        ComponentBag player = new PlayerFactory(game.manager).playerBag();

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new ExpireSystem(),
                        new ActionAfterTimeSystem(),
                        new ExplosionSystem(),
                        new OrbitalSystem(),
                        new InCombatSystem(),
                        new ExpiryRangeSystem(),
                        new ActiveOnTouchSystem(),
                        new AnimationSystem(),
                        new BlinkSystem(),
                        new CollisionSystem(),
                        new BounceCollisionSystem(),
                        new GroundCollisionSystem(),
                        new BulletSystem(),
                        new EnemyCollisionSystem(),
                        new MessageBannerSystem(),
                        new FindPlayerSystem(player),
                        new FiringAISystem(),
                        new GrapplePointSystem(),
                        new LockSystem(),
                        new HealthSystem(),
                        new OnDeathSystem(),
                        new FadeSystem(),
                        new PhaseSystem(),
                        new ProximitySystem(),
                        new FindChildSystem(),
                        new PickUpSystem(),
                        new LuckSystem(),
                        new ShoppingSystem(),
                        new RoomTypeSystem(),
                        new MoveToPlayerAISystem(),
                        new PlatformSystem(),
                        new JumpSystem(),
                        new PlayerInputSystem(gamecam, gameport),
                        new StateSystem(),
                        new SpawnerSystem(),
                        new GravitySystem(),
                        new GrappleSystem(),
                        new FrictionSystem(),
                        new ConditionalActionSystem()
                        )
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new DirectionalSystem(),
                        new FollowPositionSystem(),
                        new CameraSystem(gamecam, gameport),
                        new RenderingSystem(game.batch, manager, gamecam),
                        new BoundsDrawingSystem(),
                        new DoorSystem(),
                        lis,
                        new ChangeLevelSystem(jg, atlas),
                        new ClearCollisionsSystem(),
                        new MapTeleportationSystem(jg.getMapTracker()),
                        new RoomTransitionSystem(jg.getStartingMap())
                )
                .build();



        world = new World(config);
        world.getSystem(PlayerInputSystem.class).getPlayerInput().setWorld(world);


        world.getSystem(BoundsDrawingSystem.class).isDrawing =
                Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_BOUND, true);

        for (Bag<Component> bag : startingArena.getBagOfEntities()) {
            Entity entity = world.createEntity();
            for (Component comp : bag) {
                entity.edit().add(comp);

            }
        }

        Entity entity = world.createEntity();
        for (Component comp : player) {
            entity.edit().add(comp);
        }

        healthResource = entity.getComponent(HealthComponent.class);
        stats = entity.getComponent(StatComponent.class);


        if(Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GODMODE, false)) {
            stats.damage = 10f;
            stats.accuracy = 99f;
            stats.fireRate = 10f;
            stats.speed = 1.5f;
        }


        currencyComponent = entity.getComponent(CurrencyComponent.class);
        arenaGUI = new ArenaGUI(0, 0, arenaArray, startingArena);
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
            world.setDelta(0.02f);
        }

        if(gameOver){
            endGame(world);
        }


        handleInput(world.delta);
        world.process();


        if(world.getSystem(FindPlayerSystem.class).getPC(HealthComponent.class).health <= 0 && !gameOver){
            gameOver = true;
            jg.generateTutorial = false;
            createDeathScreenWorld();
        }

        drawHUD(world, gamecam);


        if(!gameOver) {
            if(!isPaused) {

                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);

                arenaGUI.update(world.delta,
                        gamecam.position.x + Measure.units(40),
                        gamecam.position.y + Measure.units(20),
                        rts.getVisitedArenas(),
                        rts.getUnvisitedButAdjacentArenas(),
                        rts.getCurrentArena(),
                        rts.getCurrentPlayerLocation());

            }

            //Map of the world

            arenaGUI.draw(game.batch);

            //HUD


            RoomTransition rt = world.getSystem(RoomTransitionSystem.class).entryTransition;
            if(rt != null) {
                world.getSystem(RoomTransitionSystem.class).entryTransition.draw(game.batch);
            }

            rt = world.getSystem(RoomTransitionSystem.class).exitTransition;
            if(rt != null) {
                world.getSystem(RoomTransitionSystem.class).exitTransition.draw(game.batch);
            }


        }



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

            pauseArenaGUI.draw(game.batch);
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



    public void drawHUD(World world, OrthographicCamera gamecam){

        if (!game.batch.isDrawing()) {
            game.batch.begin();
        }

        Array<TextureRegion> healthRegions = new Array<TextureRegion>();

        for(int i = 1; i <= healthResource.health; i++){
            if(i <= healthResource.health && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("item/heart", 0));
            } else if(healthResource.health % 2 != 0 && i == healthResource.health){
                healthRegions.add(atlas.findRegion("item/heart", 1));
            }
        }

        int emptyHealth = (int) healthResource.maxHealth - (int) healthResource.health;
        emptyHealth = (emptyHealth % 2 == 0) ? emptyHealth : emptyHealth - 1;

        for(int i = 1; i <= emptyHealth; i++) {
            if(i <= emptyHealth && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("item/heart", 2));
            }
        }

        float screenoffset = Measure.units(2.5f);

        int count = 0;

        for(int i = 0; i < healthRegions.size; i++) {
            game.batch.draw(healthRegions.get(i),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (110 * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(8f),
                    MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
            count++;
        }

        for(int i = count; i < stats.armor + count; i++) {
            game.batch.draw(atlas.findRegion("item/armor"),
                    gamecam.position.x - (gamecam.viewportWidth / 2) + screenoffset + (110 * i),
                    gamecam.position.y + (gamecam.viewportHeight / 2) - Measure.units(8f),
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
                Measure.units(2.5f), Measure.units(2.5f));

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

    }

    public class gestures extends AbstractGestureDectector {

        @Override
        public boolean tap(float x, float y, int count, int button) {

            if (gameOver) {
                game.setScreen(new MenuScreen(game));
                gameOver = false;
                return true;
            }



            if(isPaused) {

                Vector3 touchInput = new Vector3(x, y, 0);
                gameport.unproject(touchInput);

                System.out.println(pauseWorld.isReturnToMainMenuTouched(touchInput.x, touchInput.y));

                if(pauseWorld.isReturnToMainMenuTouched(touchInput.x, touchInput.y)){
                    game.setScreen(new MenuScreen(game));
                }

            }

            return true;
        }



    }

}


