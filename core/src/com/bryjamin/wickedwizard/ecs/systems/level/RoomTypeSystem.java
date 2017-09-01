package com.bryjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ArenaLockComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.bryjamin.wickedwizard.ecs.systems.LockSystem;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.utils.BagToEntity;

/**
 * Created by Home on 25/03/2017.
 */

public class RoomTypeSystem extends BaseSystem {

    @Override
    protected void processSystem() {

        Arena current = world.getSystem(RoomTransitionSystem.class).getCurrentArena();


        switch(current.arenaType){
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
