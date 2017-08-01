package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by BB on 04/07/2017.
 *
 * Similar to the MoveToPlayerAI System this class replaces moving to a player position to moving to
 * any position
 *
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


        if (!mtpm.get(e).isOnTargetX && gm.has(e)) {

            if(moveToPositionWithGravity(cbc, targetPosition, vc, ac)) {
                vc.velocity.x = 0;
                cbc.bound.setCenter(targetPosition.x, cbc.getCenterY());
                mtpm.get(e).isOnTargetX = true;
            }

            if(cbc.getRecentCollisions().contains(Collider.Collision.RIGHT, true) || cbc.getRecentCollisions().contains(Collider.Collision.LEFT, true)){
                vc.velocity.x = 0;
                mtpm.get(e).isOnTargetX = true;
            }

        }


        if(!cbc.bound.contains(targetPosition.x, targetPosition.y) && !gm.has(e)) {
            moveToPosition(cbc, targetPosition, vc, ac);
        } else if(cbc.bound.contains(targetPosition.x, targetPosition.y)){
            vc.velocity.x = 0;
            vc.velocity.y = 0;
        }



    }




    /**
     * Moves an Entity towards a player using both the accelX and accelY of their Accelerant components
     * and adding it to their velocity
     *
     * @param cbc - Collision boundary of the entity moving towards the player
     * @param targetPosition - Target position of the entity's moveToPosition Component
     * @param vc - The VelocityComponent of the entity
     * @param ac - The AccelerantComponent of the entity
     */
    private void moveToPosition(CollisionBoundComponent cbc, Vector3 targetPosition, VelocityComponent vc, AccelerantComponent ac){


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

    }


    /**
     * Method for moving an entity towards a target position using it's Accelerant and Velocity components
     *
     * @param cbc - Collision boundary of the entity moving towards the player
     * @param targetPosition - Target position of the entity's moveToPosition Component
     * @param vc - The VelocityComponent of the entity
     * @param ac - The AccelerantComponent of the entity
     * @return -True if the entity has reached the target position
     */
    private boolean moveToPositionWithGravity(CollisionBoundComponent cbc, Vector3 targetPosition, VelocityComponent vc, AccelerantComponent ac){

        if (cbc.getCenterX() > targetPosition.x) {
            vc.velocity.x  = (vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
            return cbc.getCenterX() - vc.velocity.x * world.delta <= targetPosition.x;
        } else {
            vc.velocity.x = (vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
            return cbc.getCenterX() + vc.velocity.x * world.delta >= targetPosition.x;
        }
    }





}
