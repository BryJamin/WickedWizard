package com.byrjamin.wickedwizard.screens.world;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.screens.MenuButton;
import com.byrjamin.wickedwizard.screens.MenuScreen;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 08/08/2017.
 *
 * Used to ask are you sure
 *
 */

public class AreYouSureWorld extends WorldContainer {


    private final String text;

    private final MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;
    private final MenuButton menuButton;

    private final float buttonWidth = Measure.units(20f);

    private boolean isActive = false;

    public AreYouSureWorld(MainGame game, Viewport viewport, String text){
        this.game = game;
        this.gameport = viewport;
        this.atlas = game.assetManager.get(FileLocationStrings.spriteAtlas);
        menuButton = new MenuButton(Assets.medium, atlas.findRegion(TextureStrings.BLOCK));
        this.text = text;
        isActive = true;
        createWorld();
    }


    public World createWorld() {

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

        Camera gamecam = gameport.getCamera();

        float camX = gamecam.position.x - gamecam.viewportWidth / 2 + MainGame.GAME_BORDER;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2 + MainGame.GAME_BORDER;


        Entity textEntity = world.createEntity();
        textEntity.edit().add(new PositionComponent(gameport.getCamera().position.x
                ,gameport.getCamera().position.y - gameport.getWorldHeight() / 2 + Measure.units(50f)));
        textEntity.edit().add(new TextureFontComponent(text));

        Entity blackScreen = world.createEntity();
        blackScreen.edit().add(new PositionComponent(gameport.getCamera().position.x - gameport.getWorldWidth() / 2,
                gameport.getCamera().position.y - gameport.getWorldHeight() / 2));
        blackScreen.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK),
                gameport.getCamera().viewportWidth,
                gameport.getCamera().viewportHeight,
                TextureRegionComponent.BACKGROUND_LAYER_FAR,
                new Color(Color.BLACK)));



        Entity yes = menuButton.createButton(world, MenuStrings.YES, camX + Measure.units(20f)
                ,camY + Measure.units(20f),buttonWidth, Measure.units(10f), new Color(Color.WHITE), new Color(Color.BLACK));
        yes.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                game.getScreen().dispose();
                game.setScreen(new MenuScreen(game));
            }
        }));

        Entity resume = menuButton.createButton(world, MenuStrings.NO, camX + Measure.units(60f)
                ,camY + Measure.units(20f), buttonWidth, Measure.units(10f), new Color(Color.WHITE), new Color(Color.BLACK));
        resume.edit().add(new ActionOnTouchComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                PlayScreen playScreen = (PlayScreen) game.getScreen();
                playScreen.escapeAreYouSure();
            }
        }));




        return world;

    }











    @Override
    public World getWorld() {
        return world;
    }
}
