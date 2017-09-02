package com.bryjamin.wickedwizard.factories.items.passives.armor;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;

/**
 * Created by BB on 27/08/2017.
 */

public class ItemAngrySlimeCoat implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        player.getComponent(StatComponent.class).armor += 2;
        player.getComponent(StatComponent.class).speed += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.Speed.minor;
        player.getComponent(StatComponent.class).damage += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.Speed.minor;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Armor.angrySlimeCoat;
    }
}
