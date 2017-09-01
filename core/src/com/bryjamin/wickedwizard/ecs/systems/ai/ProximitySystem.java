package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem;

/**
 * Created by BB on 17/04/2017.
 *
 * Iterates for entities with a ProximityTriggerAIComponent
 *
 * If the triggering conditions are met the the trigger action is performed
 * When the triggering conditions are no longer let the clean up action is performed
 *
 *
 */

public class ProximitySystem extends EntityProcessingSystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ProximityTriggerAIComponent> ptam;

    public ProximitySystem() {
        super(Aspect.all(ProximityTriggerAIComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {

        CollisionBoundComponent playerCbc = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
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

    /**
     * Checks to see if the given rectangle overlaps any of hitboxes stored within the
     * ProximityTriggerAi Component
     * @param rectangle - Rectangle of the player
     * @param hitBoxes - Hitboxes stored within the ProximityTriggerAi Component
     * @return - True is there is an overlap, False otherwise
     */
    private boolean overlapsHitbox(Rectangle rectangle, Array<com.bryjamin.wickedwizard.utils.collider.HitBox> hitBoxes){
        for(com.bryjamin.wickedwizard.utils.collider.HitBox hb : hitBoxes){
            if(rectangle.overlaps(hb.hitbox)) return true;
        }
        return false;
    }

}