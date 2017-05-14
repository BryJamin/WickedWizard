package com.byrjamin.wickedwizard.ecs.systems.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;

/**
 * Created by Home on 29/04/2017.
 */

public class ClearCollisionsSystem extends EntitySystem{


    ComponentMapper<CollisionBoundComponent> cm;

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     *
     */
    public ClearCollisionsSystem() {
        super(Aspect.all(CollisionBoundComponent.class));
    }

    @Override
    protected void processSystem() {
        for(int i = 0; i < this.getEntityIds().size(); i++) {
            cm.get(this.getEntityIds().get(i)).getRecentCollisions().clear();
        }
    }
}
