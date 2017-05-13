package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 09/04/2017.
 */

public interface Item extends PickUp {

    @Override
    boolean applyEffect(World world, Entity player);

    @Override
    Pair<String, Integer> getRegionName();

    String getName();
    String getDescription();


}
