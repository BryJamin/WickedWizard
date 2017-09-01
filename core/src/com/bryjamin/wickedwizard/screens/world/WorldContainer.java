package com.bryjamin.wickedwizard.screens.world;

import com.artemis.World;

/**
 * Created by Home on 28/07/2017.
 */

public interface WorldContainer {

    void createWorld();
    void process(float delta);
    World getWorld();


}
