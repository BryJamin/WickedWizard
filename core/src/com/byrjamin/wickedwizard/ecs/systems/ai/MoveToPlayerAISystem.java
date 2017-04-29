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

            if (!cbc.bound.contains(pBound.getCenterX(), pBound.getCenterY())) {
                if (cbc.getCenterX() > pBound.getCenterX()) {
                    vc.velocity.x  = (vc.velocity.x <= -ac.maxX) ? -ac.maxX : vc.velocity.x - ac.accelX;
                    if(cbc.getCenterX() - vc.velocity.x * world.delta < pBound.getCenterX()) vc.velocity.x /= 2;
                } else {
                    vc.velocity.x  = (vc.velocity.x >= ac.maxX) ? ac.maxX : vc.velocity.x + ac.accelX;
                    if(cbc.getCenterX() + vc.velocity.x * world.delta > pBound.getCenterX()) vc.velocity.x /= 2;
                }

                if(!gm.has(e)) { //TODO For now if an entity has gravity it can't really follow a player's Y so I just skip this application
                    if (cbc.getCenterY() > pBound.getCenterY()) {
                        vc.velocity.y = (vc.velocity.y <= -ac.maxY) ? -ac.maxY : vc.velocity.y - ac.accelY;
                        if(cbc.getCenterY() - vc.velocity.y * world.delta < pBound.getCenterY()) vc.velocity.y = 0;
                    } else {
                        vc.velocity.y = (vc.velocity.y >= ac.maxY) ? ac.maxY : vc.velocity.y + ac.accelY;
                        if(cbc.getCenterY() + vc.velocity.y * world.delta > pBound.getCenterY()) vc.velocity.y = 0;
                    }
                }
            }



    }

}
