package com.byrjamin.wickedwizard.screens;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
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
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MusicStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.systems.SoundSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GravitySystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.CollisionSystem;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
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


    private Entity startGame;
    private Entity startTutorial;

    private Entity boundOption;
    private Entity godOption;

    private Entity musicSetting;

    private Entity soundSetting;




    private Entity bossSelecterbutton;
    private Entity bossStartbutton;



    GestureDetector gestureDetector;
    private boolean gameOver = false;

    private Preferences settings;

    //TODO IF you ever click in the deck area don't cast any spells

    public MenuScreen(MainGame game) {
        super(game);

        settings = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);

        gestureDetector = new GestureDetector(new gestures());
        manager = game.manager;
        atlas = game.manager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        Assets.initialize(game.manager);

        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameport = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);


        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        createMenu();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }


    public void handleInput(float dt) {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gestureDetector);
        Gdx.input.setInputProcessor(multiplexer);
    }


    public void createMenu(){

        SoundSystem soundSystem = new SoundSystem(manager);

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        new StateSystem(),
                        new CollisionSystem(),
                        new ActionOnTouchSystem(),
                        //new FindPlayerSystem(player),
                        new GravitySystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, manager, gameport),
                        new BoundsDrawingSystem(true),
                        soundSystem

                )
                .build();

        soundSystem.playMusic(MusicStrings.high_c);

        world = new World(config);

        startGame = createButton(world, "Start", gameport.getWorldWidth() / 2
                ,gameport.getWorldHeight() / 2 + Measure.units(25f));


        startTutorial = createButton(world, "Tutorial", gameport.getWorldWidth() / 2
                ,gameport.getWorldHeight() / 2 + Measure.units(10f));


        boolean isBound = settings.getBoolean(PreferenceStrings.SETTINGS_BOUND, false);
        boolean isGod = settings.getBoolean(PreferenceStrings.SETTINGS_GODMODE, false);

        boolean musicOn = settings.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);
        boolean soundOn = settings.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

        boundOption = createButton(world, isBound ? "Bounds on" : "Bounds off", Measure.units(20f), Measure.units(30));
        godOption = createButton(world, isGod ? "GodMode on" : "GodMode off", Measure.units(20f), Measure.units(20));

        float x = Measure.units(50f);
        float y = Measure.units(10f);

        musicSetting = world.createEntity();
        musicSetting.edit().add(new PositionComponent(x, y));
        musicSetting.edit().add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(10f), Measure.units(10f))));
        TextureRegionComponent trc = new TextureRegionComponent(musicOn ? atlas.findRegion(TextureStrings.SETTINGS_MUSIC_ON) : atlas.findRegion(TextureStrings.SETTINGS_MUSIC_OFF),
                Measure.units(10f), Measure.units(10f), TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        musicSetting.edit().add(trc);
        musicSetting.edit().add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, musicOn ? atlas.findRegions(TextureStrings.SETTINGS_MUSIC_ON) : atlas.findRegions(TextureStrings.SETTINGS_MUSIC_OFF), Animation.PlayMode.LOOP));
        musicSetting.edit().add(new AnimationComponent(animMap));


        x = Measure.units(60f);
        y = Measure.units(10f);

        soundSetting = world.createEntity();
        soundSetting.edit().add(new PositionComponent(x, y));
        soundSetting.edit().add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(10f), Measure.units(10f))));
        trc = new TextureRegionComponent(soundOn ? atlas.findRegion(TextureStrings.SETTINGS_SOUND_ON) : atlas.findRegion(TextureStrings.SETTINGS_SOUND_OFF),
                Measure.units(10f), Measure.units(10f), TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        soundSetting.edit().add(trc);
        soundSetting.edit().add(new AnimationStateComponent(0));
        animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, soundOn ? atlas.findRegions(TextureStrings.SETTINGS_SOUND_ON) : atlas.findRegions(TextureStrings.SETTINGS_SOUND_OFF), Animation.PlayMode.LOOP));
        soundSetting.edit().add(new AnimationComponent(animMap));
        //Player
        MenuButton mb = new MenuButton(Assets.small, atlas.findRegion(TextureStrings.BLOCK));

        bossStartbutton = mb.createButton(world, "0", Measure.units(25f), Measure.units(40f), Measure.units(10f), Measure.units(20f), new Color(Color.WHITE), new Color(Color.BLACK));
        bossStartbutton.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.setScreen(new PlayScreen(game, new PlayScreenConfig(PlayScreenConfig.Spawn.BOSS, Integer.parseInt(e.getComponent(TextureFontComponent.class).text))));
                world.getSystem(SoundSystem.class).stopMusic();
            }
        }));


        bossSelecterbutton = mb.createButton(world, "", Measure.units(10f), Measure.units(40f), Measure.units(10f), Measure.units(10f), new Color(Color.WHITE), new Color(Color.WHITE));
        bossSelecterbutton.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                int i = Integer.parseInt(bossStartbutton.getComponent(TextureFontComponent.class).text);
                bossStartbutton.getComponent(TextureFontComponent.class).text = Integer.toString(i + 1);
            }
        }));









        SolitarySkin ss = new SolitarySkin(atlas);

        DecorFactory df = new DecorFactory(manager, ss);
        BackgroundFactory bf = new BackgroundFactory();

    }



    public Entity createButton(World world, String text, float x, float y){

        SolitarySkin ss = new SolitarySkin(atlas);

        float width = Measure.units(30f);
        float height = Measure.units(10f);

        x = x - width / 2;
        y = y - width / 2;

        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x,y));
        TextureFontComponent tfc = new TextureFontComponent(Assets.medium, text, 0, height / 2 + Measure.units(1f), width, height,
                TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(Color.WHITE));


        /*        tfc.color = ss.getBackgroundTint();
        tfc.DEFAULT = ss.getBackgroundTint();*/
        e.edit().add(tfc);

        Rectangle r = new Rectangle(x, y, width, height);

        e.edit().add(new CollisionBoundComponent(r));

/*
        Entity shape = world.createEntity();
        shape.edit().add(new PositionComponent(x,y));

        ShapeComponent sc = new ShapeComponent(width,height, TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);
        sc.color = ss.getWallTint();
        sc.DEFAULT = ss.getWallTint();


        shape.edit().add(sc);*/

        return e;

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

    public void setUpMusicEntity(Entity musicEntity, boolean musicOn) {
            musicEntity.getComponent(AnimationComponent.class).animations.put(0,
                    new Animation<TextureRegion>(0.15f / 1f, musicOn ?
                            atlas.findRegions(TextureStrings.SETTINGS_MUSIC_ON) :
                            atlas.findRegions(TextureStrings.SETTINGS_MUSIC_OFF), Animation.PlayMode.LOOP));
        System.out.println(musicOn);
    }

    public void setUpSoundEntity(Entity soundEntity, boolean soundOn) {
        soundEntity.getComponent(AnimationComponent.class).animations.put(0,
                new Animation<TextureRegion>(0.15f / 1f, soundOn ?
                        atlas.findRegions(TextureStrings.SETTINGS_SOUND_ON) :
                        atlas.findRegions(TextureStrings.SETTINGS_SOUND_OFF), Animation.PlayMode.LOOP));
    }

    public class gestures extends AbstractGestureDectector {

        @Override
        public boolean tap(float x, float y, int count, int button) {


            Vector3 touchInput = new Vector3(x, y, 0);
            gameport.unproject(touchInput);

            world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);

            if (startGame.getComponent(CollisionBoundComponent.class).bound.contains(touchInput.x,touchInput.y)) {
                game.setScreen(new PlayScreen(game));
                world.getSystem(SoundSystem.class).stopMusic();
            }

            if (startTutorial.getComponent(CollisionBoundComponent.class).bound.contains(touchInput.x,touchInput.y)) {
                game.setScreen(new PlayScreen(game, new PlayScreenConfig(PlayScreenConfig.Spawn.TUTORIAL, 0)));
                world.getSystem(SoundSystem.class).stopMusic();
            }

            if (boundOption.getComponent(CollisionBoundComponent.class).bound.contains(touchInput.x,touchInput.y)) {

                boolean isBound = settings.getBoolean(PreferenceStrings.SETTINGS_BOUND, true);
                settings.putBoolean(PreferenceStrings.SETTINGS_BOUND, !isBound).flush();
                boundOption.getComponent(TextureFontComponent.class).text = !isBound ? "Bounds on" : "Bounds off";
                //game.setScreen(new PlayScreen(game, true));
            }

            if (godOption.getComponent(CollisionBoundComponent.class).bound.contains(touchInput.x,touchInput.y)) {

                boolean isGod = settings.getBoolean(PreferenceStrings.SETTINGS_GODMODE, true);
                settings.putBoolean(PreferenceStrings.SETTINGS_GODMODE, !isGod).flush();
                godOption.getComponent(TextureFontComponent.class).text = !isGod ? "GodMode on" : "GodMode off";


                //game.setScreen(new PlayScreen(game, true));
            }

            if(musicSetting.getComponent(CollisionBoundComponent.class).bound.contains(touchInput.x, touchInput.y)) {
                boolean musicOn = settings.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);
                settings.putBoolean(PreferenceStrings.SETTINGS_MUSIC, !musicOn).flush();
                setUpMusicEntity(musicSetting, !musicOn);
            }

            if(soundSetting.getComponent(CollisionBoundComponent.class).bound.contains(touchInput.x, touchInput.y)) {
                boolean soundOn = settings.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);
                settings.putBoolean(PreferenceStrings.SETTINGS_SOUND, !soundOn).flush();
                setUpSoundEntity(soundSetting, !soundOn);
            }


           // boolean isGod = settings.getBoolean(PreferenceStrings.SETTINGS_GODMODE, false);

            return true;
        }

    }

}


