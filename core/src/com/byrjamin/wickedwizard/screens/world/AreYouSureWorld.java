package com.byrjamin.wickedwizard.screens.world;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.graphics.Camera;
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
import com.byrjamin.wickedwizard.utils.AbstractGestureDectector;
import com.byrjamin.wickedwizard.utils.CenterMath;
import com.byrjamin.wickedwizard.utils.GameDelta;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 08/08/2017.
 *
 * Used to ask are you sure
 *
 */

public class AreYouSureWorld extends AbstractGestureDectector implements WorldContainer {


    private final String text;

    private final MainGame game;
    private final Viewport gameport;
    private final TextureAtlas atlas;

    private final float buttonWidth = Measure.units(20f);

    private static final float buttonGap = Measure.units(60f);

    private boolean isActive = false;
    private World world;


    private Action yesAction;
    private Action noAction;


    public AreYouSureWorld(final MainGame game, Viewport viewport, String text, Action yesAction, Action noAction){
        this.game = game;
        this.gameport = viewport;
        this.atlas = game.assetManager.get(FileLocationStrings.spriteAtlas);

        this.text = text;
        isActive = true;

        this.yesAction = yesAction;
        this.noAction = noAction;


        createWorld();
    }


    public void  createWorld() {

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

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;


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


        float totalWidth = buttonWidth + buttonGap;

        MenuButton.MenuButtonBuilder menuButtonBuilder = new MenuButton.MenuButtonBuilder(Assets.medium, atlas.findRegion(TextureStrings.BLOCK))
                .action(yesAction)
                .width(buttonWidth)
                .height(Measure.units(10f))
                .foregroundColor(new Color(Color.WHITE))
                .backgroundColor(new Color(Color.BLACK));


        Entity yes = menuButtonBuilder.build().createButton(world, MenuStrings.YES, camX + CenterMath.offsetX(gameport.getCamera().viewportWidth, totalWidth)
                ,camY + Measure.units(20f));
        yes.edit().add(new ActionOnTouchComponent(yesAction));

        menuButtonBuilder.action(noAction);
        Entity resume = menuButtonBuilder.build().createButton(world, MenuStrings.NO, camX + CenterMath.offsetX(gameport.getCamera().viewportWidth, totalWidth) + buttonGap
                ,camY + Measure.units(20f)
        );
        resume.edit().add(new ActionOnTouchComponent(noAction));


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
        return world.getSystem(ActionOnTouchSystem.class).touch(touchInput.x, touchInput.y);
    }
}
