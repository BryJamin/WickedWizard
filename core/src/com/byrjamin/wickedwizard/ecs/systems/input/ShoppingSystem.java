package com.byrjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.object.AltarComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;

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
               // if(world.getSystem(FindPlayerSystem.class).getPC(CurrencyComponent.class).money - cm.get(e).money >= 0) {
                  //  world.getSystem(FindPlayerSystem.class).getPC(CurrencyComponent.class).money -= cm.get(e).money;
        /*            aotm.get(e).action.performAction(world, e);

                    //TODO only certain items should be deleted from the world when bought. E.G keys or something

                    if(pm.has(e)) {
                        for(ChildComponent c : pm.get(e).children) {
                            world.getSystem(FindChildSystem.class).findChildEntity(c).deleteFromWorld();

                            //TODO children seem to not exist?
                        }
                    }*/

            aotm.get(e).action.performAction(world, e);

                    //e.deleteFromWorld();
                    return true;
                //}
            }
        }
        return false;
    }

}
