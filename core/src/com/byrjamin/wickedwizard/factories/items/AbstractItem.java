package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.systems.PickUpSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public abstract class AbstractItem implements Item {


    @Override
    public String getName() {
        return "PlaceHolder";
    }

    @Override
    public String getDescription() {
        return "PlaceHolder";
    }

    @Override
    public boolean applyEffect(World world, Entity player) {
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("block", 0);
    }
}
