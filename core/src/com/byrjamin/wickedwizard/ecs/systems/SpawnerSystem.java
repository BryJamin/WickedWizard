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
    ComponentMapper<SpawnerComponent> sm;

    @SuppressWarnings("unchecked")
    public SpawnerSystem() {
        super(Aspect.all(SpawnerComponent.class, PositionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        SpawnerComponent sc = sm.get(e);
        PositionComponent pc = pm.get(e);
        sc.time -= world.delta;
        //System.out.println(sc.time);
        if(sc.time <= 0){
            if(sc.getSpawner().size > 0) {
                Entity spawnedEntity = world.createEntity();
                for (Component c : sc.getSpawner().first().spawnBag(pc.getX() + sc.offsetX, pc.getY() + sc.offsetY)) {
                    spawnedEntity.edit().add(c);
                }
                sc.getSpawner().add(sc.getSpawner().first());
                sc.getSpawner().removeIndex(0);

            }

            sc.life--;

            if(sc.life <= 0){
                e.deleteFromWorld();
            } else {
                sc.time = sc.resetTime;
            }
            //e.deleteFromWorld();
        }
    }

}
