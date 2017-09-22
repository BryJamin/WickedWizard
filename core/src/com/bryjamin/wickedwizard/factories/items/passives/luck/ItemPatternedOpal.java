package com.bryjamin.wickedwizard.factories.items.passives.luck;

/**
 * Created by BB on 03/09/2017.
 */

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

public class ItemPatternedOpal implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).luck += PresetStatIncrease.minor;
        player.getComponent(CurrencyComponent.class).money += 5;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Luck.patternedOpal;
    }
}
