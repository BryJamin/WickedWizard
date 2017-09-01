package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

/**
 * Created by BB on 06/04/2017.
 *
 * Set the position of all entities with a 'FollowPositionComponent' at the designated offset
 * from the position they followings
 *
 */

public class FollowPositionSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent> pm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent> fm;

    @SuppressWarnings("unchecked")
    public FollowPositionSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent.class, com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent pc = pm.get(e);
        com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent fc = fm.get(e);
        pc.position.set(fc.trackedPosition.x + fc.offsetX, fc.trackedPosition.y + fc.offsetY, 0);
    }

}
