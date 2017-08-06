package com.byrjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 09/04/2017.
 */

public class PickUpHalfHealthUp implements PickUp {

    @Override
    public boolean applyEffect(World world, Entity player) {
        StatComponent sc = player.getComponent(StatComponent.class);
        if(sc.health == sc.maxHealth) return false;
        sc.health = (sc.health + 1 >= sc.maxHealth) ? sc.maxHealth : sc.health + 1;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.PickUp.healthUp;
    }
}
