package com.bryjamin.wickedwizard.screens.world.menu;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.GameDelta;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 17/08/2017.
 */

public class SettingsWorld extends com.bryjamin.wickedwizard.utils.AbstractGestureDectector implements com.bryjamin.wickedwizard.screens.world.WorldContainer {

    private final com.bryjamin.wickedwizard.MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;


    private static final float buttonWidth = Measure.units(30f);
    private static final float buttonHeight = Measure.units(10f);

    private static final Color buttonForeground = new Color(Color.BLACK);
    private static final Color buttonBackground = new Color(Color.WHITE);

    private com.bryjamin.wickedwizard.screens.world.AreYouSureWorld areYouSureWorld;


    private enum STATE {
        NORMAL, AREYOUSURE
    }

    private STATE state = STATE.NORMAL;


    private World world;

    public SettingsWorld(com.bryjamin.wickedwizard.MainGame game, Viewport viewport){
        this.game = game;
        this.gameport = viewport;
        this.atlas = game.assetManager.get(com.bryjamin.wickedwizard.assets.FileLocationStrings.spriteAtlas);


        createWorld();
    }




    public void createWorld(){

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        //new FindPlayerSystem(player),
                        new com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem(),
                        new com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem(game.batch, game.assetManager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();


        world = new World(config);


        com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder menuButtonBuilder = new com.bryjamin.wickedwizard.screens.MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(buttonForeground)
                .backgroundColor(new Color(0,0,0,0));


        Entity settingsText =
                menuButtonBuilder.build().createButton(
                        world,
                        MenuStrings.SETTINGS,
                        Measure.units(0f),
                        Measure.units(50f));


        menuButtonBuilder
                .backgroundColor(buttonBackground)
                .action(new Action() {
                            @Override
                            public void performAction(World world, Entity e) {

                                areYouSureWorld = new com.bryjamin.wickedwizard.screens.world.AreYouSureWorld(game, gameport, MenuStrings.ARE_YOU_SURE_RESET_PROGRESS,
                                        new Action() {
                                            @Override
                                            public void performAction(World world, Entity e) {
                                                DataSave.clearData();
                                                game.getScreen().dispose();
                                                game.setScreen(new com.bryjamin.wickedwizard.screens.MenuScreen(game));
                                            }
                                        },
                                        new Action() {
                                            @Override
                                            public void performAction(World world, Entity e) {
                                                state = STATE.NORMAL;
                                            }
                                        }
                                );


                                state = STATE.AREYOUSURE;
                            }
                        });


        Entity resetProgress =
                menuButtonBuilder.build().createButton(
                        world,
                        MenuStrings.RESET,
                        CenterMath.offsetX(com.bryjamin.wickedwizard.MainGame.GAME_WIDTH, buttonWidth)
                        , Measure.units(22.5f));





        menuButtonBuilder.action(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                boolean guideLineBool = !Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GUIDELINE, true);
                Gdx.app.getPreferences(PreferenceStrings.SETTINGS).putBoolean(PreferenceStrings.SETTINGS_GUIDELINE, guideLineBool).flush();
                e.getComponent(TextureFontComponent.class).text = guideLineBool ? MenuStrings.SETTINGS_GUIDELINE_ON : MenuStrings.SETTINGS_GUIDELINE_OFF;
            }
        });


        boolean bool = Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GUIDELINE, true);

        Entity guideLine = menuButtonBuilder.build().createButton(world,
                bool? MenuStrings.SETTINGS_GUIDELINE_ON : MenuStrings.SETTINGS_GUIDELINE_OFF,
                CenterMath.offsetX(com.bryjamin.wickedwizard.MainGame.GAME_WIDTH, buttonWidth),
                Measure.units(35f));



    }


    @Override
    public void process(float delta) {
        switch (state){
            case NORMAL:
            default:
                GameDelta.delta(world, delta);
                break;
            case AREYOUSURE:
                areYouSureWorld.process(delta);
                break;
        }
    }

    @Override
    public World getWorld() {
        return world;
    }


    @Override
    public boolean tap(float x, float y, int count, int button) {

        switch (state){

            case NORMAL:
            default:
                Vector3 touchInput = gameport.unproject(new Vector3(x, y, 0));
                return world.getSystem(com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
            case AREYOUSURE:
                return areYouSureWorld.tap(x, y, count, button);

        }
    }
}
