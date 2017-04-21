package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;

/**
 * Created by Home on 07/03/2017.
 */
public class StateSystem extends EntityProcessingSystem {

    ComponentMapper<AnimationStateComponent> sm;

    @SuppressWarnings("unchecked")
    public StateSystem() {
        super(Aspect.all(AnimationStateComponent.class));
    }

    @Override
    protected void process(Entity e) {
        sm.get(e).stateTime += world.delta;
    }

}
