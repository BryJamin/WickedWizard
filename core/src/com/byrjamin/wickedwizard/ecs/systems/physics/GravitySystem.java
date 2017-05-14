package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.movement.GlideComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 04/03/2017.
 */
public class GravitySystem extends EntityProcessingSystem {

    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<GravityComponent> gm;
    ComponentMapper<GlideComponent> glm;

    private static final float MAXGRAVITY = -Measure.units(110f);

    @SuppressWarnings("unchecked")
    public GravitySystem() {
        super(Aspect.all(VelocityComponent.class, GravityComponent.class));
    }

    @Override
    protected void process(Entity e) {
        VelocityComponent vc = vm.get(e);
        GravityComponent gc = gm.get(e);

        if(!gc.ignoreGravity) {
            if(glm.has(e)) {
                if (glm.get(e).gliding && glm.get(e).active && vc.velocity.y <= 0) {
                    vc.velocity.add(0, gc.gravity);
                    if (vc.velocity.y < gc.gravity * 4f) {
                        vc.velocity.y = gc.gravity * 4f;
                    }
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
