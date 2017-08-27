package com.byrjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 27/08/2017.
 */

public class ItemThreeDimensionalGold implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(CurrencyComponent.class).money = 15;
        player.getComponent(StatComponent.class).luck += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Luck.threeDimensionalGold;
    }
}
