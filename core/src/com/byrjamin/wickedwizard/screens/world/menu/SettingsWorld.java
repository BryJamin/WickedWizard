package com.byrjamin.wickedwizard.screens.world.menu;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.FontAssets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.screens.DataSave;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.screens.world.AreYouSureWorld;
import com.byrjamin.wickedwizard.screens.world.WorldContainer;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.GameDelta;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 17/08/2017.
 */

public class SettingsWorld extends AbstractGestureDectector implements WorldContainer {

    private final MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;


    private static final float buttonWidth = Measure.units(30f);
    private static final float buttonHeight = Measure.units(10f);

    private static final Color buttonForeground = new Color(Color.BLACK);
    private static final Color buttonBackground = new Color(Color.WHITE);

    private AreYouSureWorld areYouSureWorld;


    private enum STATE {
        NORMAL, AREYOUSURE
    }

    private STATE state = STATE.NORMAL;


    private World world;

    public SettingsWorld(MainGame game, Viewport viewport){
        this.game = game;
        this.gameport = viewport;
        this.atlas = game.assetManager.get(FileLocationStrings.spriteAtlas);


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


        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(FontAssets.medium, atlas.findRegion(TextureStrings.BLOCK))
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

                                areYouSureWorld = new AreYouSureWorld(game, gameport, MenuStrings.ARE_YOU_SURE_RESET_PROGRESS,
                                        new Action() {
                                            @Override
                                            public void performAction(World world, Entity e) {
                                                DataSave.clearData();
                                                game.getScreen().dispose();
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
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth)
                        , CenterMath.offsetY(MainGame.GAME_HEIGHT, buttonHeight));



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
                return world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
            case AREYOUSURE:
                return areYouSureWorld.tap(x, y, count, button);

        }
    }
}
