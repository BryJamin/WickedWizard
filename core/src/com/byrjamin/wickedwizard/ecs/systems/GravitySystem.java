package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;

/**
 * Created by Home on 04/03/2017.
 */
public class GravitySystem extends EntityProcessingSystem {

    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<GravityComponent> gm;

    @SuppressWarnings("unchecked")
    public GravitySystem() {
        super(Aspect.all(VelocityComponent.class, GravityComponent.class));
    }

    @Override
    protected void process(Entity e) {
        VelocityComponent vc = vm.get(e);
        GravityComponent gc = gm.get(e);
        vc.velocity.add(0, gc.gravity);
    }

}
