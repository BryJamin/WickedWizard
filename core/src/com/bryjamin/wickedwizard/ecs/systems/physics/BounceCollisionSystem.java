package com.bryjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;


/**
 * Created by Home on 11/03/2017.
 */
public class BounceCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<PlayerComponent> playerm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<BounceComponent> bouncecm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.WallComponent> wm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent> bm;

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


        //The sake of this is to stop the collision between two together vertical block (this is a test run)

        boolean leftOrRight = cbc.getRecentCollisions().contains(com.bryjamin.wickedwizard.utils.collider.Collider.Collision.LEFT, true) ||
                cbc.getRecentCollisions().contains(com.bryjamin.wickedwizard.utils.collider.Collider.Collision.RIGHT, true);

        boolean topOrBottom = cbc.getRecentCollisions().contains(com.bryjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM, true) ||
                cbc.getRecentCollisions().contains(com.bryjamin.wickedwizard.utils.collider.Collider.Collision.TOP, true);

/*
        if(leftOrRight && topOrBottom) {

            Array<Collider.Collision> collisionArray = new Array<Collider.Collision>();
            collisionArray.addAll(cbc.getRecentCollisions());

            for (Collider.Collision c : collisionArray) {
                cbc.getRecentCollisions().removeValue(c, false);
            }
        }
*/

        for(com.bryjamin.wickedwizard.utils.collider.Collider.Collision c : cbc.getRecentCollisions()){

            if(c != com.bryjamin.wickedwizard.utils.collider.Collider.Collision.NONE) {
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
