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

public class ItemDilation implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).increaseMaxHealth(2);
        player.getComponent(StatComponent.class).shotSize += PresetStatIncrease.major;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Health.dilation;
    }


}
