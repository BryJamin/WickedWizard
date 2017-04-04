package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;

/**
 * Created by Home on 05/03/2017.
 */
public class HealthSystem extends EntityProcessingSystem {

    ComponentMapper<HealthComponent> hm;
    ComponentMapper<VelocityComponent> vm;

    @SuppressWarnings("unchecked")
    public HealthSystem() {
        super(Aspect.all(HealthComponent.class));
    }

    @Override
    protected void process(Entity e) {
        HealthComponent hc = hm.get(e);
        if(hc.health <= 0){
            world.getSystem(OnDeathSystem.class).kill(e);
        }
    }





}
