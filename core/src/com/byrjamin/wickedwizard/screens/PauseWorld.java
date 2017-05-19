package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by ae164 on 19/05/17.
 */

public class PauseWorld {


    private World world;

    private SpriteBatch batch;
    private AssetManager manager;
    private OrthographicCamera gamecam;

    private MenuButton menuButton;

    private Entity returntoMainMenu;

    public PauseWorld(SpriteBatch batch, AssetManager manager, OrthographicCamera gamecam){

        this.batch = batch;
        this.manager = manager;
        this.gamecam = gamecam;

        menuButton = new MenuButton(Assets.medium);


    }

    public World startWorld(){

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        //new FindPlayerSystem(player),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(batch, manager, gamecam),
                        new BoundsDrawingSystem()
                )
                .build();

        world = new World(config);

        System.out.println(gamecam.viewportWidth / 2);

        returntoMainMenu = menuButton.createButton(world, "Return to Main Menu", gamecam.viewportWidth / 2 + Measure.units(20f)
                ,gamecam.viewportHeight / 2 + Measure.units(0f), Measure.units(40f), Measure.units(10f), new Color(Color.BLACK), new Color(Color.WHITE));

        System.out.println("?????????????????");



        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(0,0));
        e.edit().add(new ShapeComponent(0,0, gamecam.viewportWidth, gamecam.viewportHeight, TextureRegionComponent.BACKGROUND_LAYER_FAR, new Color(0,0,0,0.5f)));


        return world;
    }



    public void endWorld(){
        //world.
    }







    }
