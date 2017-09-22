package com.bryjamin.wickedwizard.factories.items.passives.armor;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;


/**
 * Created by Home on 13/05/2017.
 */

public class ItemSlimeCoat implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        player.getComponent(StatComponent.class).armor += 1;
        player.getComponent(StatComponent.class).speed += PresetStatIncrease.Speed.major;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Armor.slimeCoat;
    }
}
