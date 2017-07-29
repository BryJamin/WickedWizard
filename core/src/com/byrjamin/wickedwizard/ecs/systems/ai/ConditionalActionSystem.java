package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.ConditionalActionComponent;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by BB on 20/05/17.
 *
 * Performs actions within all entities with ConditionActionComponents if the condition has been met.
 */
public class ConditionalActionSystem extends EntityProcessingSystem {

    ComponentMapper<ConditionalActionComponent> cac;

    @SuppressWarnings("unchecked")
    public ConditionalActionSystem() {
        super(Aspect.all(ConditionalActionComponent.class));
    }


    @Override
    protected void process(Entity e) {
        ConditionalActionComponent conditionalActionComponent = cac.get(e);
        for(Pair<Action, Condition> taskConditionPair : conditionalActionComponent.actionConditionPairArray){
            if(taskConditionPair.getRight().condition(world, e)){
                taskConditionPair.getLeft().performAction(world, e);
            }
        }
    }
}
