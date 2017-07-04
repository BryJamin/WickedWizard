package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 04/07/2017.
 */

public class MoveToSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<MoveToPositionComponent> mtpm;
    ComponentMapper<AccelerantComponent> am;
    ComponentMapper<GravityComponent> gm;

    @SuppressWarnings("unchecked")
    public MoveToSystem() {
        super(Aspect.all(PositionComponent.class,
                VelocityComponent.class,
                MoveToPositionComponent.class,
                CollisionBoundComponent.class,
                AccelerantComponent.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        Vector3 targetPosition = mtpm.get(e).moveToPosition;

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);
        AccelerantComponent ac = am.get(e);

        //TODO new if statements do not account for enemies with slow accelerations (they no longer slide)

        System.out.println();

        if (!mtpm.get(e).isOnTargetX && gm.has(e)) {
            if (cbc.getCenterX() > targetPosition.x) {
                vc.velocity.x  = (vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
                if(cbc.getCenterX() - vc.velocity.x * world.delta <= targetPosition.x) {
                    vc.velocity.x = 0;
                    cbc.bound.setCenter(new Vector2(targetPosition.x, cbc.getCenterY()));
                    mtpm.get(e).isOnTargetX = true;
                }

                if(cbc.getRecentCollisions().contains(Collider.Collision.LEFT, true)){
                    //If you collide with something your movement should stop.
                    vc.velocity.x = 0;
                    mtpm.get(e).isOnTargetX = true;
                }
            } else {
                vc.velocity.x  = (vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
                if(cbc.getCenterX() + vc.velocity.x * world.delta >= targetPosition.x){
                    vc.velocity.x = 0;
                    cbc.bound.setCenter(new Vector2(targetPosition.x, cbc.getCenterY()));
                    mtpm.get(e).isOnTargetX = true;
                }

                if(cbc.getRecentCollisions().contains(Collider.Collision.RIGHT, true)){
                    vc.velocity.x = 0;
                    mtpm.get(e).isOnTargetX = true;
                }

            }
        }


        if(!cbc.bound.contains(targetPosition.x, targetPosition.y) && !gm.has(e)) {

            double angleOfTravel = BulletMath.angleOfTravel(cbc.getCenterX(), cbc.getCenterY(),
                    targetPosition.x, targetPosition.y);

            float vy = BulletMath.velocityY(vc.velocity.y, angleOfTravel);
            float accelY = BulletMath.velocityY(ac.accelY, angleOfTravel);
            float maxY = BulletMath.velocityY(ac.maxY, angleOfTravel);

            vc.velocity.y = (Math.abs(vy) + Math.abs(accelY) >= Math.abs(maxY)) ? maxY : vc.velocity.y + accelY;


            float vx = BulletMath.velocityX(vc.velocity.x, angleOfTravel);
            float accelX = BulletMath.velocityX(ac.accelX, angleOfTravel);
            float maxX = BulletMath.velocityX(ac.maxX, angleOfTravel);

            vc.velocity.x = (Math.abs(vx) + Math.abs(accelX) >= Math.abs(maxX)) ? maxX : vc.velocity.x + accelX;

        } else if(cbc.bound.contains(targetPosition.x, targetPosition.y)){
            vc.velocity.x = 0;
            vc.velocity.y = 0;
        }



    }


    public boolean isOnTargetX(MoveToPositionComponent mtpc, CollisionBoundComponent cbc){

        System.out.println(mtpc.moveToPosition.x);
        System.out.println(cbc.getCenterX());


        return mtpc.moveToPosition.x == cbc.getCenterX();
    }

}
