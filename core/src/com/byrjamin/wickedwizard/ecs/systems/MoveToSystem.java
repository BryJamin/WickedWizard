package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;

/**
 * Created by Home on 22/03/2017.
 */

public class MoveToSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<MoveToComponent> mtm;

    @SuppressWarnings("unchecked")
    public MoveToSystem() {
        super(Aspect.all(PositionComponent.class, MoveToComponent.class, VelocityComponent.class));
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        MoveToComponent mtc = mtm.get(e);

//        pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta);

        Float target = mtc.target_x;

        if(target != null){
            if (target - 20 <= pc.getX() && pc.getX() < target + 20) {
                vc.velocity.x = 0;
                mtc.target_x = null;
            } else if (pc.getX() >= target) {
                vc.velocity.x = -mtc.getSpeed();
            } else {
                vc.velocity.x = mtc.getSpeed();
            }
        }


    }

}

