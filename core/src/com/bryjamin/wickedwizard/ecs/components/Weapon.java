package com.bryjamin.wickedwizard.ecs.components;

import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 12/04/2017.
 */

public interface Weapon {

    public float baseDamage = 1;
    public float baseFireRate = 1;

    void fire(World world, Entity e, float x, float y, double angleInRadians);

    float getBaseFireRate();

    float getBaseDamage();


}
