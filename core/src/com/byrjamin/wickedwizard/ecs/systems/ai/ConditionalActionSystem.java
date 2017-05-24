package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;

/**
 * Created by ae164 on 20/05/17.
 */

public class ConditionalActionSystem extends EntityProcessingSystem {

    ComponentMapper<HealthComponent> hm;
    ComponentMapper<ConditionalActionComponent> cac;
    ComponentMapper<BlinkComponent> bm;

    @SuppressWarnings("unchecked")
    public ConditionalActionSystem() {
        super(Aspect.all(ConditionalActionComponent.class));
    }


    @Override
    protected void process(Entity e) {
        if(cac.get(e).condition.condition(world, e)){
            cac.get(e).action.performAction(world, e);
        }
    }
}
