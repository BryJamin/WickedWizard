package com.bryjamin.wickedwizard.screens.world.play;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.ActionAfterTimeSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.FollowCameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AfterUIRenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.AnimationSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.BoundsDrawingSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.ActionOnTouchSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.GameSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.bryjamin.wickedwizard.factories.TutorialTipsFactory;
import com.bryjamin.wickedwizard.screens.world.WorldContainer;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.GameDelta;

/**
 * Created by BB on 26/09/2017.
 */

public class TipsMessageWorld implements WorldContainer {

    private SpriteBatch batch;
    private MainGame game;
    private AssetManager manager;
    private TextureAtlas atlas;
    private Viewport gameport;
    private Camera gamecam;

    private TutorialTipsFactory tutorialTipsFactory;

    private OrderedMap<String, Bag<ComponentBag>> tipsMap = new OrderedMap<String, Bag<ComponentBag>>();




    private World world;

    public TipsMessageWorld(MainGame game, Viewport gameport) {

        this.game = game;
        this.batch = game.batch;
        this.manager = game.assetManager;
        this.atlas = manager.get(FileLocationStrings.spriteAtlas);
        this.gameport = gameport;
        this.gamecam = gameport.getCamera();
        this.tutorialTipsFactory = new TutorialTipsFactory(game.assetManager);
        createWorld();

        this.tipsMap.put(MenuStrings.Tutorial.CONTROLLER_ID, tutorialTipsFactory.controllerTips(new Bag<ComponentBag>(), gamecam));
        this.tipsMap.put(MenuStrings.Tutorial.DOUBLE_TAP_ID, tutorialTipsFactory.cancelHoverTips(new Bag<ComponentBag>(), gamecam));
        this.tipsMap.put(MenuStrings.Tutorial.AIMING_ID, tutorialTipsFactory.aimingTips(new Bag<ComponentBag>(), gamecam));
        this.tipsMap.put(MenuStrings.Tutorial.TWO_THUMBS_ID, tutorialTipsFactory.twoThumbsTips(new Bag<ComponentBag>(), gamecam));
        this.tipsMap.put(MenuStrings.Tutorial.UI_ID, tutorialTipsFactory.uiTips(new Bag<ComponentBag>(), gamecam));
        this.tipsMap.put(MenuStrings.Tutorial.AUTO_FIRE_ID, tutorialTipsFactory.autoFire(new Bag<ComponentBag>(), gamecam));

    }



    public boolean createTip(String id){
        if(tipsMap.containsKey(id)){
            Bag<ComponentBag> bags = tipsMap.get(id);
            BagToEntity.bagsToEntities(world, bags);
            return true;
        }
        return false;
    }





    @Override
    public void createWorld() {

        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem()
                )
                .with(WorldConfigurationBuilder.Priority.HIGH,

                        new AnimationSystem(),
                        new ActionAfterTimeSystem(),
                        new OnDeathSystem(),
                        new FindChildSystem(),
                        new ActionOnTouchSystem(),
                        new FollowPositionSystem(),
                        new FadeSystem())
                .with(WorldConfigurationBuilder.Priority.LOW,
                        new FollowCameraSystem(gamecam),
                        new RenderingSystem(batch, manager, gameport),
                        new AfterUIRenderingSystem(game, gameport),
                        new BoundsDrawingSystem(),
                        new GameSystem(game)
                )
                .build();

        world = new World(config);

    }

    @Override
    public void process(float delta) {
        GameDelta.delta(world, delta);
    }

    @Override
    public World getWorld() {
        return world;
    }


}

