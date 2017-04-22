package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.movement.GlideComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;

/**
 * Created by Home on 04/03/2017.
 */
public class GravitySystem extends EntityProcessingSystem {

    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<GravityComponent> gm;
    ComponentMapper<GlideComponent> glm;

    @SuppressWarnings("unchecked")
    public GravitySystem() {
        super(Aspect.all(VelocityComponent.class, GravityComponent.class));
    }

    @Override
    protected void process(Entity e) {
        VelocityComponent vc = vm.get(e);
        GravityComponent gc = gm.get(e);

        if(!gc.ignoreGravity) {
            vc.velocity.add(0, gc.gravity);
            if(glm.has(e)) {
                if (glm.get(e).gliding && glm.get(e).active) {
                    if (vc.velocity.y < gc.gravity) {
                        vc.velocity.y = gc.gravity;

                    }
                }
            }
        } else {
        }
    }

}
