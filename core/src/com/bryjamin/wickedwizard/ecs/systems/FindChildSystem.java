package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.IntBag;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;


/**
 * Created by Home on 17/04/2017.
 */

public class FindChildSystem extends EntitySystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public FindChildSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent.class));
    }

    @Override
    protected void processSystem() {

    }

    public Entity findChildEntity(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c){
        for(Entity e : this.getEntities()){
            if(e.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent.class) == c) return e;
        }
        return null;
    }


    public Entity findParentEntity(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent c){
        IntBag parents = world.getAspectSubscriptionManager().get(Aspect.all(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class)).getEntities();
        for(int i = 0; i < parents.size(); i++){
            com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent pc = world.getEntity(parents.get(i)).getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class);

            //TODO if you kill two parents in the exact same game loop the Aspect does not update yet.
            //TODO it may be better to link children to parents to avoid the null pointer exception that can occur
            if(pc == null) return null;

            if(pc.children.contains(c, true)){
                return world.getEntity(parents.get(i));
            }
        }
        return null;
    }

}
