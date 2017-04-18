package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 12/04/2017.
 */

public interface Action {
    void performAction(World w, Entity e);
    void cleanUpAction(World w, Entity e);
}