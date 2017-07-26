package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 17/04/2017.
 */

public class ProximitySystem extends EntityProcessingSystem {

    ComponentMapper<PickUpComponent> im;

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ProximityTriggerAIComponent> ptam;

    public ProximitySystem() {
        super(Aspect.all(ProximityTriggerAIComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {
        CollisionBoundComponent playerCbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);

        CollisionBoundComponent cbc = cbm.get(e);
        ProximityTriggerAIComponent ptac = ptam.get(e);

        boolean canBeTriggered;

        if(ptac.onCameraTrigger){
            canBeTriggered = world.getSystem(CameraSystem.class).isOnCamera(cbc.bound);


        } else {
            canBeTriggered = overlapsHitbox(playerCbc.bound, ptac.proximityHitBoxes);
        }


        if(!ptac.triggered && canBeTriggered){
            ptac.task.performAction(world, e);
            ptac.triggered = true;
        } else if(!canBeTriggered && ptac.triggered){
            ptac.triggered = false;
            ptac.task.cleanUpAction(world, e);
        }
    }

    private boolean overlapsHitbox(Rectangle rectangle, Array<HitBox> hitBoxes){
        for(HitBox hb : hitBoxes){
            if(rectangle.overlaps(hb.hitbox)) return true;
        }
        return false;
    }

}