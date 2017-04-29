package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 09/04/2017.
 */

public class PickUpSystem extends EntityProcessingSystem {

    ComponentMapper<PickUpComponent> im;
    ComponentMapper<CollisionBoundComponent> cbm;

    public PickUpSystem() {
        super(Aspect.all(PlayerComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().
                get(Aspect.all(PickUpComponent.class, CollisionBoundComponent.class));
        IntBag itemsIds = subscription.getEntities();


        CollisionBoundComponent player = cbm.get(e);
        CollisionBoundComponent item;

        for(int i = 0; i < itemsIds.size(); i++){


            int itemEntity = itemsIds.get(i);
            item = cbm.get(itemEntity);

            if(player.bound.overlaps(item.bound)){
                PickUpComponent ic = im.get(itemEntity);
                if(ic.getPickUp().applyEffect(world, e)) {;
                    world.delete(itemEntity);
                }
            }

        }


    }


    public void itemOverHead(Entity player, Pair<String, Integer> textureRegionName){

        CollisionBoundComponent pBound = player.getComponent(CollisionBoundComponent.class);

        Entity itemHoverAffect = world.createEntity();
        itemHoverAffect.edit().add(new PositionComponent());
        itemHoverAffect.edit().add(new FollowPositionComponent(player.getComponent(PositionComponent.class).position,
                0, pBound.bound.getHeight() + pBound.bound.getHeight() / 4));
        itemHoverAffect.edit().add(new TextureRegionComponent(world.getSystem(RenderingSystem.class).atlas.findRegion(textureRegionName.getLeft(), textureRegionName.getRight()),
                Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR));
        itemHoverAffect.edit().add(new ExpireComponent(0.9f));


    }
}
