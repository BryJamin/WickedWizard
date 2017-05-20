package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.InCombatActionComponent;

/**
 * Created by ae164 on 20/05/17.
 */

public class InCombatSystem extends EntitySystem {

    private boolean isInCombat;

    ComponentMapper<InCombatActionComponent> icam;

    public InCombatSystem() {
        super(Aspect.all(InCombatActionComponent.class));
    }


    @Override
    protected boolean checkProcessing() {
        return false;
    }

    @Override
    protected void processSystem() {

    }



    public void goInCombat(){
        if(!isInCombat) {
            isInCombat = true;
            for (Entity e : this.getEntities()) {
                icam.get(e).action.performAction(world, e);
            }
        }
    }

    public void leaveCombat(){
        if(isInCombat) {
            isInCombat = false;
            for (Entity e : this.getEntities()) {
                icam.get(e).action.cleanUpAction(world, e);
            }
        }
    }

}
