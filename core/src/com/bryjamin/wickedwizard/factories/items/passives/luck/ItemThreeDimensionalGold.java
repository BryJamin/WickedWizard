package com.bryjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.factories.items.Item;

/**
 * Created by BB on 27/08/2017.
 */

public class ItemThreeDimensionalGold implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(CurrencyComponent.class).money += 15;
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).luck += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.minor;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Luck.threeDimensionalGold;
    }
}
