package com.byrjamin.wickedwizard.screens;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GravitySystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GroundCollisionSystem;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.utils.Measure;

//TODO


/**
 * Created by Home on 15/10/2016.
 */
public class MenuScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gameport;

    public final TextureAtlas atlas;
    public AssetManager manager;

    private BitmapFont currencyFont;

    private World world;

    private Rectangle startGameButton;
    private Rectangle tutorialGameButton;

    GestureDetector gestureDetector;
    private boolean gameOver = false;

    //TODO IF you ever click in the deck area don't cast any spells

    public MenuScreen(MainGame game) {
        super(game);
        gestureDetector = new GestureDetector(new gestures());
        manager = game.manager;
        atlas = game.manager.get("sprite.atlas", TextureAtlas.class);
        Assets.initialize(game.manager);

        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameport = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);


        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        createDeathScreenWorld();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }


    public void handleInput(float dt) {
        InputMultiplexer multiplexer = new InputMultiplexer();
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
                        new StateSystem(),
                        new GroundCollisionSystem(),
                        //new FindPlayerSystem(player),
                        new GravitySystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, manager, gamecam),
                        new BoundsDrawingSystem()

                )
                .build();

        world = new World(config);

        startGameButton = createButton(world, "Start", gameport.getWorldWidth() / 2
                ,gameport.getWorldHeight() / 2 + Measure.units(20f));


        tutorialGameButton = createButton(world, "Tutorial", gameport.getWorldWidth() / 2
                ,gameport.getWorldHeight() / 2 + Measure.units(10f));


        //Player

        Entity player = world.createEntity();
        player.edit().add(new PositionComponent(Measure.units(80f),Measure.units(18f)));
        player.edit().add(new CollisionBoundComponent(new Rectangle(600,900,100, 100)));
        player.edit().add(new VelocityComponent(0,0));
        player.edit().add(new GravityComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setState(0);
        player.edit().add(sc);

        IntMap<Animation<TextureRegion>> k = new IntMap<Animation<TextureRegion>>();
        k.put(0, new Animation<TextureRegion>(1/10f, atlas.findRegions("block_walk"), Animation.PlayMode.LOOP));

        player.edit().add(new AnimationComponent(k));
        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion("block_walk"),0, 0,
                Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        trc.color = new Color(Color.WHITE);
        trc.DEFAULT = new Color(Color.WHITE);
        player.edit().add(trc);


        SolitarySkin ss = new SolitarySkin(atlas);

        DecorFactory df = new DecorFactory(manager, ss);
        BackgroundFactory bf = new BackgroundFactory();


        Bag<Bag<Component>> bags = new Bag<Bag<Component>>();
        bags.add(df.wallBag(0, 0, Measure.units(5), gameport.getWorldHeight(), ss));
        bags.add(df.wallBag(0, 0, gameport.getWorldWidth(), Measure.units(10f), ss));
        bags.add(df.wallBag(0, gameport.getWorldHeight() - Measure.units(5f), gameport.getWorldWidth(), Measure.units(5), ss));

        bags.add(bf.backgroundBags(0,0, gameport.getWorldWidth(), gameport.getWorldHeight(), Measure.units(20),
                atlas.findRegions("block"), ss));

        for(Bag<Component> b  : bags) {
            Entity e = world.createEntity();
            for(Component c : b){
                e.edit().add(c);
            }
        }

     /*   Entity e = world.createEntity();
        e.edit().add(new PositionComponent(gamecam.viewportWidth / 2
                ,gamecam.position.y - gamePort.getWorldHeight() / 2 + 800));
        TextureFontComponent tfc = new TextureFontComponent(Assets.medium, "Start", gamecam.viewportWidth, 0, TextureRegionComponent.BACKGROUND_LAYER_FAR);
        e.edit().add(tfc);
        e.edit().add(new CollisionBoundComponent(new Rectangle(gamecam.viewportWidth / 2,
                gamecam.position.y - gamePort.getWorldHeight() / 2 + 700, gamecam.viewportWidth, Measure.units(10f))));*/

    }



    public Rectangle createButton(World world, String text, float x, float y){

        float width = Measure.units(20f);
        float height = Measure.units(10f);

        x = x - width / 2;
        y = y - width / 2;

        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x,y));
        TextureFontComponent tfc = new TextureFontComponent(Assets.medium, text, 0, height / 2 + Measure.units(1f), width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR);
        tfc.color = new Color(Color.BLACK);
        e.edit().add(tfc);

        Rectangle r = new Rectangle(x, y, width, height);

        e.edit().add(new CollisionBoundComponent(r));

        return r;

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

//        handleInput(world.delta);

        if (delta < 0.030f) {
            world.setDelta(delta);
        } else {
            world.setDelta(0.030f);
        }

        handleInput(world.delta);
        world.process();
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


            Vector3 touchInput = new Vector3(x, y, 0);
            gameport.unproject(touchInput);

            if (startGameButton.contains(touchInput.x,touchInput.y)) {
                game.setScreen(new PlayScreen(game, false));
            }

            if (tutorialGameButton.contains(touchInput.x,touchInput.y)) {
                game.setScreen(new PlayScreen(game, true));
            }

            return true;
        }

    }

}


