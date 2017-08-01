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
 * Created by BB on 30/03/2017.
 *
 * Iterates through each entity with a PhaseComponent
 *
 * If a condition has been met for the current phase the next phase stored within the PhaseComponent
 * becomes the current phase
 *
 */
public class PhaseSystem extends EntityProcessingSystem {

    ComponentMapper<PhaseComponent> pm;

    @SuppressWarnings("unchecked")
    public PhaseSystem() {
        super(Aspect.all(PhaseComponent.class));
    }

    @Override
    protected void process(Entity e) {

        PhaseComponent pc = pm.get(e);
        if(pc.phaseQueue.size <= 0) return;
        pc.currentPhaseTime += world.delta;

        if(!pc.hasActionBeenPerformed) {
            pc.phaseQueue.first().getLeft().performAction(world, e);
            pc.hasActionBeenPerformed = true;
        }

        if(pc.phaseQueue.first().getRight().condition(world, e)) startNextPhase(e);

    }


    /**
     * Starts the next phase of an entity
     *
     * Can be used to forcibly skip a condition check
     *
     * @param e - Entity with a phase component
     */
    public void startNextPhase(Entity e){
        PhaseComponent pc = pm.get(e);
        Pair<Task, Condition> taskConditionPair = pc.phaseQueue.removeFirst();
        pc.phaseQueue.addLast(taskConditionPair);
        taskConditionPair.getLeft().cleanUpAction(world, e);
        pc.hasActionBeenPerformed = false;
        pc.currentPhaseTime = 0;
    }


}