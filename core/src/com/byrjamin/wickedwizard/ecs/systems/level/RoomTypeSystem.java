package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ArenaLockComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.systems.LockSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.utils.BagToEntity;

/**
 * Created by Home on 25/03/2017.
 */

public class RoomTypeSystem extends BaseSystem {

    @Override
    protected void processSystem() {

        Arena current = world.getSystem(com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem.class).getCurrentArena();


        switch(current.roomType){
            case TRAP:
            case BOSS:
                if(world.getAspectSubscriptionManager().get(Aspect.one(EnemyComponent.class, ArenaLockComponent.class).exclude(BulletComponent.class)).getEntities().size() <= 0){

                    if(!(current.getWaves().size <= 0)) {
                        BagToEntity.bagsToEntities(world, current.getWaves().removeFirst());
                        break;
                    }

                    world.getSystem(LockSystem.class).unlockDoors();
                    world.getSystem(InCombatSystem.class).leaveCombat();

                } else {
                    world.getSystem(LockSystem.class).lockDoors();
                    world.getSystem(InCombatSystem.class).goInCombat();
                }
                break;
            case ITEM:
                break;
            case SHOP:
                break;
            case NORMAL:
                break;
        }

    }

}
