package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ItemComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;

/**
 * Created by Home on 09/04/2017.
 */

public class PickUpSystem extends EntityProcessingSystem {

    ComponentMapper<ItemComponent> im;
    ComponentMapper<CollisionBoundComponent> cbm;

    public PickUpSystem() {
        super(Aspect.all(PlayerComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().
                get(Aspect.all(ItemComponent.class, CollisionBoundComponent.class));
        IntBag itemsIds = subscription.getEntities();


        CollisionBoundComponent player = cbm.get(e);
        CollisionBoundComponent item;

        for(int i = 0; i < itemsIds.size(); i++){


            int itemEntity = itemsIds.get(i);
            item = cbm.get(itemEntity);

            if(player.bound.overlaps(item.bound)){
                ItemComponent ic = im.get(itemEntity);
                if(ic.getItem().applyEffect(world, e)) {;
                    world.delete(itemEntity);
                }
            }

        }


    }
}
