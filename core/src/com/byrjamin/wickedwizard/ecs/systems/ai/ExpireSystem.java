package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;

/**
 * Created by Home on 13/04/2017.
 */

public class ExpireSystem extends EntityProcessingSystem {

    ComponentMapper<ExpireComponent> em;

    @SuppressWarnings("unchecked")
    public ExpireSystem() {
        super(Aspect.all(ExpireComponent.class));
    }

    @Override
    protected void process(Entity e) {
        ExpireComponent ec = em.get(e);
        if ((ec.expiryTime -= world.delta) <= 0) world.deleteEntity(e);
    }

}





