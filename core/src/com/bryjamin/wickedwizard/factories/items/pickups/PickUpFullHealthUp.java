package com.bryjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.PickUp;

/**
 * Created by BB on 10/08/2017.
 */

public class PickUpFullHealthUp implements PickUp {

    @Override
    public boolean applyEffect(World world, Entity player) {
        com.bryjamin.wickedwizard.ecs.components.StatComponent sc = player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class);
        if(sc.health == sc.maxHealth) return false;
        sc.health = (sc.health + 2 >= sc.maxHealth) ? sc.maxHealth : sc.health + 2;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.PickUp.fullHealthUp;
    }
}

