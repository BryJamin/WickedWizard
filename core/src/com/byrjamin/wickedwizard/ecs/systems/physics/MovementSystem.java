package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 04/03/2017.
 */
public class MovementSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public MovementSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class));
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);

        pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);


        if(cbm.has(e)) {
            CollisionBoundComponent cbc = cbm.get(e);
            cbc.bound.x = pc.getX();
            cbc.bound.y = pc.getY();
        }

        if(vc.velocity.x < 0)
            com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.LEFT, DirectionalComponent.PRIORITY.LOW);
        else if(vc.velocity.x > 0)
            com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.RIGHT, DirectionalComponent.PRIORITY.LOW);



    }

}