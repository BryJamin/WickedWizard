package com.byrjamin.wickedwizard.factories.items.passives.armor;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 27/08/2017.
 */

public class ItemAngrySlimeCoat implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        player.getComponent(StatComponent.class).armor += 2;
        player.getComponent(StatComponent.class).speed += PresetStatIncrease.Speed.minor;
        player.getComponent(StatComponent.class).damage += PresetStatIncrease.Speed.minor;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Armor.angrySlimeCoat;
    }
}
