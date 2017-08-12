package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.texture.BlinkOnHitComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.object.LockComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;

/**
 * Created by Home on 13/03/2017.
 */

public class DoorSystem extends EntityProcessingSystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<DoorComponent> dm;
    ComponentMapper<LockComponent> lm;
    ComponentMapper<EnemyComponent> em;
    ComponentMapper<FriendlyComponent> fm;
    ComponentMapper<BlinkOnHitComponent> bm;

    @SuppressWarnings("unchecked")
    public DoorSystem() {
        super(Aspect.all(PlayerComponent.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(DoorComponent.class));
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

                            world.getSystem(RoomTransitionSystem.class).goFromSourceDoorToDestinationDoor(
                                    dm.get(doorEntity),
                                    doorEntryPercentage);
                        }
                    }
                }
            }



        }






    }
}
