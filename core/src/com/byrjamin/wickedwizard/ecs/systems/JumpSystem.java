package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.byrjamin.wickedwizard.helper.collider.Collider;

/**
 * Created by Home on 30/03/2017.
 */

public class JumpSystem extends EntityProcessingSystem {

    ComponentMapper<JumpComponent> jm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public JumpSystem() {
        super(Aspect.all(JumpComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void process(Entity e) {

        JumpComponent jc = jm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);

        if(cbc.getRecentCollisions().contains(Collider.Collision.TOP, false)){
            jc.jumps = jc.maxJumps;
        }
    }

}
