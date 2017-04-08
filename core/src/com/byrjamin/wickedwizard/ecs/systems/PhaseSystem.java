package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;

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

        pc.currentPhaseTime -= world.delta;

        if(pc.currentPhaseTime < 0){

            for(Component c : pc.getCurrentPhaseComponents(pc.currentPhase)){
                e.edit().remove(c.getClass());
            }

            pc.getPhaseSequence().add(pc.getPhaseSequence().first());
            pc.getPhaseSequence().removeIndex(0);

            pc.currentPhase = pc.getPhaseSequence().first();
            pc.currentPhaseTime = pc.getCurrentPhaseTimer(pc.currentPhase);

            for(Component c : pc.getCurrentPhaseComponents(pc.currentPhase)){
                e.edit().add(c);
            }

        }

    }

}