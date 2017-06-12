package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 12/04/2017.
 */

public interface Task extends Action {
    void performAction(World world, Entity e);
    void cleanUpAction(World world, Entity e);
}
