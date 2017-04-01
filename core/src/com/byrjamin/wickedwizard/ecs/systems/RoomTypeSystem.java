package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.GrappleableComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.RoomTypeComponent;
import com.byrjamin.wickedwizard.ecs.components.WallComponent;

/**
 * Created by Home on 25/03/2017.
 */

public class RoomTypeSystem extends EntitySystem {

    ComponentMapper<RoomTypeComponent> rtm;


    @SuppressWarnings("unchecked")
    public RoomTypeSystem() {
        super(Aspect.all(RoomTypeComponent.class));
    }

    @Override
    protected void processSystem() {

        Entity e = this.getEntities().safeGet(0);

        switch(rtm.get(e).getType()){
            case BATTLE:
                if(world.getAspectSubscriptionManager().get(Aspect.all(EnemyComponent.class).exclude(BulletComponent.class)).getEntities().size() <= 0){
                    world.getSystem(LockSystem.class).unlockDoors();
                } else {
                    world.getSystem(LockSystem.class).lockDoors();
                }
                break;
            case TUTORIAL:
                world.getSystem(TutorialSystem.class).startTutorial();;
                break;
        }

    }

}
