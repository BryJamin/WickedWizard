package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Condition;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 30/03/2017.
 */

public class PhaseSystem extends EntityProcessingSystem {

    ComponentMapper<PhaseComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<CollisionBoundComponent> cbm;

    @SuppressWarnings("unchecked")
    public PhaseSystem() {
        super(Aspect.all(PhaseComponent.class));
    }

    @Override
    protected void process(Entity e) {

        PhaseComponent pc = pm.get(e);
        pc.currentPhaseTime += world.delta;

        if(!pc.hasActionBeenPerformed) {
            pc.phaseQueue.first().getLeft().performAction(world, e);
            pc.hasActionBeenPerformed = true;
        }

        if(pc.phaseQueue.first().getRight().condition(world, e)){
            Pair<Task, Condition> taskConditionPair = pc.phaseQueue.removeFirst();
            pc.phaseQueue.addLast(taskConditionPair);
            taskConditionPair.getLeft().cleanUpAction(world, e);
            pc.hasActionBeenPerformed = false;
            pc.currentPhaseTime = 0;
        }

    }

}