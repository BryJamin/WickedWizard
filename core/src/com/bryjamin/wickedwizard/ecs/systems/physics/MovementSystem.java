package com.bryjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem;
import com.bryjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 04/03/2017.
 */
public class MovementSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent> dm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ProximityTriggerAIComponent> ptam;

    @SuppressWarnings("unchecked")
    public MovementSystem() {
        super(Aspect.all(PositionComponent.class));
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);

        if(vm.has(e)) {
            com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = vm.get(e);
            pc.position.add(vc.velocity.x * world.delta, vc.velocity.y * world.delta, 0);

        //    System.out.println("VelY is + " + vc.velocity.y);

            if(dm.has(e)) {
                if (vc.velocity.x < 0)
                    DirectionalSystem.changeDirection(e, Direction.LEFT, com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent.PRIORITY.LOW);
                else if (vc.velocity.x > 0)
                    DirectionalSystem.changeDirection(e, Direction.RIGHT, com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent.PRIORITY.LOW);
            }

        }


        if(cbm.has(e)) {
            CollisionBoundComponent cbc = cbm.get(e);
            cbc.bound.x = pc.getX();
            cbc.bound.y = pc.getY();




            for(com.bryjamin.wickedwizard.utils.collider.HitBox hb : cbc.hitBoxes){
                hb.hitbox.x = pc.getX() + hb.offsetX;
                hb.hitbox.y = pc.getY() + hb.offsetY;
            }

        }

        if(ptam.has(e)){
            for(com.bryjamin.wickedwizard.utils.collider.HitBox hb : ptam.get(e).proximityHitBoxes){
                hb.hitbox.x = pc.getX() + hb.offsetX;
                hb.hitbox.y = pc.getY() + hb.offsetY;
            }
        }



    }

}
