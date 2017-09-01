package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 10/06/2017.
 */

public interface Action {
    void performAction(World world, Entity e);
}
