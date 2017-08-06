package com.byrjamin.wickedwizard.factories.items.passives.firerate;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by Home on 16/04/2017.
 */

public class ItemSwiftShot implements Item {


    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).fireRate  += PresetStatIncrease.major;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.FireRate.swiftShot;
    }

}
