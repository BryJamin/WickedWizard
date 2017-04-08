package com.byrjamin.wickedwizard.screens;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.systems.ActiveOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.DirectionalSystem;
import com.byrjamin.wickedwizard.ecs.systems.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.FollowPositionSystem;
import com.byrjamin.wickedwizard.ecs.systems.JumpSystem;
import com.byrjamin.wickedwizard.ecs.systems.LockSystem;
import com.byrjamin.wickedwizard.ecs.systems.MoveToSystem;
import com.byrjamin.wickedwizard.ecs.systems.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.PhaseSystem;
import com.byrjamin.wickedwizard.ecs.systems.RoomTypeSystem;
import com.byrjamin.wickedwizard.ecs.systems.SpawnerSystem;
import com.byrjamin.wickedwizard.ecs.systems.TutorialSystem;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.factories.JigsawGenerator;
import com.byrjamin.wickedwizard.archive.helper.AbstractGestureDectector;
import com.byrjamin.wickedwizard.archive.helper.RoomInputAdapter;
import com.byrjamin.wickedwizard.archive.maps.Map;
import com.byrjamin.wickedwizard.ecs.systems.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.BlinkSystem;
import com.byrjamin.wickedwizard.ecs.systems.BounceCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.BulletSystem;
import com.byrjamin.wickedwizard.ecs.systems.DoorSystem;
import com.byrjamin.wickedwizard.ecs.systems.EnemyCollisionSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.FiringAISystem;
import com.byrjamin.wickedwizard.ecs.systems.GrappleSystem;
import com.byrjamin.wickedwizard.ecs.systems.GravitySystem;
import com.byrjamin.wickedwizard.ecs.systems.HealthSystem;
import com.byrjamin.wickedwizard.ecs.systems.MoveToPlayerAISystem;
import com.byrjamin.wickedwizard.ecs.systems.MovementSystem;
import com.byrjamin.wickedwizard.ecs.systems.PlayerInputSystem;
import com.byrjamin.wickedwizard.ecs.systems.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.RoomTransitionSystem;
import com.byrjamin.wickedwizard.ecs.systems.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.GroundCollisionSystem;
import com.byrjamin.wickedwizard.factories.arenas.RoomFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;

import java.util.Random;


//TODO


/**
 * Created by Home on 15/10/2016.
 */
public class PlayScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gamePort;

    public static TextureAtlas atlas;

    private Pixmap pixmap;

    private World world;
    private World deathWorld;

    private JumpComponent jumpresource;
    private HealthComponent healthResource;

    GestureDetector gestureDetector;
    GestureDetector controlschemeDetector;

    Vector3 input = new Vector3();
    Vector3 touchDownInput = new Vector3();
    Vector3 touchUpInput = new Vector3();

    private boolean gameOver = false;

    BitmapFont font = new BitmapFont();

    Map map;

    private ArenaGUI arenaGUI;
    private Random random;
    private JigsawGenerator jg;

    private RoomInputAdapter roomInputAdapter;

    private GestureDetector gameOvergestureDetector;

    private Array<Arena> arenaArray;


    //TODO IF you ever click in the deck area don't cast any spells

    public PlayScreen(MainGame game) {
        super(game);


        gestureDetector = new GestureDetector(new gestures());

        font.getData().setScale(5, 5);

        atlas = game.manager.get("sprite.atlas", TextureAtlas.class);

        map = new Map();
        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //TODO Decide whetehr to have heath on the screen or have health off in like black space.
        gamePort = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);

        //Moves the gamecamer to the (0,0) position instead of being in the center.
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        roomInputAdapter = new RoomInputAdapter(map.getActiveRoom(), gamePort);

        random = new Random();
        jg = new JigsawGenerator(13, random);

        createWorld();

    }

    public void handleInput(float dt) {

        InputMultiplexer multiplexer = new InputMultiplexer();

        if (!gameOver) {
            multiplexer.addProcessor(world.getSystem(PlayerInputSystem.class).getInputProcessor());
        } else {
            multiplexer.addProcessor(gestureDetector);
        }

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
                        new RenderingSystem(game.batch, gamecam),
                        new BoundsDrawingSystem()
                )
                .build();

        deathWorld = new World(config);

        FadeComponent fc = new FadeComponent(2.0f, false);
        fc.fadeIn = true;

        Entity e = deathWorld.createEntity();
        e.edit().add(new PositionComponent(gamecam.position.x - gamePort.getWorldWidth() / 2
                ,gamecam.position.y - gamePort.getWorldHeight() / 2 + 800));
        TextureFontComponent tfc = new TextureFontComponent("Oh dear, you seem to have died \n\n Tap to restart");
        e.edit().add(tfc);
        e.edit().add(fc);


        e = deathWorld.createEntity();
        e.edit().add(new PositionComponent(gamecam.position.x - gamePort.getWorldWidth() / 2,
                gamecam.position.y - gamePort.getWorldHeight() / 2));
        ShapeComponent sc = new ShapeComponent(gamecam.viewportWidth, gamecam.viewportHeight);
        sc.color = Color.BLACK;
        sc.color.a = 0.5f;
        sc.layer = -1;

        e.edit().add(sc);
        e.edit().add(fc);


    }


    public void createWorld(){

        arenaArray = jg.generate();
        Arena startingArena = jg.getStartingRoom();
        RoomFactory.cleanArenas(arenaArray);

        ComponentBag player = PlayerFactory.playerBag();

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new FollowPositionSystem(),
                        new ActiveOnTouchSystem(),
                        new AnimationSystem(),
                        new BlinkSystem(),
                        new BounceCollisionSystem(),
                        new BulletSystem(),
                        new EnemyCollisionSystem(),
                        new FindPlayerSystem(player),
                        new FiringAISystem(),
                        new GrappleSystem(),
                        new GravitySystem(),
                        new LockSystem(),
                        new GroundCollisionSystem(),
                        new HealthSystem(),
                        new OnDeathSystem(),
                        new FadeSystem(),
                        new TutorialSystem(),
                        new PhaseSystem(),
                        new MoveToSystem(),
                        new JumpSystem(),
                        new RoomTypeSystem(),
                        new MoveToPlayerAISystem(),
                        new PlayerInputSystem(gamecam, gamePort),
                        new StateSystem(),
                        new SpawnerSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RoomTransitionSystem(startingArena, arenaArray),
                        new DirectionalSystem(),
                        new CameraSystem(gamecam, gamePort),
                        new RenderingSystem(game.batch, gamecam),
                        new BoundsDrawingSystem(),
                        new DoorSystem()
                )
                .build();

        world = new World(config);

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

        jumpresource = entity.getComponent(JumpComponent.class);
        healthResource = entity.getComponent(HealthComponent.class);
        arenaGUI = new ArenaGUI(0, 0, arenaArray, startingArena);


    }

    public void update(float dt) {

        //TODO look into proper ways ot do delta time capping, or just make it that on desktop if the mouse is
        //TODO touching the screen pause the game.
        //caps the delta time if the game is paused for some reason.

        if (dt > 0.20) {
            System.out.println(" dt" + dt);
        }

        if (dt < 0.20f) {

            gamecam.position.set((int) map.getActiveRoom().getWizard().getCenterX(), (int) map.getActiveRoom().getWizard().getCenterY(), 0);

            if (gamecam.position.x <= gamePort.getWorldWidth() / 2) {
                gamecam.position.set(gamePort.getWorldWidth() / 2, gamecam.position.y, 0);
            } else if (gamecam.position.x + gamecam.viewportWidth / 2 >= map.getActiveRoom().WIDTH) {
                gamecam.position.set((int) (map.getActiveRoom().WIDTH - gamecam.viewportWidth / 2), (int) gamecam.position.y, 0);
            }

            if (gamecam.position.y <= gamePort.getWorldHeight() / 2) {
                gamecam.position.set(gamecam.position.x, gamePort.getWorldHeight() / 2, 0);
            } else if (gamecam.position.y + gamecam.viewportHeight / 2 >= map.getActiveRoom().HEIGHT) {
                gamecam.position.set((int) gamecam.position.x, (int) (map.getActiveRoom().HEIGHT - gamecam.viewportHeight / 2), 0);
            }

            gamecam.update();

            handleInput(dt);
            map.update(dt, gamecam);
            if (map.getActiveRoom().getWizard().isDead()) {
                gameOver = true;
            }
        }
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

        //TODO this doesn't fit the slowdown fly everywhere problem when using artemis
        if (delta < 0.20f) {
            world.setDelta(delta);
        } else {
            world.setDelta(0.20f);
        }

        if(gameOver){
            pauseWorld(world);
        }

        handleInput(world.delta);
        world.process();

        if(world.getSystem(FindPlayerSystem.class).getPC(HealthComponent.class).health <= 0 && !gameOver){
            gameOver = true;
            jg.generateTutorial = false;
            createDeathScreenWorld();
        }

        if(!gameOver) {
            world.getSystem(RoomTransitionSystem.class).updateGUI(arenaGUI, gamecam);
        }

        drawHUD(world, gamecam);
        arenaGUI.draw(game.batch);

        if(gameOver){

            if (delta < 0.20f) {
                deathWorld.setDelta(delta);
            } else {
                deathWorld.setDelta(0.20f);
            }


            deathWorld.process();
        }

        //pauseWorld(world);

       // System.out.println(Gdx.graphics.getFramesPerSecond());
    }


    public void pauseWorld(World world){
        for(BaseSystem s: world.getSystems()){
            if(!(s instanceof RenderingSystem)) {
                s.setEnabled(false);
            }
        }

    }

    public void drawHUD(World world, OrthographicCamera gamecam){

        if (!game.batch.isDrawing()) {
            game.batch.begin();
        }

        Array<TextureRegion> healthRegions = new Array<TextureRegion>();

        for(int i = 1; i <= healthResource.health; i++){
            if(i <= healthResource.health && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("heart", 0));
            } else if(healthResource.health % 2 != 0 && i == healthResource.health){
                healthRegions.add(atlas.findRegion("heart", 1));
            }
        }

        int emptyHealth = (int) healthResource.maxHealth - (int) healthResource.health;
        emptyHealth = (emptyHealth % 2 == 0) ? emptyHealth : emptyHealth - 1;

        for(int i = 1; i <= emptyHealth; i++) {
            if(i <= emptyHealth && i % 2 == 0) {
                healthRegions.add(atlas.findRegion("heart", 2));
            }
        }

        for(int i = 1; i <= healthRegions.size; i++) {
            game.batch.draw(healthRegions.get(i - 1), gamecam.position.x - (gamecam.viewportWidth / 2) + (100 * i), gamecam.position.y + (gamecam.viewportHeight / 2) - 220, MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
        }

        for (int i = 1; i <= jumpresource.jumps; i++) {
            game.batch.draw(atlas.findRegion("bullet_blue"), gamecam.position.x - (gamecam.viewportWidth / 2) + 50 + (50 * i), gamecam.position.y + (gamecam.viewportHeight / 2) - 280, MainGame.GAME_UNITS * 2.5f, MainGame.GAME_UNITS * 2.5f);
        }

    }

    @Override
    public void resize(int width, int height) {
        //Updates the view port to the designated width and height.
        gamePort.update(width, height);

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
                createWorld();
                gameOver = false;
            }

            return true;
        }

    }

}


