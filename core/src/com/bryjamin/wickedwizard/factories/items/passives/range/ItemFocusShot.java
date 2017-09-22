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

public class ItemFocusShot implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).range += PresetStatIncrease.massive;
        player.getComponent(StatComponent.class).fireRate -= PresetStatIncrease.major;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Range.focusShot;
    }

}

