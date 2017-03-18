package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;

/**
 * Created by Home on 04/03/2017.
 */
public class MovementSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;

    @SuppressWarnings("unchecked")
    public MovementSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class));
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);

        pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

    }

}
