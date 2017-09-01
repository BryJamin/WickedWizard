package com.bryjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.PickUp;

/**
 * Created by Home on 02/08/2017.
 */

public class PickUpArmorUp implements PickUp {

    @Override
    public boolean applyEffect(World world, Entity player) {
        com.bryjamin.wickedwizard.ecs.components.StatComponent sc = player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class);
        sc.armor += 1;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.PickUp.armorUp;
    }
}
