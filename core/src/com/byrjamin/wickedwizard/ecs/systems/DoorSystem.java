package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.FriendlyComponent;
import com.byrjamin.wickedwizard.ecs.components.object.LockComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;

/**
 * Created by Home on 13/03/2017.
 */

public class DoorSystem extends EntityProcessingSystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ActiveOnTouchComponent> aotm;
    ComponentMapper<DoorComponent> dm;
    ComponentMapper<LockComponent> lm;
    ComponentMapper<EnemyComponent> em;
    ComponentMapper<FriendlyComponent> fm;
    ComponentMapper<BlinkComponent> bm;

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

            if(aotm.has(doorEntity)) {
                if(!aotm.get(doorEntity).isActive) {
                    continue;
                }
            }

            if(cbm.has(doorEntity)) {

                CollisionBoundComponent cbc = cbm.get(doorEntity);
                BoundsDrawer.drawBounds(world.getSystem(com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem.class).batch, cbc.bound);
                if(cbc.bound.overlaps(cbm.get(e).bound)) {
                   // System.out.println("INSIDE THA DOOR");

                    if(lm.has(doorEntity)) {

                        System.out.println(lm.get(doorEntity).isLocked() + "Running");
                        if(!lm.get(doorEntity).isLocked()) {

                            float doorEntryPercentage = ((cbm.get(e).bound.y - cbc.bound.getY()) /
                                    (cbc.bound.getHeight()));

                            world.getSystem(RoomTransitionSystem.class).goFromTo(
                                    dm.get(doorEntity).currentCoords,
                                    dm.get(doorEntity).leaveCoords,
                                    doorEntryPercentage);
                        }
                    }
                }
            }



        }






    }
}
