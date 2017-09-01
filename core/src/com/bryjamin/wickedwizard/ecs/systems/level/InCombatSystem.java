package com.bryjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;

/**
 * Created by BB on 20/05/17.
 *
 * System used to track all Entities with the In Combat Action Component
 *
 * This System is called by the
 *
 */
public class InCombatSystem extends EntitySystem {

    private boolean isInCombat;

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.ai.InCombatActionComponent> icam;

    public InCombatSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.ai.InCombatActionComponent.class));
    }


    @Override
    protected boolean checkProcessing() {
        return false;
    }

    @Override
    protected void processSystem() {

    }


    /**
     * Goes through all entities with the InCombatActionComponent and Performs their InCombat Actions
     */
    public void goInCombat(){
        if(!isInCombat) {
            isInCombat = true;
            for (Entity e : this.getEntities()) {
                icam.get(e).task.performAction(world, e);
            }
        }
    }

    /**
     * Goes through all entities with the InCombatActionComponent and Performs their Out of Combat
     * clean up Actions
     */
    public void leaveCombat(){
        if(isInCombat) {
            isInCombat = false;
            for (Entity e : this.getEntities()) {
                icam.get(e).task.cleanUpAction(world, e);
            }
        }
    }

}
