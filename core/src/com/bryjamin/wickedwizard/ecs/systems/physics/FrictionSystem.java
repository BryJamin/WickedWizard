package com.bryjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

/**
 * Created by Home on 10/04/2017.
 */

public class FrictionSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent> pm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent> vm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.MoveToComponent> mtm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent> cbm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.PickUpComponent> pickUpm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.GlideComponent> glidem;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent> gravm;

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent> frictionMapper;


    @SuppressWarnings("unchecked")
    public FrictionSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent.class, com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class));
    }

    @Override
    protected void process(Entity e) {

        //PositionComponent pc = pm.get(e);
        com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = vm.get(e);

        if(canApplyFriction(e) || pm.has(e)) {
            //VelocityComponent vc = vm.get(e);
            com.bryjamin.wickedwizard.ecs.components.movement.FrictionComponent fc = frictionMapper.get(e);


            float friction = com.bryjamin.wickedwizard.utils.Measure.units(7.5f);
            float minSpeed = com.bryjamin.wickedwizard.utils.Measure.units(0f);

            if(pm.has(e) && glidem.has(e)) {
                    if (!cbm.get(e).recentCollisions.contains(com.bryjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM, false) && !glidem.get(e).gliding) {
                        minSpeed = com.bryjamin.wickedwizard.utils.Measure.units(20f);
                    }
            }

            if(fc.horizontalFriction) {
                if (vc.velocity.x >= minSpeed) {
                    vc.velocity.x = (vc.velocity.x - friction <= minSpeed) ? minSpeed : vc.velocity.x - friction;
                } else if (vc.velocity.x <= -minSpeed) {
                    vc.velocity.x = (vc.velocity.x + friction >= -minSpeed) ? -minSpeed : vc.velocity.x + friction;
                }
            }

            if(fc.verticalFriction) {
                if (vc.velocity.y >= minSpeed) {
                    vc.velocity.y = (vc.velocity.y - friction <= minSpeed) ? minSpeed : vc.velocity.y - friction;
                } else if (vc.velocity.y <= -minSpeed) {
                    vc.velocity.y = (vc.velocity.y + friction >= -minSpeed) ? -minSpeed : vc.velocity.y + friction;
                }
            }



        }



    }


    public boolean canApplyFriction(Entity e){
        if(mtm.has(e)){
            if(mtm.get(e).hasTarget()){
                return false;
            }
        }

        if(pickUpm.has(e) && cbm.has(e) && gravm.has(e) || !frictionMapper.get(e).airFriction){
            return cbm.get(e).recentCollisions.contains(com.bryjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM, false);
        }

        if(glidem.has(e) && cbm.has(e)){
            return cbm.get(e).recentCollisions.contains(com.bryjamin.wickedwizard.utils.collider.Collider.Collision.BOTTOM, false) ||
                    glidem.get(e).gliding;
        }



        return true;
    }


}
