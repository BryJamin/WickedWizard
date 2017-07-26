package com.byrjamin.wickedwizard.screens.world;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Locale;

/**
 * Created by ae164 on 19/05/17.
 */

public class PauseWorld {


    private World world;

    private SpriteBatch batch;
    private AssetManager manager;
    private TextureAtlas atlas;
    private Viewport gameport;

    private MenuButton menuButton;

    private Entity returntoMainMenu;


    public PauseWorld(SpriteBatch batch, AssetManager manager, Viewport gameport){

        this.batch = batch;
        this.manager = manager;
        this.atlas = manager.get(FileLocationStrings.spriteAtlas);
        this.gameport = gameport;

        menuButton = new MenuButton(Assets.medium, atlas.findRegion(TextureStrings.BLOCK));


    }

    public World startWorld(StatComponent playerStats){

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,
                        new AnimationSystem(),
                        new ActionOnTouchSystem(),
                        //new FindPlayerSystem(player),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new RenderingSystem(batch, manager, gameport),
                        new BoundsDrawingSystem()
                )
                .build();

        world = new World(config);

        Camera gamecam = gameport.getCamera();

        float camX = gamecam.position.x - gamecam.viewportWidth / 2 + MainGame.GAME_BORDER;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2 + MainGame.GAME_BORDER;

        returntoMainMenu = menuButton.createButton(world, "Main Menu", camX + Measure.units(50f)
                ,camY, Measure.units(40f), Measure.units(10f), new Color(Color.BLACK), new Color(Color.WHITE));


        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(camX,camY));
        e.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                CenterMath.offsetX(gamecam.viewportWidth, gamecam.viewportWidth * 2),
                CenterMath.offsetX(gamecam.viewportHeight, gamecam.viewportHeight * 2),
                gamecam.viewportWidth * 2, gamecam.viewportHeight * 2, TextureRegionComponent.BACKGROUND_LAYER_FAR, new Color(0,0,0,0.93f)));



        Entity statText = world.createEntity();
        statText.edit().add(new PositionComponent(camX + Measure.units(10f), camY + Measure.units(55f)));
        statText.edit().add(new TextureFontComponent(Assets.medium, "Stats", Measure.units(10f), Measure.units(10f), TextureRegionComponent.BACKGROUND_LAYER_NEAR));

        String[] stats = new String[]
                {
                        String.format(Locale.getDefault(), "Dmg %.2f", playerStats.damage),
                        String.format(Locale.getDefault(), "Fir %.2f", playerStats.fireRate),
                        String.format(Locale.getDefault(), "Rng %.2f", playerStats.range),
                        String.format(Locale.getDefault(), "Acc %.2f", playerStats.accuracy),
                        String.format(Locale.getDefault(), "Lck %.2f", playerStats.luck),
                        String.format(Locale.getDefault(), "Crt %.2f", playerStats.crit) + "%",
                        String.format(Locale.getDefault(), "Spd %.0f", playerStats.speed * 100) + "%",
                };


        for(int i = 0; i < stats.length; i++){
            statsText(world, stats[i], camX + Measure.units(5), camY + Measure.units(50f - (5 * i)));
        }


        Entity itemText = world.createEntity();
        itemText.edit().add(new PositionComponent(camX + Measure.units(30f), camY + Measure.units(55f)));
        itemText.edit().add(new TextureFontComponent(Assets.medium, "Items", Measure.units(15f), Measure.units(10f), TextureRegionComponent.BACKGROUND_LAYER_NEAR));


        return world;
    }

    public void statsText(World world, String text, float x, float y){
        Entity damage = world.createEntity();
        damage.edit().add(new PositionComponent(x, y));
        damage.edit().add(new TextureFontComponent(Assets.medium, text, Measure.units(20f), Measure.units(10f), TextureRegionComponent.BACKGROUND_LAYER_NEAR));
    }


    public World getWorld() {
        return world;
    }

    public Entity getReturntoMainMenu() {
        return returntoMainMenu;
    }


    public boolean isReturnToMainMenuTouched(float x, float y){
        return returntoMainMenu.getComponent(CollisionBoundComponent.class).bound.contains(x,y);
    }

    public void endWorld(){
        //world.
    }


    public void process(){
        world.process();
    }







    }
