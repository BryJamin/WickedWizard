package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.UIComponent;

/**
 * Created by BB on 06/04/2017.
 *
 * Set the position of all entities with a 'FollowPositionComponent' at the designated offset
 * from the position they followings
 *
 */

public class FollowPositionSystem extends EntityProcessingSystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<FollowPositionComponent> fm;

    @SuppressWarnings("unchecked")
    public FollowPositionSystem() {
        super(Aspect.all(FollowPositionComponent.class, PositionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent pc = pm.get(e);
        FollowPositionComponent fc = fm.get(e);
        pc.position.set(fc.trackedPosition.x + fc.offsetX, fc.trackedPosition.y + fc.offsetY, 0);
    }

}
