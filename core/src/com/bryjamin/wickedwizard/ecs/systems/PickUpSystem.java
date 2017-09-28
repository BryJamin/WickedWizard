package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;

/**
 * Created by Home on 09/04/2017.
 */

public class PickUpSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent> im;
    ComponentMapper<CollisionBoundComponent> cbm;

    public PickUpSystem() {
        super(Aspect.all(PlayerComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().
                get(Aspect.all(com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent.class, CollisionBoundComponent.class));
        IntBag itemsIds = subscription.getEntities();


        CollisionBoundComponent player = cbm.get(e);
        CollisionBoundComponent item;

        for(int i = 0; i < itemsIds.size(); i++){


            int itemEntity = itemsIds.get(i);
            item = cbm.get(itemEntity);

            if(player.bound.overlaps(item.bound)){
                PickUpComponent ic = im.get(itemEntity);
                if(ic.getPickUp().applyEffect(world, e)) {;
                    world.getSystem(OnDeathSystem.class).kill(world.getEntity(itemEntity));
                }
            }

        }


    }



}
