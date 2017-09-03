package com.bryjamin.wickedwizard.factories.items.passives.health;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

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
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Health.medicine;
    }
}


