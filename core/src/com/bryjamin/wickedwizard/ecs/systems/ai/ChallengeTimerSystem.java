package com.bryjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

/**
 * Created by BB on 22/08/2017.
 */

public class ChallengeTimerSystem extends EntityProcessingSystem {

    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent> challengeTimerMapper;

    @SuppressWarnings("unchecked")
    public ChallengeTimerSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent.class));
    }

    @Override
    protected void process(Entity e) {
        com.bryjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent ctc = challengeTimerMapper.get(e);
        ctc.time -= world.delta;
    }

}
