package com.byrjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 26/07/2017.
 */

public class PortalFactory extends AbstractFactory {


    private static final float mapPortalSize = Measure.units(10f);
    private static final float levelPortalSize = Measure.units(20f);


    public PortalFactory(AssetManager assetManager) {
        super(assetManager);
    }



    public ComponentBag mapPortal (float x, float y, BossTeleporterComponent btc){

        ComponentBag bag = portal(x,y, mapPortalSize, mapPortalSize, new Task() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(MapTeleportationSystem.class).goFromSourceToDestination(e.getComponent(BossTeleporterComponent.class));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });

        bag.add(btc);

        return bag;

    }


    private ComponentBag portal(float x, float y, float width, float height, Task task){

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x, y));


        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));

        AnimationStateComponent sc = new AnimationStateComponent(AnimationStateComponent.DEFAULT);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.1f, atlas.findRegions(TextureStrings.TELEPORTER), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.TELEPORTER),
                width,
                height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new ProximityTriggerAIComponent(task, new HitBox(new Rectangle(x,y, width, height))));


        return bag;

    }


    public ComponentBag levelPortal(float x, float y){

        return portal(x,y, levelPortalSize, levelPortalSize, new Task() {
            @Override
            public void performAction(World world, Entity e) {

                world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(MapTeleportationSystem.class).createNewLevel();
                    }
                });

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }

        });

    }



}
