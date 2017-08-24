package com.byrjamin.wickedwizard.screens.world;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 26/07/2017.
 */

public class DeathScreenWorld {


    private Viewport gameport;
    private TextureAtlas atlas;

    private MainGame game;

    private World world;


    private static final float buttonWidth = Measure.units(50f);
    private static final float buttonHeight = Measure.units(15f);

    public DeathScreenWorld(MainGame game, Viewport gameport){
        this.game = game;
        this.atlas  = game.assetManager.get(FileLocationStrings.spriteAtlas);
        this.gameport = gameport;
        createDeathScreenWorld();
    }



    public void createDeathScreenWorld(){

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

        FadeComponent fc = new FadeComponent(2.0f, false);
        fc.fadeIn = true;

        Entity restartEntity = createRestartTextEntity(world, fc);

        PositionComponent restartEntityPosition = restartEntity.getComponent(PositionComponent.class);
        CollisionBoundComponent restartEntityBound = restartEntity.getComponent(CollisionBoundComponent.class);

        Entity upperTextEntity = world.createEntity();
        upperTextEntity.edit().add(new PositionComponent(restartEntityPosition.getX(), restartEntityPosition.getY() + restartEntityBound.bound.getHeight() / 2));
        TextureFontComponent tfc = new TextureFontComponent(MenuStrings.Death.DEATH_FLAVOR_TEXT[MathUtils.random.nextInt(MenuStrings.Death.DEATH_FLAVOR_TEXT.length)]);
        upperTextEntity.edit().add(new CollisionBoundComponent(new Rectangle(gameport.getCamera().position.x
                ,gameport.getCamera().position.y - gameport.getWorldHeight() / 2 + 800, restartEntityBound.bound.getWidth(), restartEntityBound.bound.getHeight() / 2)));
        upperTextEntity.edit().add(tfc);
        upperTextEntity.edit().add(fc);

        Entity lowerTextEntity = world.createEntity();
        lowerTextEntity.edit().add(new PositionComponent(restartEntityPosition.getX(), restartEntityPosition.getY()));
        tfc = new TextureFontComponent(MenuStrings.Death.RESTART);
        lowerTextEntity.edit().add(new CollisionBoundComponent(new Rectangle(gameport.getCamera().position.x
                ,gameport.getCamera().position.y - gameport.getWorldHeight() / 2 + 800, restartEntityBound.bound.getWidth(), restartEntityBound.bound.getHeight() / 2)));
        lowerTextEntity.edit().add(tfc);
        lowerTextEntity.edit().add(fc);



        Entity blackScreen = world.createEntity();
        blackScreen.edit().add(new PositionComponent(gameport.getCamera().position.x - gameport.getWorldWidth() / 2,
                gameport.getCamera().position.y - gameport.getWorldHeight() / 2));
        TextureRegionComponent trc = new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), gameport.getCamera().viewportWidth, gameport.getCamera().viewportHeight, TextureRegionComponent.FOREGROUND_LAYER_NEAR);
        trc.color = new Color(Color.BLACK);
        trc.color.a = 0.5f;
        trc.layer = -1;

        blackScreen.edit().add(trc);
        blackScreen.edit().add(fc);

    }


    public Entity createRestartTextEntity(World world, FadeComponent fadeComponent){

        float camX = gameport.getCamera().position.x - gameport.getCamera().viewportWidth / 2;
        float camY = gameport.getCamera().position.y - gameport.getCamera().viewportHeight / 2;

        Entity restartEntity = world.createEntity();
        restartEntity.edit().add(new PositionComponent(camX + CenterMath.offsetX(gameport.getCamera().viewportWidth, Measure.units(50f))
                ,camY + CenterMath.offsetY(gameport.getCamera().viewportHeight, Measure.units(10f))));
        restartEntity.edit().add(new CollisionBoundComponent(new Rectangle(gameport.getCamera().position.x
                ,gameport.getCamera().position.y - gameport.getWorldHeight() / 2 + 800, buttonWidth, buttonHeight)));
        restartEntity.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new MenuScreen(game));
            }
        }));
        restartEntity.edit().add(fadeComponent);


        return restartEntity;


    }


    public void process(float delta){
        world.setDelta(delta < 0.030f ? delta : 0.030f);
        world.process();
    }


    public World getWorld() {
        return world;
    }
}
