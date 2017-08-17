package com.byrjamin.wickedwizard.screens.world;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.screens.DataSave;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.GameDelta;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 17/08/2017.
 */

public class SettingsWorld extends AbstractGestureDectector implements WorldContainer{

    private final MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;
    private final MenuButton menuButton;


    private static final float buttonWidth = Measure.units(30f);
    private static final float buttonHeight = Measure.units(10f);

    private static final Color buttonForeground = new Color(Color.BLACK);
    private static final Color buttonBackground = new Color(Color.WHITE);


    private World world;

    public SettingsWorld(MainGame game, Viewport viewport){
        this.game = game;
        this.gameport = viewport;
        this.atlas = game.assetManager.get(FileLocationStrings.spriteAtlas);
        menuButton = new MenuButton(Assets.medium, atlas.findRegion(TextureStrings.BLOCK));
        createWorld();
    }




    public World createWorld(){

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


        Entity startTutorial =
                menuButton.createButton(
                        world,
                        MenuStrings.RESET,
                        CenterMath.offsetX(MainGame.GAME_WIDTH, buttonWidth)
                        , CenterMath.offsetY(MainGame.GAME_HEIGHT, buttonHeight),
                        buttonWidth,
                        buttonHeight,
                        buttonForeground,
                        buttonBackground);

        startTutorial.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                DataSave.clearData();
                game.getScreen().dispose();
                game.setScreen(new MenuScreen(game));
            }
        }));


        return world;

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
        world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
        return true;
    }
}
