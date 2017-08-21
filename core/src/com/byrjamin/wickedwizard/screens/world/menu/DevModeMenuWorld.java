package com.byrjamin.wickedwizard.screens.world.menu;

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
import com.byrjamin.wickedwizard.screens.world.WorldContainer;
import com.byrjamin.wickedwizard.utils.GameDelta;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 28/07/2017.
 */

public class DevModeMenuWorld implements WorldContainer {

    private MainGame game;
    private Viewport gameport;

    private TextureAtlas atlas;

    private Preferences settings;
    private Preferences devToolPrefs;

    private World world;


    public DevModeMenuWorld(MainGame game, Viewport gameport) {

        this.game = game;
        this.gameport = gameport;
        atlas = game.assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);

        createWorld();

    }


    @Override
    public void createWorld() {
        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new ActionOnTouchSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, game.assetManager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();

        world = new World(config);

        settings = Gdx.app.getPreferences(PreferenceStrings.SETTINGS);
        devToolPrefs = Gdx.app.getPreferences(PreferenceStrings.DEV_MODE_PREF_KEY);

        boolean isBound = devToolPrefs.getBoolean(PreferenceStrings.DEV_BOUND, false);
        boolean isGod = devToolPrefs.getBoolean(PreferenceStrings.DEV_GODMODE, false);

        Color foreground = Color.WHITE;
        Color background = Color.BLACK;


        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(Assets.small, atlas.findRegion(TextureStrings.BLOCK))
                .width(Measure.units(30))
                .height(Measure.units(10f))
                .foregroundColor(foreground)
                .backgroundColor(background);


        Entity boundOption = menuButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        boolean isBound = devToolPrefs.getBoolean(PreferenceStrings.DEV_BOUND, true);
                        devToolPrefs.putBoolean(PreferenceStrings.DEV_BOUND, !isBound).flush();
                        e.getComponent(TextureFontComponent.class).text = !isBound ? "Bounds on" : "Bounds off";
                    }
                })
                .build()
                .createButton(world,  isBound ? "Bounds on" : "Bounds off", Measure.units(5f), Measure.units(30));

        Entity godOption = menuButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        boolean isGod = devToolPrefs.getBoolean(PreferenceStrings.DEV_GODMODE, true);

                        System.out.println(isGod);

                        devToolPrefs.putBoolean(PreferenceStrings.DEV_GODMODE, !isGod).flush();
                        e.getComponent(TextureFontComponent.class).text = !isGod ? "GodMode on" : "GodMode off";
                    }
                })
                .build()
                .createButton(world,  isGod ? "GodMode on" : "GodMode off", Measure.units(5f), Measure.units(20));

        createBossAndRoomSelectors();

    }

    private void createBossAndRoomSelectors(){


        MenuButton.MenuButtonBuilder startButtonBuilder = new MenuButton.MenuButtonBuilder(Assets.small, atlas.findRegion(TextureStrings.BLOCK))
                .width(Measure.units(10f))
                .height(Measure.units(10f))
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(Color.WHITE));

        MenuButton.MenuButtonBuilder selectorButtonBuilder = new MenuButton.MenuButtonBuilder(Assets.small, atlas.findRegion(TextureStrings.BLOCK))
                .width(Measure.units(7.5f))
                .height(Measure.units(7.5f))
                .foregroundColor(new Color(Color.WHITE))
                .backgroundColor(new Color(Color.WHITE));


        final Entity bossStartbutton = startButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.getScreen().dispose();
                        game.setScreen(new PlayScreen(game, new PlayScreenConfig(PlayScreenConfig.Spawn.BOSS, Integer.parseInt(e.getComponent(TextureFontComponent.class).text))));
                    }
                })
                .build()
                .createButton(world, devToolPrefs.getString(PreferenceStrings.DEV_BOSS_NUMBER, "0"), Measure.units(20f), Measure.units(45f));

        Entity bossSelecterButtonUp = selectorButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        int i = Integer.parseInt(bossStartbutton.getComponent(TextureFontComponent.class).text);
                        String bossSetting = Integer.toString(i < 8 ? i + 1 : i);
                        devToolPrefs.putString(PreferenceStrings.DEV_BOSS_NUMBER, bossSetting).flush();
                        bossStartbutton.getComponent(TextureFontComponent.class).text = bossSetting;
                    }
                })
                .build()
                .createButton(world, "", Measure.units(10f), Measure.units(50));


        Entity bossSelecterButtonDown = selectorButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        int i = Integer.parseInt(bossStartbutton.getComponent(TextureFontComponent.class).text);
                        String bossSetting = Integer.toString(i > 0 ? i - 1 : i);
                        devToolPrefs.putString(PreferenceStrings.DEV_BOSS_NUMBER, bossSetting).flush();
                        bossStartbutton.getComponent(TextureFontComponent.class).text = bossSetting;
                    }
                })
                .build()
                .createButton(world, "", Measure.units(10f), Measure.units(40f));




        final Entity levelStartbutton = startButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.getScreen().dispose();
                        game.setScreen(new PlayScreen(game,
                                new PlayScreenConfig(
                                        PlayScreenConfig.Spawn.ARENA,
                                        Integer.parseInt(devToolPrefs.getString(PreferenceStrings.DEV_ROOM_LEVEL, "0")),
                                        Integer.parseInt(devToolPrefs.getString(PreferenceStrings.DEV_ROOM_NUMBER, "0")))));
                    }
                })
                .build()
                .createButton(world, devToolPrefs.getString(PreferenceStrings.DEV_ROOM_LEVEL, "0"), Measure.units(80f), Measure.units(45f));


        Entity levelSelecterButtonUp = selectorButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        int i = Integer.parseInt(levelStartbutton.getComponent(TextureFontComponent.class).text);
                        String levelSetting = Integer.toString(i < 5 ? i + 1 : i);

                        devToolPrefs.putString(PreferenceStrings.DEV_ROOM_LEVEL, levelSetting).flush();
                        levelStartbutton.getComponent(TextureFontComponent.class).text = levelSetting;
                    }
                })
                .build()
                .createButton(world, "", Measure.units(70f), Measure.units(50));



        Entity levelSelecterButtonDown = selectorButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        int i = Integer.parseInt(levelStartbutton.getComponent(TextureFontComponent.class).text);
                        String levelSetting = Integer.toString(i > 0 ? i - 1 : i);


                        devToolPrefs.putString(PreferenceStrings.DEV_ROOM_LEVEL, levelSetting).flush();
                        levelStartbutton.getComponent(TextureFontComponent.class).text = levelSetting;
                    }
                })
                .build()
                .createButton(world, "", Measure.units(70f), Measure.units(40f));


        final Entity roomStartbutton = startButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.getScreen().dispose();
                        game.setScreen(new PlayScreen(game,
                                new PlayScreenConfig(
                                        PlayScreenConfig.Spawn.ARENA,
                                        Integer.parseInt(devToolPrefs.getString(PreferenceStrings.DEV_ROOM_LEVEL, "0")),
                                        Integer.parseInt(devToolPrefs.getString(PreferenceStrings.DEV_ROOM_NUMBER, "0")))));
                    }
                })
                .build()
                .createButton(world, devToolPrefs.getString(PreferenceStrings.DEV_ROOM_NUMBER, "0"), Measure.units(80f), Measure.units(27.5f));


        Entity roomSelecterButtonUp = selectorButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        int i = Integer.parseInt(roomStartbutton.getComponent(TextureFontComponent.class).text);
                        String levelSetting = Integer.toString(i < 30 ? i + 1 : i);


                        devToolPrefs.putString(PreferenceStrings.DEV_ROOM_NUMBER, levelSetting).flush();
                        roomStartbutton.getComponent(TextureFontComponent.class).text = levelSetting;
                    }
                })
                .build()
                .createButton(world, "", Measure.units(70f), Measure.units(30f));



        Entity roomSelecterButtonDown = selectorButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        int i = Integer.parseInt(roomStartbutton.getComponent(TextureFontComponent.class).text);
                        String levelSetting = Integer.toString(i > -1 ? i - 1 : i);


                        devToolPrefs.putString(PreferenceStrings.DEV_ROOM_NUMBER, levelSetting).flush();
                        roomStartbutton.getComponent(TextureFontComponent.class).text = levelSetting;
                    }
                })
                .build()
                .createButton(world, "", Measure.units(70f), Measure.units(20f));

    }


    @Override
    public void process(float delta) {
        GameDelta.delta(world, delta);
    }

    @Override
    public World getWorld() {
        return world;
    }
}
