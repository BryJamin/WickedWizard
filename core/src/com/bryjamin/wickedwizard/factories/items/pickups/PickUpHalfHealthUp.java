package com.bryjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.PickUp;

/**
 * Created by Home on 09/04/2017.
 */

public class PickUpHalfHealthUp implements PickUp {

    @Override
    public boolean applyEffect(World world, Entity player) {
        com.bryjamin.wickedwizard.ecs.components.StatComponent sc = player.getComponent(StatComponent.class);
        if(sc.getHealth() == sc.maxHealth) return false;
        sc.increaseHealth(1);
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.PickUp.healthUp;
    }
}
