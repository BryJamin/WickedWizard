package com.byrjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.ItemResource;

/**
 * Created by Home on 02/08/2017.
 */

public class PickUpArmorUp implements com.byrjamin.wickedwizard.factories.items.PickUp {

    @Override
    public boolean applyEffect(World world, Entity player) {
        StatComponent sc = player.getComponent(StatComponent.class);
        sc.armor += 1;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.PickUp.armorUp;
    }
}
