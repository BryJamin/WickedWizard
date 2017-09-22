package com.bryjamin.wickedwizard.factories.items.passives.armor;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 20/09/2017.
 */

public class ItemTesssScarf implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        StatComponent statComponent = player.getComponent(StatComponent.class);
        statComponent.armor += 1;
        statComponent.shotSpeed += PresetStatIncrease.minor;
        statComponent.fireRate += PresetStatIncrease.minor;;
        statComponent.range += PresetStatIncrease.minor;
        statComponent.damage += PresetStatIncrease.minor;
        statComponent.accuracy += PresetStatIncrease.minor;
        statComponent.luck += PresetStatIncrease.minor;
        statComponent.crit += 1.0f;
        statComponent.increaseMaxHealth(2);
        statComponent.increaseHealth(2);
        statComponent.shotSize += PresetStatIncrease.minor;
        statComponent.speed += PresetStatIncrease.Speed.minor;

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Armor.tesssScarf;
    }
}
