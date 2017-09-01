package com.bryjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 09/04/2017.
 */

public interface Item extends PickUp {

    @Override
    boolean applyEffect(World world, Entity player);

    @Override
    ItemResource.ItemValues getValues();

}
