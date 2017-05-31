package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.object.AltarComponent;
import com.byrjamin.wickedwizard.ecs.components.object.PickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GlideComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;

/**
 * Created by Home on 10/04/2017.
 */

public class FrictionSystem extends EntityProcessingSystem {

    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<MoveToComponent> mtm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<PickUpComponent> im;
    ComponentMapper<GlideComponent> gm;


    @SuppressWarnings("unchecked")
    public FrictionSystem() {
        super(Aspect.all(VelocityComponent.class).one(PlayerComponent.class, PickUpComponent.class));
    }

    @Override
    protected void process(Entity e) {

        //PositionComponent pc = pm.get(e);

        if(canApplyFriction(e) || pm.has(e)) {
            VelocityComponent vc = vm.get(e);


            float friction = Measure.units(7.5f);
            float minSpeed = Measure.units(0f);

            if(pm.has(e) && gm.has(e)) {
                    if (!cbm.get(e).recentCollisions.contains(Collider.Collision.TOP, false) && !gm.get(e).gliding) {
                        minSpeed = Measure.units(20f);
                    }
            }

             if (vc.velocity.x >= minSpeed) {
                vc.velocity.x = (vc.velocity.x - friction <= minSpeed) ? minSpeed : vc.velocity.x - friction;
            } else if (vc.velocity.x <= -minSpeed) {
                vc.velocity.x = (vc.velocity.x + friction >= -minSpeed) ? -minSpeed : vc.velocity.x + friction;
            }
        }

    }


    public boolean canApplyFriction(Entity e){
        if(mtm.has(e)){
            if(mtm.get(e).hasTarget()){
                return false;
            }
        }

        if(im.has(e) && cbm.has(e)){
            return cbm.get(e).recentCollisions.contains(Collider.Collision.TOP, false);
        }

        if(gm.has(e) && cbm.has(e)){
            return cbm.get(e).recentCollisions.contains(Collider.Collision.TOP, false) ||
                    gm.get(e).gliding;
        }



        return true;
    }


}
