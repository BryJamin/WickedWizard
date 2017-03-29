package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
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
    ComponentMapper<AccelerantComponent> am;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public MoveToSystem() {
        super(Aspect.all(PositionComponent.class, MoveToComponent.class, VelocityComponent.class, AccelerantComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        MoveToComponent mtc = mtm.get(e);
        AccelerantComponent ac = am.get(e);

        Float targetX = mtc.targetX;

        if(targetX != null){

            CollisionBoundComponent cbc = cbm.get(e);

            float currentPosition = (cbm.has(e)) ? cbm.get(e).getCenterX() : pc.getX();

            if (cbc.bound.contains(mtc.targetX, cbc.getCenterY())) {
                vc.velocity.x = mtc.endSpeedX; //For grappling
                mtc.targetX = null;
            } else if (currentPosition >= targetX) {
                vc.velocity.x = mtc.speedX;
                //vc.velocity.add(mtc.speedX, 0);//(vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
            } else {
                vc.velocity.x = mtc.speedX;
                //vc.velocity.add(mtc.speedX, 0); //(vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
            }
        }

        Float targetY = mtc.targetY;

        if(targetY != null){

            CollisionBoundComponent cbc = cbm.get(e);

            float currentPosition = (cbm.has(e)) ? cbm.get(e).getCenterY() : pc.getY();

            if (cbc.bound.contains(cbc.getCenterX(), mtc.targetY)) {
                vc.velocity.y = mtc.endSpeedY; //For grappling
                mtc.targetY = null;
            } else if (currentPosition >= targetY) {
                vc.velocity.y = mtc.speedY;
                //vc.velocity.add(0, mtc.speedY); //(vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
            } else {
                vc.velocity.y = mtc.speedY;
                //vc.velocity.add(0, mtc.speedY); //(vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
            }
        }




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

