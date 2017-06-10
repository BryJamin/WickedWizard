package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;

/**
 * Created by Home on 27/05/2017.
 */

public class ActionAfterTimeSystem extends EntityProcessingSystem {

    ComponentMapper<ActionAfterTimeComponent> aatm;

    @SuppressWarnings("unchecked")
    public ActionAfterTimeSystem() {
        super(Aspect.all(ActionAfterTimeComponent.class));
    }

    @Override
    protected void process(Entity e) {
        ActionAfterTimeComponent aatc = aatm.get(e);
        if ((aatc.timeUntilAction -= world.delta) <= 0) {
            aatc.task.performAction(world, e);

            if(aatc.repeat){
                aatc.timeUntilAction = aatc.resetTime;
            } else {
                e.edit().remove(ActionAfterTimeComponent.class);
            }

        }
    }

}
