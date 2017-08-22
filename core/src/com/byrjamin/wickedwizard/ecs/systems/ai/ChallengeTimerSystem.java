package com.byrjamin.wickedwizard.ecs.systems.ai;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent;

/**
 * Created by BB on 22/08/2017.
 */

public class ChallengeTimerSystem extends EntityProcessingSystem {

    ComponentMapper<ChallengeTimerComponent> challengeTimerMapper;

    @SuppressWarnings("unchecked")
    public ChallengeTimerSystem() {
        super(Aspect.all(ChallengeTimerComponent.class));
    }

    @Override
    protected void process(Entity e) {
        ChallengeTimerComponent ctc = challengeTimerMapper.get(e);
        ctc.time -= world.delta;
    }

}
