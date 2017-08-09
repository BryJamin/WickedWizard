package com.byrjamin.wickedwizard.screens.world;

import com.artemis.World;

/**
 * Created by Home on 28/07/2017.
 */

public abstract class WorldContainer {

    protected World world;


    public void process(float delta) {
        world.setDelta(delta < 0.030f ? delta : 0.030f);
        world.process();
    }

    public abstract World getWorld();


}
