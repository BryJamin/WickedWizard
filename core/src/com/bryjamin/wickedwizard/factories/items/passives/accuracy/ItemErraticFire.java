package com.bryjamin.wickedwizard.factories.items.passives.accuracy;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 16/09/2017.
 */

public class ItemErraticFire implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).accuracy -= PresetStatIncrease.massive;
        player.getComponent(StatComponent.class).fireRate += PresetStatIncrease.massive;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Accuracy.erraticFire;
    }

}

