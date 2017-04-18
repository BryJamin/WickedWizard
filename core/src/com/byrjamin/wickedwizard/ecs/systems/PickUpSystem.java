package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;

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
}
