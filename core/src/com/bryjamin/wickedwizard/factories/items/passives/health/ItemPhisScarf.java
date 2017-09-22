package com.bryjamin.wickedwizard.factories.items.passives.health;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 19/09/2017.
 */

public class ItemPhisScarf implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        StatComponent sc = player.getComponent(StatComponent.class);
        sc.increaseMaxHealth(4);
        sc.increaseHealth(4);

        sc.speed -= PresetStatIncrease.Speed.major;
        sc.damage -= PresetStatIncrease.major;

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Health.phisScarf;
    }
}
