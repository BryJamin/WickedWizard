package com.byrjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;

/**
 * Created by Home on 18/04/2017.
 */

public class ShoppingSystem extends EntitySystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<CurrencyComponent> cm;
    ComponentMapper<ActionOnTouchComponent> aotm;

    ComponentMapper<ParentComponent> pm;

    @SuppressWarnings("unchecked")
    public ShoppingSystem() {
        super(Aspect.all(ActionOnTouchComponent.class,
                CollisionBoundComponent.class
                /*AltarComponent.class,
                CurrencyComponent.class*/));
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }

    public boolean activeOnTouchTrigger(float inputX, float inputY) {
        for (Entity e : this.getEntities()) {
            if(cbm.get(e).bound.contains(inputX, inputY)){
                aotm.get(e).task.performAction(world, e);
                return true;
            }
        }
        return false;
    }

}
