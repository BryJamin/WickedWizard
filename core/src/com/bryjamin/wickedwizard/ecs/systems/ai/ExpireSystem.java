package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

/**
 * Created by BB on 13/04/2017.
 *
 * Iterates through all entities with an Expire Component
 *
 * If the entity has expired the OnDeathSystem 'kill' method is used on the entity
 *
 */

public class ExpireSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent> em;

    @SuppressWarnings("unchecked")
    public ExpireSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent.class));
    }

    @Override
    protected void process(Entity e) {
        com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent ec = em.get(e);
        if ((ec.expiryTime -= world.delta) <= 0) world.getSystem(OnDeathSystem.class).kill(e);
    }

}





