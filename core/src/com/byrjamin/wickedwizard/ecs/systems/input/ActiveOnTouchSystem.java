package com.byrjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;

/**
 * Created by Home on 19/03/2017.
 */

public class ActiveOnTouchSystem extends EntitySystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ActiveOnTouchComponent> aotm;


    @SuppressWarnings("unchecked")
    public ActiveOnTouchSystem() {
        super(Aspect.all(ActiveOnTouchComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }

    public void activeOnTouchTrigger(float inputX, float inputY) {
        for(Entity e : this.getEntities()){
            System.out.println(aotm.get(e).isActive);
            if(aotm.get(e).isEnabled) {
                aotm.get(e).isActive = cbm.get(e).bound.contains(inputX, inputY);
            }
        }
    }


}