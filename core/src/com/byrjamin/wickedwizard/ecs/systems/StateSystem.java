package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.StateComponent;

/**
 * Created by Home on 07/03/2017.
 */
public class StateSystem extends EntityProcessingSystem {

    ComponentMapper<StateComponent> sm;

    @SuppressWarnings("unchecked")
    public StateSystem() {
        super(Aspect.all(StateComponent.class));
    }

    @Override
    protected void process(Entity e) {
        sm.get(e).stateTime += world.delta;
    }

}
