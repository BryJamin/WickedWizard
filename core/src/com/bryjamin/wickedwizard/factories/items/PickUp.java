package com.bryjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 09/04/2017.
 */

public interface PickUp {

    /**
     * Applies an affect to the given entity (Most likely the player)
     * @param world - The current world instance. Items can affect more than just the player
     * @param player - The Entity the item can be applied to.
     * @return - Returns true if the item can be used/picked up by the entity.
     */
    boolean applyEffect(World world, Entity player);
    ItemLayout getValues();

}
