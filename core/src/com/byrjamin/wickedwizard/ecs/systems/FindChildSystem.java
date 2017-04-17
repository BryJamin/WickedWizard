package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem;
import com.byrjamin.wickedwizard.utils.collider.HitBox;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 17/04/2017.
 */

public class FindChildSystem extends EntitySystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public FindChildSystem() {
        super(Aspect.all(ChildComponent.class));
    }

    @Override
    protected void processSystem() {

    }

    public Entity findChildEntity(ChildComponent c){
        for(Entity e : this.getEntities()){
            if(e.getComponent(ChildComponent.class) == c) return e;
        }
        return null;
    }


}
