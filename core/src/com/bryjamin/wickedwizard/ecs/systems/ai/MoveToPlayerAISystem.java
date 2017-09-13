package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.utils.BulletMath;

/**
 * Created by BB on 06/03/2017.
 *
 * System for managing entities with the MoveToPlayerComponent.
 *
 * Based on whether they have gravity or not the way they move to a player is different
 *
 */
public class MoveToPlayerAISystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent> pm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent> vm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent> cbm;
    ComponentMapper<AccelerantComponent> am;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent> gm;

    @SuppressWarnings("unchecked")
    public MoveToPlayerAISystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class,
                com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class,
                com.bryjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent.class,
                com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class,
                AccelerantComponent.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

        com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent pBound = world.getSystem(FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent.class);

        com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent pc = pm.get(e);
        com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = vm.get(e);
        com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent cbc = cbm.get(e);
        AccelerantComponent ac = am.get(e);

        //TODO moveToPlayer method does not account for enemies with slow accelerations (they no longer slide) Update: Probably won't factor this in

        if (!cbc.bound.contains(pBound.getCenterX(), pBound.getCenterY()) && gm.has(e)) {

            moveToPlayerWithGravityComponent(cbc, pBound, vc, ac);

        } else if(!cbc.bound.contains(pBound.getCenterX(), pBound.getCenterY()) && !gm.has(e)) {

            moveToPlayer(cbc, pBound, vc, ac);

        } else if(cbc.bound.contains(pBound.getCenterX(), pBound.getCenterY())){
            vc.velocity.x = 0;
            vc.velocity.y = 0;
        }



    }


    /**
     * To entities that have gravity and want to move towards a player. This method is used as there is
     * no need to use velocity y as that is being affected by gravity
     *
     * Based on the position of the player the entity's accelerant component is either applied negatively
     * or positively
     *
     * @param cbc - Collision boundary of the entity moving towards the player
     * @param playerCbc - Collision boundary of the player
     * @param vc - The VelocityComponent of the entity
     * @param ac - The AccelerantComponent of the entity
     */
    private void moveToPlayerWithGravityComponent(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent cbc, com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent playerCbc, com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc, AccelerantComponent ac){
        if (cbc.getCenterX() > playerCbc.getCenterX()) {
            vc.velocity.x  = (vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
            if(cbc.getCenterX() - vc.velocity.x * world.delta < playerCbc.getCenterX()) vc.velocity.x = 0;
        } else {
            vc.velocity.x  = (vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
            if(cbc.getCenterX() + vc.velocity.x * world.delta > playerCbc.getCenterX()) vc.velocity.x = 0;
        }
    }


    /**
     * Moves an Entity towards a player using both the accelX and accelY of their Accelerant components
     * and adding it to their velocity
     *
     * @param cbc - Collision boundary of the entity moving towards the player
     * @param playerCbc - Collision boundary of the player
     * @param vc - The VelocityComponent of the entity
     * @param ac - The AccelerantComponent of the entity
     */
    private void moveToPlayer(com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent cbc, com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent playerCbc, com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc, AccelerantComponent ac){


        double angleOfTravel = BulletMath.angleOfTravel(cbc.getCenterX(), cbc.getCenterY(), playerCbc.getCenterX(), playerCbc.getCenterY());

        float vy = BulletMath.velocityY(vc.velocity.y, angleOfTravel);
        float accelY = BulletMath.velocityY(ac.accelY, angleOfTravel);
        float maxY = BulletMath.velocityY(ac.maxY, angleOfTravel);

        vc.velocity.y = (Math.abs(vy) + Math.abs(accelY) >= Math.abs(maxY)) ? maxY : vc.velocity.y + accelY;


        float vx = BulletMath.velocityX(vc.velocity.x, angleOfTravel);
        float accelX = BulletMath.velocityX(ac.accelX, angleOfTravel);
        float maxX = BulletMath.velocityX(ac.maxX, angleOfTravel);

        vc.velocity.x = (Math.abs(vx) + Math.abs(accelX) >= Math.abs(maxX)) ? maxX : vc.velocity.x + accelX;

    }

}
