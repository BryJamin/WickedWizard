package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.PickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;

/**
 * Created by Home on 17/04/2017.
 */

public class ProximitySystem extends EntityProcessingSystem {

    ComponentMapper<PickUpComponent> im;
    ComponentMapper<ProximityTriggerAIComponent> ptam;

    public ProximitySystem() {
        super(Aspect.all(ProximityTriggerAIComponent.class));
    }

    @Override
    protected void process(Entity e) {
        CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
        ProximityTriggerAIComponent ptac = ptam.get(e);

        if(!ptac.triggered && cbc.bound.overlaps(ptac.bound)){
            ptac.action.performAction(world, e);
            ptac.triggered = true;
        } else if(cbc.bound.overlaps(ptac.bound) && ptac.triggered){
            ptac.triggered = false;
            ptac.action.cleanUpAction(world, e);
        }
    }
}