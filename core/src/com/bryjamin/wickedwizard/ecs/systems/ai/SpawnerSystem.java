package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.utils.BagToEntity;

/**
 * Created by BB on 25/03/2017.
 *
 * Iterates through entities with the SpawnerComponent
 *
 * Creates a new Entity from the spawnBag method if the spawning time has been completed.
 * The Entity with the SpawnerComponent is deleted afterwards unless it's life span is greater than 0
 * or endless
 *
 * Update 29/07/2017
 * At this point I feel the spawner system is more of an Action after time system, but Spawner is
 * quite legacy
 *
 * Also, the way Spawner is built it is better at spawning enemies than an 'Action' unless I made a class that
 * implemented the 'Action' interface
 *
 * It's food for thought but not necessary at the moment
 *
 */

public class SpawnerSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.SpawnerComponent> sm;

    @SuppressWarnings("unchecked")
    public SpawnerSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.object.SpawnerComponent.class, PositionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        com.bryjamin.wickedwizard.ecs.components.object.SpawnerComponent sc = sm.get(e);
        PositionComponent pc = pm.get(e);
        sc.time -= world.delta;

        if(sc.time <= 0){
            if(sc.getSpawner().size > 0) {

                BagToEntity.bagToEntity(world.createEntity(), sc.getSpawner().first().spawnBag(pc.getX() + sc.offsetX, pc.getY() + sc.offsetY));
                sc.getSpawner().add(sc.getSpawner().first());
                sc.getSpawner().removeIndex(0);

            }

            sc.life--;

            if(sc.life <= 0 && !sc.isEndless) e.deleteFromWorld();
            else sc.time = sc.resetTime;

        }
    }

}
