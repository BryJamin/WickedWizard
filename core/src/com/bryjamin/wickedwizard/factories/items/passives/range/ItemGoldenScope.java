package com.bryjamin.wickedwizard.factories.items.passives.range;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 15/09/2017.
 */

public class ItemGoldenScope implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).range += PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).luck += PresetStatIncrease.minor;
        return true;
    }
    @Override
    public ItemLayout getValues() {
        return ItemResource.Range.goldenScope;
    }
}
