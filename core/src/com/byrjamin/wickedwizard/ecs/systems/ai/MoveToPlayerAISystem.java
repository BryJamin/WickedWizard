package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.utils.BulletMath;

/**
 * Created by Home on 06/03/2017.
 */
public class MoveToPlayerAISystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<AccelerantComponent> am;
    ComponentMapper<GravityComponent> gm;

    @SuppressWarnings("unchecked")
    public MoveToPlayerAISystem() {
        super(Aspect.all(PositionComponent.class,
                VelocityComponent.class,
                MoveToPlayerComponent.class,
                CollisionBoundComponent.class,
                AccelerantComponent.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void process(Entity e) {

            CollisionBoundComponent pBound = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);

            PositionComponent pc = pm.get(e);
            VelocityComponent vc = vm.get(e);
            CollisionBoundComponent cbc = cbm.get(e);
            AccelerantComponent ac = am.get(e);

            //TODO new if statements do not account for enemies with slow accelerations (they no longer slide)

            if (!cbc.bound.contains(pBound.getCenterX(), pBound.getCenterY()) && gm.has(e)) {
                if (cbc.getCenterX() > pBound.getCenterX()) {
                    vc.velocity.x  = (vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
                    if(cbc.getCenterX() - vc.velocity.x * world.delta < pBound.getCenterX()) vc.velocity.x = 0;
                } else {
                    vc.velocity.x  = (vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
                    if(cbc.getCenterX() + vc.velocity.x * world.delta > pBound.getCenterX()) vc.velocity.x = 0;
                }

            }


        if(!cbc.bound.contains(pBound.getCenterX(), pBound.getCenterY()) && !gm.has(e)) {

            double angleOfTravel = BulletMath.angleOfTravel(cbc.getCenterX(), cbc.getCenterY(), pBound.getCenterX(), pBound.getCenterY());

            System.out.println("MTPS Angle of Travel" + Math.toDegrees(angleOfTravel) + " Enitity " + e.getId());

            float vy = BulletMath.velocityY(vc.velocity.y, angleOfTravel);
            float accelY = BulletMath.velocityY(ac.accelY, angleOfTravel);
            float maxY = BulletMath.velocityY(ac.maxY, angleOfTravel);

            vc.velocity.y = (Math.abs(vy) + Math.abs(accelY) >= Math.abs(maxY)) ? maxY : vy + accelY;


            float vx = BulletMath.velocityX(vc.velocity.x, angleOfTravel);
            float accelX = BulletMath.velocityX(ac.accelX, angleOfTravel);
            float maxX = BulletMath.velocityX(ac.maxX, angleOfTravel);

            vc.velocity.x = (Math.abs(vx) + Math.abs(accelX) >= Math.abs(maxX)) ? maxX : vx + accelX;
            //vc.velocity.x = maxX;


            System.out.println(vx + accelX);


            System.out.println("Velocity X MTPS" + vc.velocity.x + " Enitity " + e.getId());
            System.out.println("Velocity Y " + vc.velocity.y + " Enitity " + e.getId());

           // vc.velocity.x = (Math.abs(vx) > Math.abs(maxX)) ? maxX : vx + accelX;

/*
           // vc.velocity.y = ()

            //TODO For now if an entity has gravity it can't really follow a player's Y so I just skip this application
            if (cbc.getCenterY() > pBound.getCenterY()) {
                vc.velocity.y = (vc.velocity.y <= -ac.maxY) ? -ac.maxY : vc.velocity.y - ac.accelY;
                if(cbc.getCenterY() - vc.velocity.y * world.delta < pBound.getCenterY()) vc.velocity.y = 0;
            } else {
                vc.velocity.y = (vc.velocity.y >= ac.maxY) ? ac.maxY : vc.velocity.y + ac.accelY;
                if(cbc.getCenterY() + vc.velocity.y * world.delta > pBound.getCenterY()) vc.velocity.y = 0;
            }*/
        } else if(cbc.bound.contains(pBound.getCenterX(), pBound.getCenterY())){
            vc.velocity.x = 0;
            vc.velocity.y = 0;
        }



    }

}
