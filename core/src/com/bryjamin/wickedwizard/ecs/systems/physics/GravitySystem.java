package com.bryjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.movement.GlideComponent;

/**
 * Created by Home on 04/03/2017.
 */
public class GravitySystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent> vm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent> gm;
    ComponentMapper<GlideComponent> glidem;

    private static final float MAXGRAVITY = -com.bryjamin.wickedwizard.utils.Measure.units(110f);

    @SuppressWarnings("unchecked")
    public GravitySystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class, com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent.class));
    }

    @Override
    protected void process(Entity e) {
        com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent vc = vm.get(e);
        com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent gc = gm.get(e);

        if(!gc.ignoreGravity) {
            if(glidem.has(e)) {
                if (glidem.get(e).gliding && glidem.get(e).active && vc.velocity.y <= 0) {
                    vc.velocity.add(0, gc.gravity / 4);
/*                    if (vc.velocity.y < gc.gravity * 4f) {
                        vc.velocity.y = gc.gravity * 4f;
                    }*/
                } else {
                    vc.velocity.add(0, gc.gravity);
                    vc.velocity.y = vc.velocity.y > MAXGRAVITY ? vc.velocity.y : MAXGRAVITY;
                }
            } else {
                vc.velocity.add(0, gc.gravity);
                vc.velocity.y = vc.velocity.y > MAXGRAVITY ? vc.velocity.y : MAXGRAVITY;
            }
        }

    }

}
