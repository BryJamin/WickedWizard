package com.bryjamin.wickedwizard.utils;

import com.artemis.World;

/**
 * Created by BB on 17/08/2017.
 */

public class GameDelta {


    public static void delta(World world, float delta){
        world.setDelta(delta < 0.030f ? delta : 0.030f);
        world.process();
    }


}
