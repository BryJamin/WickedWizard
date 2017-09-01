package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.object.LockComponent;

/**
 * Created by Home on 13/03/2017.
 */

public class DoorSystem extends EntityProcessingSystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> dm;
    ComponentMapper<LockComponent> lm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent> em;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent> fm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent> bm;

    @SuppressWarnings("unchecked")
    public DoorSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent.class));
        IntBag entityIds = subscription.getEntities();


        for(int i = 0; i < entityIds.size(); i++){
            int doorEntity = entityIds.get(i);

            if(dm.get(doorEntity).ignore) continue;

            if(cbm.has(doorEntity)) {

                CollisionBoundComponent cbc = cbm.get(doorEntity);
                if(cbc.bound.overlaps(cbm.get(e).bound)) {

                    if(lm.has(doorEntity)) {
                        if(!lm.get(doorEntity).isLocked()) {

                            float doorEntryPercentage = ((cbm.get(e).bound.y - cbc.bound.getY()) /
                                    (cbc.bound.getHeight()));

                            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem.class).goFromSourceDoorToDestinationDoor(
                                    dm.get(doorEntity),
                                    doorEntryPercentage);
                        }
                    }
                }
            }



        }






    }
}
