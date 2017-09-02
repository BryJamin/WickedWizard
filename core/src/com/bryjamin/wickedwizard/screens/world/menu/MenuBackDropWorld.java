package com.bryjamin.wickedwizard.screens.world.menu;

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
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.StateSystem;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 20/08/2017.
 */

public class MenuBackDropWorld extends com.bryjamin.wickedwizard.utils.AbstractGestureDectector implements com.bryjamin.wickedwizard.screens.world.WorldContainer {

    private World world;
    private com.bryjamin.wickedwizard.MainGame game;
    private Viewport gameport;

    private TextureAtlas atlas;
    private Preferences settings;


    private static final float musicButtonPosX = Measure.units(5f);
    private static final float musicButtonPosY = Measure.units(5f);

    private static final float soundButtonPosX = Measure.units(17.5f);
    private static final float soundButtonPosY = Measure.units(5f);

    private static final float settingsButtonPosX = Measure.units(30f);
    private static final float settingsButtonPosY = Measure.units(5f);


    private static final float itemsButtonPosX = Measure.units(42.5f);
    private static final float itemsButtonPosY = Measure.units(5f);

    private static final float smallButtonSize = Measure.units(10f);



    public MenuBackDropWorld(com.bryjamin.wickedwizard.MainGame game, Viewport gameport){
        this.game = game;
        this.atlas = game.assetManager.get(com.bryjamin.wickedwizard.assets.FileLocationStrings.spriteAtlas, TextureAtlas.class);
        this.gameport = gameport;

        settings = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);

        createWorld();

    }


    @Override
    public void createWorld() {


        WorldConfiguration backDropconfig = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem(),
                        new com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem(),
                        new StateSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem(game.batch, game.assetManager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();

        world = new World(backDropconfig);


        Entity backdrop = world.createEntity();
        backdrop.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(0,0));
        backdrop.edit().add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.MAIN_MENU_BACKDROP), com.bryjamin.wickedwizard.MainGame.GAME_WIDTH, com.bryjamin.wickedwizard.MainGame.GAME_HEIGHT, TextureRegionComponent.BACKGROUND_LAYER_FAR,
                ColorResource.RGBtoColor(137, 207, 240, 1)));



        boolean musicOn = settings.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);
        boolean soundOn = settings.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

        float x = musicButtonPosX;
        float y = musicButtonPosY;

        Entity musicSetting = world.createEntity();
        musicSetting.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(x, y));
        musicSetting.edit().add(new CollisionBoundComponent(new Rectangle(x,y, smallButtonSize, smallButtonSize)));
        TextureRegionComponent trc = new TextureRegionComponent(musicOn ? atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_MUSIC_ON) : atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_MUSIC_OFF),
                smallButtonSize, smallButtonSize, TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        musicSetting.edit().add(trc);
        musicSetting.edit().add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, musicOn ? atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_MUSIC_ON) : atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_MUSIC_OFF), Animation.PlayMode.LOOP));
        musicSetting.edit().add(new AnimationComponent(animMap));
        musicSetting.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                boolean musicOn = !settings.getBoolean(PreferenceStrings.SETTINGS_MUSIC, false);
                settings.putBoolean(PreferenceStrings.SETTINGS_MUSIC, musicOn).flush();

                com.bryjamin.wickedwizard.ecs.systems.audio.MusicSystem.MUSIC_ON = musicOn;

                e.getComponent(AnimationComponent.class).animations.put(AnimationStateComponent.DEFAULT,
                        new Animation<TextureRegion>(0.15f / 1f, musicOn ?
                                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_MUSIC_ON) :
                                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_MUSIC_OFF), Animation.PlayMode.LOOP));
            }
        }));


        x = soundButtonPosX;
        y = soundButtonPosY;

        Entity soundSetting = world.createEntity();
        soundSetting.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(x, y));
        soundSetting.edit().add(new CollisionBoundComponent(new Rectangle(x,y, smallButtonSize, smallButtonSize)));
        trc = new TextureRegionComponent(soundOn ? atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_SOUND_ON) : atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_SOUND_OFF),
                smallButtonSize, smallButtonSize, TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        soundSetting.edit().add(trc);

        soundSetting.edit().add(new AnimationStateComponent(0));
        animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, soundOn ? atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_SOUND_ON) : atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_SOUND_OFF), Animation.PlayMode.LOOP));
        soundSetting.edit().add(new AnimationComponent(animMap));


        soundSetting.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                boolean soundOn = !settings.getBoolean(PreferenceStrings.SETTINGS_SOUND, false);

                settings.putBoolean(PreferenceStrings.SETTINGS_SOUND, soundOn).flush();
                com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem.SOUNDON = soundOn;

                e.getComponent(AnimationComponent.class).animations.put(AnimationStateComponent.DEFAULT,
                        new Animation<TextureRegion>(0.15f / 1f, soundOn ?
                                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_SOUND_ON) :
                                atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_SOUND_OFF), Animation.PlayMode.LOOP));
            }
        }));


        x = settingsButtonPosX;
        y = settingsButtonPosY;

        Entity goToSettings = world.createEntity();
        goToSettings.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(x, y));
        goToSettings.edit().add(new CollisionBoundComponent(new Rectangle(x,y, smallButtonSize, smallButtonSize)));
        trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.GO_TO_SETTINGS),
                smallButtonSize, smallButtonSize, TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        goToSettings.edit().add(trc);
        goToSettings.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent(new Action() {

            @Override
            public void performAction(World world, Entity e) {

                if(com.bryjamin.wickedwizard.screens.MenuScreen.getMenuType() != com.bryjamin.wickedwizard.screens.MenuScreen.MenuType.SETTING){
                    com.bryjamin.wickedwizard.screens.MenuScreen.setMenuType(com.bryjamin.wickedwizard.screens.MenuScreen.MenuType.SETTING);
                } else {
                    com.bryjamin.wickedwizard.screens.MenuScreen.resetMenuType();
                }

            }
        }));

        x = itemsButtonPosX;
        y = itemsButtonPosY;

        Entity goToItems = world.createEntity();
        goToItems.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent(x, y));
        goToItems.edit().add(new CollisionBoundComponent(new Rectangle(x,y, smallButtonSize, smallButtonSize)));
        trc = new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.SETTINGS_ITEM),
                smallButtonSize, smallButtonSize, TextureRegionComponent.PLAYER_LAYER_MIDDLE);
        goToItems.edit().add(trc);
        goToItems.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent(new Action() {

            @Override
            public void performAction(World world, Entity e) {

                if(com.bryjamin.wickedwizard.screens.MenuScreen.getMenuType() != com.bryjamin.wickedwizard.screens.MenuScreen.MenuType.ITEMS){
                    com.bryjamin.wickedwizard.screens.MenuScreen.setMenuType(com.bryjamin.wickedwizard.screens.MenuScreen.MenuType.ITEMS);
                } else {
                    com.bryjamin.wickedwizard.screens.MenuScreen.resetMenuType();
                }

            }
        }));

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
        return world.getSystem(com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
    }
}
