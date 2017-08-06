package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by ae164 on 21/05/17.
 */

public class GroundCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public GroundCollisionSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, CollisionBoundComponent.class).exclude(BounceComponent.class, IntangibleComponent.class));
    }


    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        CollisionBoundComponent cbc = cbm.get(e);
        VelocityComponent vc = vm.get(e);
        PositionComponent pc = pm.get(e);


        if(cbc.getRecentCollisions().contains(Collider.Collision.BOTTOM, false)){
            if(vc.velocity.y <= 0) vc.velocity.y = 0;
        }

        if((cbc.getRecentCollisions().contains(Collider.Collision.TOP, true))) {
            vc.velocity.y = 0;
        }

        if(cbc.getRecentCollisions().contains(Collider.Collision.LEFT, true) || cbc.getRecentCollisions().contains(Collider.Collision.RIGHT, true)){
            vc.velocity.x = 0;
        }

    }





}
