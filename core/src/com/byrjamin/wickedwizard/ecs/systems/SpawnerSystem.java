package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.SpawnerComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;

/**
 * Created by Home on 25/03/2017.
 */

public class SpawnerSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<SpawnerComponent> sm;

    @SuppressWarnings("unchecked")
    public SpawnerSystem() {
        super(Aspect.all(SpawnerComponent.class));
    }

    @Override
    protected void process(Entity e) {
        SpawnerComponent sc = sm.get(e);
        sc.time -= world.delta;
        //System.out.println(sc.time);
        if(sc.time <= 0){
            if(!sc.getSpawnedEntity().isEmpty()) {
                Entity spawnedEntity = world.createEntity();
                for (Component c : sc.getSpawnedEntity()) {
                    spawnedEntity.edit().add(c);
                }
            }
            e.deleteFromWorld();
        }
    }

}
