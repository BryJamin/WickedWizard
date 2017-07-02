package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 11/03/2017.
 */
public class BounceCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<PlayerComponent> playerm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<BounceComponent> bouncecm;
    ComponentMapper<WallComponent> wm;
    ComponentMapper<BulletComponent> bm;

    @SuppressWarnings("unchecked")
    public BounceCollisionSystem() {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, CollisionBoundComponent.class, BounceComponent.class));
    }


    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        CollisionBoundComponent cbc = cbm.get(e);
        BounceComponent bc = bouncecm.get(e);
        VelocityComponent vc = vm.get(e);
        PositionComponent pc = pm.get(e);

        for(Collider.Collision c : cbc.getRecentCollisions()){

            if(c != Collider.Collision.NONE) {
                switch (c) {
                    case BOTTOM:
                        if(bc.vertical) vc.velocity.y = Math.abs(vc.velocity.y);
                        break;
                    case TOP:
                        if(bc.vertical) vc.velocity.y = -Math.abs(vc.velocity.y);
                        break;
                    case LEFT:
                        if(bc.horizontal) vc.velocity.x = Math.abs(vc.velocity.x);
                        break;
                    case RIGHT:
                        if(bc.horizontal) vc.velocity.x = -Math.abs(vc.velocity.x);
                        break;
                }
                pc.position.x = cbc.bound.getX();
                pc.position.y = cbc.bound.getY();

                if (bm.has(e)) {
                    e.deleteFromWorld();
                }
            }

        }


    }





}
