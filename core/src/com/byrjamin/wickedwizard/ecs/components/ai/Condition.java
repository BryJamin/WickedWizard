package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by ae164 on 20/05/17.
 */

public interface Condition{
    boolean condition(World world, Entity entity);
}
