package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Entity;

/**
 * Created by Home on 12/04/2017.
 */

public interface Phase {
    void changePhase(Entity e);
    void cleanUp(Entity e);
}
