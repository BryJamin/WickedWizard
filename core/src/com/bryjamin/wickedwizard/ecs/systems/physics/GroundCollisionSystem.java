package com.bryjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.IntangibleComponent;
import com.bryjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by ae164 on 21/05/17.
 */

public class GroundCollisionSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent> pm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public GroundCollisionSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class, com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class, CollisionBoundComponent.class).exclude(com.bryjamin.wickedwizard.ecs.components.movement.BounceComponent.class, IntangibleComponent.class));
    }


    @Override
    protected void begin() {
        super.begin();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        CollisionBoundComponent cbc = cbm.get(e);
        com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = vm.get(e);
        com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent pc = pm.get(e);

        boolean topOrBottom = cbc.getRecentCollisions().contains(Collider.Collision.BOTTOM, false) ||
                cbc.getRecentCollisions().contains(Collider.Collision.TOP, false);

        boolean leftOrRight = cbc.getRecentCollisions().contains(Collider.Collision.LEFT, false) ||
                cbc.getRecentCollisions().contains(Collider.Collision.RIGHT, false);



        if(topOrBottom && leftOrRight){

        }


        if(cbc.getRecentCollisions().contains(Collider.Collision.BOTTOM, false)){
            if(vc.velocity.y <= 0) vc.velocity.y = 0;
        }


/*        if((cbc.getRecentCollisions().contains(Collider.Collision.TOP, true))) {
            vc.velocity.y = 0;
        }*/


        if((cbc.getRecentCollisions().contains(Collider.Collision.TOP, true) && !leftOrRight)) {

            //System.out.println("INSIDE");

             vc.velocity.y = 0;
        }

        if(cbc.getRecentCollisions().contains(Collider.Collision.LEFT, true) || cbc.getRecentCollisions().contains(Collider.Collision.RIGHT, true)){

/*
            System.out.println("Velocity X is 0 hombre \n");


            for(Collider.Collision c : cbc.getRecentCollisions()){
                System.out.println("Collision is " + c + "\n");
            }*/

            vc.velocity.x = 0;
        }

/*
        if(cbc.getRecentCollisions().size >= 2) {
            for (Collider.Collision c : cbc.getRecentCollisions()) {
                System.out.println("Collision is " + c);
            }
        }*/


    }





}
