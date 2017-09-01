package com.bryjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 30/03/2017.
 */

public class JumpSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.JumpComponent> jm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public JumpSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.movement.JumpComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {

        com.bryjamin.wickedwizard.ecs.components.movement.JumpComponent jc = jm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);

        if(cbc.getRecentCollisions().contains(Collider.Collision.BOTTOM, false)){
            jc.jumps = jc.maxJumps;
        }
    }

}
