package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;

/**
 * Created by BB on 27/05/2017.
 *
 * Performs Action after a period of time for all entities with an ActionAfterTimeComponent.
 *
 * Resets and repeats action if the repeat boolean is set.
 *
 * The entity is removed otherwise
 *
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
            aatc.action.performAction(world, e);

            if(aatc.repeat){
                aatc.timeUntilAction = aatc.resetTime;
            } else {
                e.edit().remove(aatc);
            }

        }
    }

}
