package com.byrjamin.wickedwizard.factories.items.passives.health;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 10/04/2017.
 */

public class Medicine implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        StatComponent sc = player.getComponent(StatComponent.class);
        int i = PresetStatIncrease.Health.increase(1);
        sc.maxHealth = sc.maxHealth + i;
        sc.health = (sc.health + i >= sc.maxHealth) ? sc.maxHealth : sc.health + i;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Health.medicine;
    }
}


