package com.byrjamin.wickedwizard.screens.world;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.screens.PlayScreenConfig;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 28/07/2017.
 */

public class DevModeMenuWorld extends WorldContainer{

    private MainGame game;

    private TextureAtlas atlas;

    private Preferences settings;
    private Preferences devToolPrefs;


    public DevModeMenuWorld(MainGame game, Viewport gameport) {

        this.game = game;

        atlas = game.manager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new ActionOnTouchSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, game.manager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();

        world = new World(config);

        settings = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        devToolPrefs = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY);

        boolean isBound = devToolPrefs.getBoolean(PreferenceStrings.DEV_BOUND, false);
        boolean isGod = devToolPrefs.getBoolean(PreferenceStrings.DEV_GODMODE, false);

        MenuButton mb = new MenuButton(Assets.small, atlas.findRegion(TextureStrings.BLOCK));


        Color foreground = Color.WHITE;
        Color background = Color.BLACK;

        Entity boundOption = mb.createButton(world,  isBound ? "Bounds on" : "Bounds off", Measure.units(20f), Measure.units(30), Measure.units(30f),
        Measure.units(10f), foreground, background);

        boundOption.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                boolean isBound = devToolPrefs.getBoolean(PreferenceStrings.DEV_BOUND, true);
                devToolPrefs.putBoolean(PreferenceStrings.DEV_BOUND, !isBound).flush();
                e.getComponent(TextureFontComponent.class).text = !isBound ? "Bounds on" : "Bounds off";
            }
        }));

        Entity godOption = mb.createButton(world,  isGod ? "GodMode on" : "GodMode off", Measure.units(20f), Measure.units(20), Measure.units(30f),
                Measure.units(10f), foreground, background);
        godOption.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                boolean isGod = devToolPrefs.getBoolean(PreferenceStrings.DEV_GODMODE, true);

                System.out.println(isGod);

                devToolPrefs.putBoolean(PreferenceStrings.DEV_GODMODE, !isGod).flush();
                e.getComponent(TextureFontComponent.class).text = !isGod ? "GodMode on" : "GodMode off";
            }
        }));



        createMenu();
        createBossAndRoomSelectors();
    }


    private void createMenu(){








    }



    private void createBossAndRoomSelectors(){

        MenuButton mb = new MenuButton(Assets.small, atlas.findRegion(TextureStrings.BLOCK));


        final Entity bossStartbutton = mb.createButton(world, devToolPrefs.getString(PreferenceStrings.DEV_BOSS_NUMBER, "0"), Measure.units(20f), Measure.units(45f), Measure.units(10f), Measure.units(10), new Color(Color.BLACK), new Color(Color.WHITE));
        bossStartbutton.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new PlayScreen(game, new PlayScreenConfig(PlayScreenConfig.Spawn.BOSS, Integer.parseInt(e.getComponent(TextureFontComponent.class).text))));
            }
        }));


        Entity bossSelecterButtonUp = mb.createButton(world, "", Measure.units(10f), Measure.units(50), Measure.units(7.5f), Measure.units(7.5f), new Color(Color.WHITE), new Color(Color.WHITE));
        bossSelecterButtonUp.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                int i = Integer.parseInt(bossStartbutton.getComponent(TextureFontComponent.class).text);
                String bossSetting = Integer.toString(i < 8 ? i + 1 : i);
                devToolPrefs.putString(PreferenceStrings.DEV_BOSS_NUMBER, bossSetting).flush();
                bossStartbutton.getComponent(TextureFontComponent.class).text = bossSetting;
            }
        }));



        Entity bossSelecterButtonDown = mb.createButton(world, "", Measure.units(10f), Measure.units(40f), Measure.units(7.5f), Measure.units(7.5f), new Color(Color.WHITE), new Color(Color.WHITE));
        bossSelecterButtonDown.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                int i = Integer.parseInt(bossStartbutton.getComponent(TextureFontComponent.class).text);
                String bossSetting = Integer.toString(i > 0 ? i - 1 : i);
                devToolPrefs.putString(PreferenceStrings.DEV_BOSS_NUMBER, bossSetting).flush();
                bossStartbutton.getComponent(TextureFontComponent.class).text = bossSetting;
            }
        }));




        final Entity levelStartbutton = mb.createButton(world, devToolPrefs.getString(PreferenceStrings.DEV_ROOM_LEVEL, "0"), Measure.units(80f), Measure.units(45f), Measure.units(10f), Measure.units(10), new Color(Color.BLACK), new Color(Color.WHITE));
        levelStartbutton.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new PlayScreen(game,
                        new PlayScreenConfig(
                                PlayScreenConfig.Spawn.ARENA,
                                Integer.parseInt(devToolPrefs.getString(PreferenceStrings.DEV_ROOM_LEVEL, "0")),
                                Integer.parseInt(devToolPrefs.getString(PreferenceStrings.DEV_ROOM_NUMBER, "0")))));
            }
        }));



        Entity levelSelecterButtonUp = mb.createButton(world, "", Measure.units(70f), Measure.units(50), Measure.units(7.5f), Measure.units(7.5f), new Color(Color.WHITE), new Color(Color.WHITE));
        levelSelecterButtonUp.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                int i = Integer.parseInt(levelStartbutton.getComponent(TextureFontComponent.class).text);
                String levelSetting = Integer.toString(i < 5 ? i + 1 : i);

                devToolPrefs.putString(PreferenceStrings.DEV_ROOM_LEVEL, levelSetting).flush();
                levelStartbutton.getComponent(TextureFontComponent.class).text = levelSetting;
            }
        }));



        Entity levelSelecterButtonDown = mb.createButton(world, "", Measure.units(70f), Measure.units(40f), Measure.units(7.5f), Measure.units(7.5f), new Color(Color.WHITE), new Color(Color.WHITE));
        levelSelecterButtonDown.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                int i = Integer.parseInt(levelStartbutton.getComponent(TextureFontComponent.class).text);
                String levelSetting = Integer.toString(i > 0 ? i - 1 : i);


                devToolPrefs.putString(PreferenceStrings.DEV_ROOM_LEVEL, levelSetting).flush();
                levelStartbutton.getComponent(TextureFontComponent.class).text = levelSetting;
            }
        }));








        final Entity roomStartbutton = mb.createButton(world, devToolPrefs.getString(PreferenceStrings.DEV_ROOM_NUMBER, "0"), Measure.units(80f), Measure.units(12.5f), Measure.units(10f), Measure.units(10), new Color(Color.BLACK), new Color(Color.WHITE));
        roomStartbutton.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new PlayScreen(game,
                        new PlayScreenConfig(
                                PlayScreenConfig.Spawn.ARENA,
                                Integer.parseInt(devToolPrefs.getString(PreferenceStrings.DEV_ROOM_LEVEL, "0")),
                                Integer.parseInt(devToolPrefs.getString(PreferenceStrings.DEV_ROOM_NUMBER, "0")))));
            }
        }));



        Entity roomSelecterButtonUp = mb.createButton(world, "", Measure.units(70f), Measure.units(15f), Measure.units(7.5f), Measure.units(7.5f), new Color(Color.WHITE), new Color(Color.WHITE));
        roomSelecterButtonUp.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                int i = Integer.parseInt(roomStartbutton.getComponent(TextureFontComponent.class).text);
                String levelSetting = Integer.toString(i < 30 ? i + 1 : i);


                devToolPrefs.putString(PreferenceStrings.DEV_ROOM_NUMBER, levelSetting).flush();
                roomStartbutton.getComponent(TextureFontComponent.class).text = levelSetting;
            }
        }));



        Entity roomSelecterButtonDown = mb.createButton(world, "", Measure.units(70f), Measure.units(5f), Measure.units(7.5f), Measure.units(7.5f), new Color(Color.WHITE), new Color(Color.WHITE));
        roomSelecterButtonDown.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                int i = Integer.parseInt(roomStartbutton.getComponent(TextureFontComponent.class).text);
                String levelSetting = Integer.toString(i > -1 ? i - 1 : i);


                devToolPrefs.putString(PreferenceStrings.DEV_ROOM_NUMBER, levelSetting).flush();
                roomStartbutton.getComponent(TextureFontComponent.class).text = levelSetting;
            }
        }));


    }


    @Override
    public void process(float delta) {
        super.process(delta);
    }

    @Override
    public World getWorld() {
        return world;
    }
}
