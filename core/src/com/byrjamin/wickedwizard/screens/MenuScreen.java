package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.GravitySystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.CollisionSystem;
import com.byrjamin.wickedwizard.screens.world.DevModeMenuWorld;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.Measure;

//TODO


/**
 * Created by Home on 15/10/2016.
 */
public class MenuScreen extends AbstractScreen {

    private OrthographicCamera gamecam;

    private Viewport gameport;

    private TextureAtlas atlas;
    private AssetManager manager;

    private World world;

    private DevModeMenuWorld devModeMenuWorld;

    private GestureDetector gestureDetector;

    private Preferences settings;
    private Preferences devSettings;


    public enum MenuType {
        MAIN, DEV, SETTING, DOWN;
    }

    private MenuType menuType;


    //TODO IF you ever click in the deck area don't cast any spells

    public MenuScreen(MainGame game) {
        super(game);

        settings = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        devSettings = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY);


        String menuTypeName = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY).getString(PreferenceStrings.DEV_MENU_IS_DEV, MenuType.DEV.name());

        menuType = menuTypeName.equals(MenuType.DEV.name()) ? MenuType.DEV : MenuType.MAIN;

        gestureDetector = new GestureDetector(new gestures());
        manager = game.manager;
        atlas = game.manager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        Assets.initialize(game.manager);

        gamecam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameport = new FitViewport(MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, gamecam);


        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        createMenu();

        devModeMenuWorld = new DevModeMenuWorld(game, gameport);

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
                        new MusicSystem(manager)

                )
                .build();

        world = new World(config);
        world.getSystem(MusicSystem.class).playMainMenuMusic();




        Entity backdrop = world.createEntity();
        backdrop.edit().add(new PositionComponent(0,0));
        backdrop.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.MAIN_MENU_BACKDROP), MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, TextureRegionComponent.BACKGROUND_LAYER_FAR,
                ColorResource.RGBtoColor(137, 207, 240, 1)));

        MenuButton mb = new MenuButton(Assets.medium, atlas.findRegion(TextureStrings.BLOCK));

        Color foreGround = new Color(Color.BLACK);
        Color backGround = new Color(0,0,0,0);


        Entity startGame = mb.createButton(world, MenuStrings.START, CenterMath.offsetX(MainGame.GAME_WIDTH, Measure.units(30f))
                ,MainGame.GAME_HEIGHT / 2 + Measure.units(5f),
                Measure.units(30f),
                Measure.units(10f),
                foreGround,
                backGround);

        startGame.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.setScreen(new PlayScreen(game));
                world.getSystem(MusicSystem.class).stopMusic();
            }
        }));

        Entity startTutorial = mb.createButton(world, "Tutorial", CenterMath.offsetX(MainGame.GAME_WIDTH, Measure.units(30f))
                ,MainGame.GAME_HEIGHT / 2 - Measure.units(10f),
                Measure.units(30f),
                Measure.units(10f),
                foreGround,
                backGround);

        startTutorial.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.setScreen(new PlayScreen(game, new PlayScreenConfig(PlayScreenConfig.Spawn.TUTORIAL, 0)));
                world.getSystem(MusicSystem.class).stopMusic();

            }
        }));


        boolean musicOn = settings.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);
        boolean soundOn = settings.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);


        float x = Measure.units(5f);
        float y = Measure.units(5);

        Entity musicSetting = world.createEntity();
        musicSetting.edit().add(new PositionComponent(x, y));
        musicSetting.edit().add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(10f), Measure.units(10f))));
        TextureRegionComponent trc = new TextureRegionComponent(musicOn ? atlas.findRegion(TextureStrings.SETTINGS_MUSIC_ON) : atlas.findRegion(TextureStrings.SETTINGS_MUSIC_OFF),
                Measure.units(10f), Measure.units(10f), TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        musicSetting.edit().add(trc);
        musicSetting.edit().add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, musicOn ? atlas.findRegions(TextureStrings.SETTINGS_MUSIC_ON) : atlas.findRegions(TextureStrings.SETTINGS_MUSIC_OFF), Animation.PlayMode.LOOP));
        musicSetting.edit().add(new AnimationComponent(animMap));
        musicSetting.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                boolean musicOn = settings.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);
                settings.putBoolean(PreferenceStrings.SETTINGS_MUSIC, !musicOn).flush();
                setUpMusicEntity(e, !musicOn);
            }
        }));


        x = Measure.units(17.5f);
        y = Measure.units(5);

        Entity soundSetting = world.createEntity();
        soundSetting.edit().add(new PositionComponent(x, y));
        soundSetting.edit().add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(10f), Measure.units(10f))));
        trc = new TextureRegionComponent(soundOn ? atlas.findRegion(TextureStrings.SETTINGS_SOUND_ON) : atlas.findRegion(TextureStrings.SETTINGS_SOUND_OFF),
                Measure.units(10f), Measure.units(10f), TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        soundSetting.edit().add(trc);
        soundSetting.edit().add(new AnimationStateComponent(0));
        animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, soundOn ? atlas.findRegions(TextureStrings.SETTINGS_SOUND_ON) : atlas.findRegions(TextureStrings.SETTINGS_SOUND_OFF), Animation.PlayMode.LOOP));
        soundSetting.edit().add(new AnimationComponent(animMap));
        soundSetting.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                boolean soundOn = settings.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);
                settings.putBoolean(PreferenceStrings.SETTINGS_SOUND, !soundOn).flush();
                setUpSoundEntity(e, !soundOn);
            }
        }));

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
        if(menuType == MenuType.MAIN) {
          //  world.process();
        } else if(menuType == MenuType.DEV){
            devModeMenuWorld.process(delta);
        }
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
        world.dispose();
    }

    public void setUpMusicEntity(Entity musicEntity, boolean musicOn) {
            musicEntity.getComponent(AnimationComponent.class).animations.put(0,
                    new Animation<TextureRegion>(0.15f / 1f, musicOn ?
                            atlas.findRegions(TextureStrings.SETTINGS_MUSIC_ON) :
                            atlas.findRegions(TextureStrings.SETTINGS_MUSIC_OFF), Animation.PlayMode.LOOP));
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

            if(menuType == MenuType.MAIN) {
                world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
            } else if(menuType == MenuType.DEV){
                devModeMenuWorld.getWorld().getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
            }
            return true;
        }


        @Override
        public boolean longPress(float x, float y) {

            switch(menuType) {
                case DEV: menuType = MenuType.MAIN;
                    devSettings.putString(PreferenceStrings.DEV_MENU_IS_DEV, menuType.name());
                    devSettings.flush();
                    break;
                case MAIN: menuType = MenuType.DEV;
                    devSettings.putString(PreferenceStrings.DEV_MENU_IS_DEV, menuType.name());
                    devSettings.flush();
                    break;
            }

            return true;
        }
    }

}


