package com.byrjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.PickUp;

/**
 * Created by BB on 10/08/2017.
 */

public class PickUpFullHealthUp implements PickUp {

    @Override
    public boolean applyEffect(World world, Entity player) {
        StatComponent sc = player.getComponent(StatComponent.class);
        if(sc.health == sc.maxHealth) return false;
        sc.health = (sc.health + 2 >= sc.maxHealth) ? sc.maxHealth : sc.health + 2;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.PickUp.fullHealthUp;
    }
}

