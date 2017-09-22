package com.bryjamin.wickedwizard.factories.items.passives.speed;

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

public class ItemAbstractLeaf implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).speed += PresetStatIncrease.Speed.major;
        player.getComponent(StatComponent.class).shotSize -= PresetStatIncrease.minor;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Speed.abstractLeaf;
    }
}

