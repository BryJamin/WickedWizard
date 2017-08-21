package com.byrjamin.wickedwizard.screens.world.menu;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.screens.world.WorldContainer;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.utils.GameDelta;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 20/08/2017.
 */

public class MenuBackDropWorld extends AbstractGestureDectector implements WorldContainer {

    private World world;
    private MainGame game;
    private Viewport gameport;

    private TextureAtlas atlas;
    private Preferences settings;


    private static final float musicButtonPosX = Measure.units(5f);
    private static final float musicButtonPosY = Measure.units(5f);

    private static final float soundButtonPosX = Measure.units(17.5f);
    private static final float soundButtonPosY = Measure.units(5f);

    private static final float settingsButtonPosX = Measure.units(30f);
    private static final float settingsButtonPosY = Measure.units(5f);

    private static final float smallButtonSize = Measure.units(10f);



    public MenuBackDropWorld(MainGame game, Viewport gameport){
        this.game = game;
        this.atlas = game.assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        this.gameport = gameport;

        settings = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);

        createWorld();

    }


    @Override
    public void createWorld() {


        WorldConfiguration backDropconfig = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new ActionOnTouchSystem(),
                        new AnimationSystem(),
                        new StateSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, game.assetManager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();

        world = new World(backDropconfig);


        Entity backdrop = world.createEntity();
        backdrop.edit().add(new PositionComponent(0,0));
        backdrop.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.MAIN_MENU_BACKDROP), MainGame.GAME_WIDTH, MainGame.GAME_HEIGHT, TextureRegionComponent.BACKGROUND_LAYER_FAR,
                ColorResource.RGBtoColor(137, 207, 240, 1)));



        boolean musicOn = settings.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);
        boolean soundOn = settings.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

        float x = musicButtonPosX;
        float y = musicButtonPosY;

        Entity musicSetting = world.createEntity();
        musicSetting.edit().add(new PositionComponent(x, y));
        musicSetting.edit().add(new CollisionBoundComponent(new Rectangle(x,y, smallButtonSize, smallButtonSize)));
        TextureRegionComponent trc = new TextureRegionComponent(musicOn ? atlas.findRegion(TextureStrings.SETTINGS_MUSIC_ON) : atlas.findRegion(TextureStrings.SETTINGS_MUSIC_OFF),
                smallButtonSize, smallButtonSize, TextureRegionComponent.PLAYER_LAYER_MIDDLE);
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


        x = soundButtonPosX;
        y = soundButtonPosY;

        Entity soundSetting = world.createEntity();
        soundSetting.edit().add(new PositionComponent(x, y));
        soundSetting.edit().add(new CollisionBoundComponent(new Rectangle(x,y, smallButtonSize, smallButtonSize)));
        trc = new TextureRegionComponent(soundOn ? atlas.findRegion(TextureStrings.SETTINGS_SOUND_ON) : atlas.findRegion(TextureStrings.SETTINGS_SOUND_OFF),
                smallButtonSize, smallButtonSize, TextureRegionComponent.PLAYER_LAYER_MIDDLE);
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


        x = settingsButtonPosX;
        y = settingsButtonPosY;

        Entity goToSettings = world.createEntity();
        goToSettings.edit().add(new PositionComponent(x, y));
        goToSettings.edit().add(new CollisionBoundComponent(new Rectangle(x,y, smallButtonSize, smallButtonSize)));
        trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.GO_TO_SETTINGS),
                smallButtonSize, smallButtonSize, TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        goToSettings.edit().add(trc);
        goToSettings.edit().add(new ActionOnTouchComponent(new Action() {

            private MenuScreen.MenuType previousMenuType = MenuScreen.MenuType.MAIN;

            @Override
            public void performAction(World world, Entity e) {

                MenuScreen menuScreen = (MenuScreen) game.getScreen();

                if(menuScreen.getMenuType() != MenuScreen.MenuType.SETTING){
                    previousMenuType = menuScreen.getMenuType();
                    menuScreen.setMenuType(MenuScreen.MenuType.SETTING);
                } else {
                    menuScreen.setMenuType(previousMenuType);
                }

            }
        }));

    }


    public void setUpMusicEntity(Entity musicEntity, boolean musicOn) {
        musicEntity.getComponent(AnimationComponent.class).animations.put(AnimationStateComponent.DEFAULT,
                new Animation<TextureRegion>(0.15f / 1f, musicOn ?
                        atlas.findRegions(TextureStrings.SETTINGS_MUSIC_ON) :
                        atlas.findRegions(TextureStrings.SETTINGS_MUSIC_OFF), Animation.PlayMode.LOOP));
    }

    public void setUpSoundEntity(Entity soundEntity, boolean soundOn) {
        soundEntity.getComponent(AnimationComponent.class).animations.put(AnimationStateComponent.DEFAULT,
                new Animation<TextureRegion>(0.15f / 1f, soundOn ?
                        atlas.findRegions(TextureStrings.SETTINGS_SOUND_ON) :
                        atlas.findRegions(TextureStrings.SETTINGS_SOUND_OFF), Animation.PlayMode.LOOP));
    }


    @Override
    public void process(float delta) {
        GameDelta.delta(world, delta);
    }

    @Override
    public World getWorld() {
        return world;
    }


    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchInput = gameport.unproject(new Vector3(x, y, 0));
        return world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
    }
}
