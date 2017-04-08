package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.factories.arenas.Arena;

/**
 * Created by Home on 25/03/2017.
 */

public class RoomTypeSystem extends BaseSystem {

    @Override
    protected void processSystem() {

        Arena current = world.getSystem(RoomTransitionSystem.class).getCurrentArena();


        switch(current.roomType){
            case BATTLE:
            case BOSS:
                if(world.getAspectSubscriptionManager().get(Aspect.all(EnemyComponent.class).exclude(BulletComponent.class)).getEntities().size() <= 0){
                    world.getSystem(LockSystem.class).unlockDoors();
                } else {
                    world.getSystem(LockSystem.class).lockDoors();
                }
                break;
        }

    }

}
