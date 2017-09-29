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
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.PreferenceStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.screens.MenuButton;
import com.bryjamin.wickedwizard.screens.MenuScreen;
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

    private static final float border = Measure.units(17.5f);

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
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        //new FindPlayerSystem(player),
                        new ActionOnTouchSystem(),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(game.batch, game.assetManager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();


        world = new World(config);


        Entity title = new MenuButton.MenuButtonBuilder(com.bryjamin.wickedwizard.assets.FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(new Color(Color.BLACK))
                .backgroundColor(new Color(0, 0, 0, 0))
                .build()
                .createButton(world,
                        MenuStrings.SETTINGS,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth),
                        Measure.units(50f));



        final MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .width(buttonWidth)
                .height(buttonHeight)
                .foregroundColor(buttonForeground)
                .backgroundColor(new Color(0,0,0,0));


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
                                                MenuScreen.setMenuType(MenuScreen.MenuType.MAIN);
                                                game.setScreen(new MenuScreen(game));
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



        //GUIDE LINE

        boolean bool = Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GUIDELINE, true);

        Entity guideLine = menuButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        boolean guideLineBool = !Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_GUIDELINE, true);
                        Gdx.app.getPreferences(PreferenceStrings.SETTINGS).putBoolean(PreferenceStrings.SETTINGS_GUIDELINE, guideLineBool).flush();
                        e.getComponent(TextureFontComponent.class).text = guideLineBool ? MenuStrings.SETTINGS_GUIDELINE_ON : MenuStrings.SETTINGS_GUIDELINE_OFF;
                    }
                })
                .build()
                .createButton(world,
                        bool? MenuStrings.SETTINGS_GUIDELINE_ON : MenuStrings.SETTINGS_GUIDELINE_OFF,
                        gameport.getCamera().viewportWidth - buttonWidth - border,
                        Measure.units(35f));




        bool = Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_AUTOFIRE, true);

        Entity autoFire = menuButtonBuilder
                .action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        boolean bool = !Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_AUTOFIRE, true);
                        Gdx.app.getPreferences(PreferenceStrings.SETTINGS).putBoolean(PreferenceStrings.SETTINGS_AUTOFIRE, bool).flush();
                        e.getComponent(TextureFontComponent.class).text = bool ? MenuStrings.SETTINGS_AUTOFIRE_ON : MenuStrings.SETTINGS_AUTOFIRE_OFF;
                    }
                })
                .build()
                .createButton(world,
                        bool? MenuStrings.SETTINGS_AUTOFIRE_ON : MenuStrings.SETTINGS_AUTOFIRE_OFF,
                        border,
                        Measure.units(35f));







        Entity backToMainMenu = menuButtonBuilder.
                action(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        MenuScreen.goBack();
                    }
                })
                .build()
                .createButton(
                        world,
                        MenuStrings.BACK,
                        MainGame.GAME_WIDTH - Measure.units(30f) - Measure.units(5f)
                        , Measure.units(5f));


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
