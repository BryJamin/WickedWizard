package com.bryjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.object.WallComponent;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 22/03/2017.
 */

public class GrappleSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<MoveToComponent> mtm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WallComponent> wm;

    @SuppressWarnings("unchecked")
    public GrappleSystem() {
        super(Aspect.all(PositionComponent.class, MoveToComponent.class, VelocityComponent.class, AccelerantComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        MoveToComponent mtc = mtm.get(e);
        CollisionBoundComponent cbc = cbm.get(e);

        if(vc.velocity.y < 0 && cbc.getRecentCollisions().contains(Collider.Collision.BOTTOM, true)){
            mtc.targetX = null;
            mtc.targetY = null;
        } else if(vc.velocity.y > 0 && cbc.getRecentCollisions().contains(Collider.Collision.TOP, true)) {
            mtc.targetX = null;
            mtc.targetY = null;
        }


        Float targetX = mtc.targetX;



        if(targetX != null){

            float currentPosition = (cbm.has(e)) ? cbm.get(e).getCenterX() : pc.getX();

            if (cbc.bound.contains(mtc.targetX, cbc.getCenterY())) {
                mtc.targetX = null;
                vc.velocity.x = vc.velocity.x / 4;
                //vc.velocity.x = mtc.endSpeedX;
            } else if (currentPosition >= targetX) {
                vc.velocity.x = (vc.velocity.x <= -mtc.maxX) ? -mtc.maxX : vc.velocity.x - mtc.accelX;
            } else {
                vc.velocity.x = (vc.velocity.x >= mtc.maxX) ? mtc.maxX : vc.velocity.x + mtc.accelX;
            }
        }

        Float targetY = mtc.targetY;

        if(targetY != null){

            float currentPosition = (cbm.has(e)) ? cbm.get(e).getCenterY() : pc.getY();

            if (cbc.bound.contains(cbc.getCenterX(), mtc.targetY)) {
                mtc.targetY = null;
                vc.velocity.y = (vc.velocity.y < Measure.units(0)) ? 0 : vc.velocity.y;
                vc.velocity.y = (vc.velocity.y > mtc.maxEndSpeedY) ? mtc.maxEndSpeedY : vc.velocity.y;

                //TODO review this.
                if(world.getMapper(JumpComponent.class).has(e)) {
                    e.getComponent(JumpComponent.class).jumps = 1;
                }


            } else if (currentPosition >= targetY) {
                vc.velocity.y = (vc.velocity.y <= -mtc.maxX) ? -mtc.maxY : vc.velocity.y - mtc.accelY;
            } else {
                vc.velocity.y = (vc.velocity.y >= mtc.maxY) ? mtc.maxY : vc.velocity.y + mtc.accelY;
            }
        }



/*
    if(mtc.targetX != null && mtc.targetY != null) {
        if (cbc.bound.contains(targetX, targetY)) {
            endTravel(vc, mtc);
        }
    }
*/





    }

    public void flyToNoPathCheck(double angleOfTravel, float x, float y, float speedOfTravel, MoveToComponent mtc, CollisionBoundComponent cbc){

        float cosine = (float) Math.cos(angleOfTravel);
        float sine = (float) Math.sin(angleOfTravel);

        //Vector2 flyPath = flyPathCheck(angleOfTravel, distanceOfTravel, cbc.bound);

        mtc.targetX = x;
        mtc.targetY = y;

        //mtc.speedX = cosine * speedOfTravel;
        //mtc.speedY = sine * speedOfTravel;

        mtc.accelX = Math.abs(cosine * speedOfTravel);
        mtc.accelY = Math.abs(sine * speedOfTravel);

        mtc.maxX = Math.abs(cosine * speedOfTravel);
        mtc.maxY = Math.abs(sine * speedOfTravel);

        //mtc.endSpeedY =

    }


    public static void moveTo(float target, float currentPosition, AccelerantComponent ac, VelocityComponent vc){
        if (target - 20 <= currentPosition && currentPosition < target + 20) {
            vc.velocity.x = 0;
        } else if (currentPosition >= target) {
            vc.velocity.x  = (vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
        } else {
            vc.velocity.x  = (vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
        }
    }

    public static void decelerate(AccelerantComponent ac, VelocityComponent vc){

        if (vc.velocity.x >= -50 && vc.velocity.x < 50) {
            vc.velocity.x = 0;
        }else if(vc.velocity.x >= 0) {
            vc.velocity.x = (vc.velocity.x - ac.accelX <= 0) ? 0 : vc.velocity.x - ac.accelX;
        } else if(vc.velocity.x <= 0){
            vc.velocity.x = (vc.velocity.x + ac.accelX >= 0) ? 0 : vc.velocity.x + ac.accelX;
        }
    }


}

