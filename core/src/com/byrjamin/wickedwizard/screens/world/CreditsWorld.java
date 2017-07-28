package com.byrjamin.wickedwizard.screens.world;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpiryRangeComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 26/07/2017.
 */

public class CreditsWorld {


    private static final float skipWidth = Measure.units(30f);
    private static final float skipHeight = Measure.units(7.5f);
    private static final float skipOffsetY = Measure.units(5);


    private static final float creditStartY = Measure.units(0);

    private static final float creditsSpeed = Measure.units(10f);

    private Viewport gameport;
    private TextureAtlas atlas;

    private MainGame game;

    private World world;

    private Entity mainMenuTrigger;

    private static final String skipText = "Tap Here To Skip";


    public CreditsWorld(MainGame game, Viewport gameport){
        this.game = game;
        this.atlas  = game.manager.get(FileLocationStrings.spriteAtlas);
        this.gameport = gameport;
        createCreditsWorld();
    }




    public void createCreditsWorld(){

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
                        new RenderingSystem(game.batch, game.manager, gameport)
                        //new BoundsDrawingSystem(true)
                )
                .build();


        world = new World(config);



        Entity mainMenuTrigger = new MenuButton(Assets.small, atlas.findRegion(TextureStrings.BLOCK)).createButtonWithAction(world, skipText,
                Measure.units(65f),
                Measure.units(10f),
                skipWidth,
                skipHeight,
                new Color(Color.WHITE),
                new Color(Color.BLACK),
                new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        game.getScreen().dispose();
                        game.setScreen(new MenuScreen(game));
                    }
                });


        Entity creditCrawl = world.createEntity();
        creditCrawl.edit().add(new PositionComponent(gameport.getCamera().position.x - MainGame.GAME_WIDTH / 2,
                gameport.getCamera().position.y));
        creditCrawl.edit().add(new VelocityComponent(0, creditsSpeed));
        creditCrawl.edit().add(new TextureFontComponent(Assets.small, "Thanks for Playing",
                MainGame.GAME_WIDTH,
                TextureRegionComponent.BACKGROUND_LAYER_FAR,
                new Color(Color.WHITE)));

        Entity creditsController = world.createEntity();
        creditsController.edit().add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }
        }));

/*
        world.createEntity();

        mainMenuTrigger.edit().add(new PositionComponent(gameport.getCamera().position.x,
                gameport.getCamera().position.y - Measure.units(20f)));

        mainMenuTrigger.edit().add(new TextureFontComponent(Assets.small, skipText,0, skipOffsetY, skipWidth, TextureRegionComponent.FOREGROUND_LAYER_MIDDLE, new Color(Color.WHITE)));

        mainMenuTrigger.edit().add(new CollisionBoundComponent(new Rectangle(mainMenuTrigger.getComponent(PositionComponent.class).getX(),
                mainMenuTrigger.getComponent(PositionComponent.class).getY(), skipWidth, skipHeight)));

        mainMenuTrigger.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new MenuScreen(game));
            }
        }));
*/








    };


    private ComponentBag createCreditsBag(String text){


        ComponentBag bag = new ComponentBag();

        PositionComponent pc = new PositionComponent(gameport.getCamera().position.x - MainGame.GAME_WIDTH / 2,
                -Measure.units(5f));

        bag.add(pc);
        //bag.add(new VelocityComponent(0, creditsSpeed));
        bag.add(new TextureFontComponent(Assets.small, text,
                MainGame.GAME_WIDTH,
                TextureRegionComponent.BACKGROUND_LAYER_FAR,
                new Color(Color.WHITE)));

        bag.add(new ExpiryRangeComponent(pc.position, Measure.units(150f)));

        return bag;

    }

    private Pair<ComponentBag, Float> createCreditsPart (ComponentBag bag, float distanceTravelledTillNextInLine){
        return new Pair<ComponentBag, Float>(bag, distanceTravelledTillNextInLine);
    }



    private class CreditRoll extends ConditionalActionComponent {








    }


    public void process(float delta){
        world.setDelta(delta < 0.030f ? delta : 0.030f);
        world.process();
    }

    public World getWorld() {
        return world;
    }
}
