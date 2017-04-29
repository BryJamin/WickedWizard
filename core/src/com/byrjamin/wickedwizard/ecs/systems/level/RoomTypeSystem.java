package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.systems.ChangeLevelSystem;
import com.byrjamin.wickedwizard.ecs.systems.LockSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;

/**
 * Created by Home on 25/03/2017.
 */

public class RoomTypeSystem extends BaseSystem {


    public boolean nextLevelDoor = false;

    @Override
    protected void processSystem() {

        Arena current = world.getSystem(com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem.class).getCurrentArena();


        switch(current.roomType){
            case TRAP:
            case BOSS:
                if(world.getAspectSubscriptionManager().get(Aspect.all(EnemyComponent.class).exclude(BulletComponent.class)).getEntities().size() <= 0){
                    world.getSystem(LockSystem.class).unlockDoors();


                    if(current.roomType == Arena.RoomType.BOSS) {



                        if (!nextLevelDoor && world.getSystem(ChangeLevelSystem.class).getLevel() != ChangeLevelSystem.Level.FREEDOMRUN) {
                            Entity e = world.createEntity();
                            for (Component c : new DeathFactory(world.getSystem(RenderingSystem.class).getAssetManager()).worldPortal(current.getWidth() / 2, current.getHeight() / 2)) {
                                e.edit().add(c);
                            }
                            nextLevelDoor = true;
                        }
                    }

                } else {
                    world.getSystem(LockSystem.class).lockDoors();
                }
                break;
        }

    }

}
