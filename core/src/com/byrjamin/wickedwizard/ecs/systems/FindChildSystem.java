package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;

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


    public Entity findParentEntity(ChildComponent c){
        IntBag parents = world.getAspectSubscriptionManager().get(Aspect.all(ParentComponent.class)).getEntities();
        for(int i = 0; i < parents.size(); i++){
            ParentComponent pc = world.getEntity(parents.get(i)).getComponent(ParentComponent.class);
            if(pc.children.contains(c, true)){
                return world.getEntity(parents.get(i));
            }
        }
        return null;
    }

}
