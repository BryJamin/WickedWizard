package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.World;

/**
 * Created by Home on 12/04/2017.
 */

public interface Weapon {

    void fire(World world, float x, float y, double angle);



}
