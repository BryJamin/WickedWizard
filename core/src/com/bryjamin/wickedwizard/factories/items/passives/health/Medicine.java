package com.bryjamin.wickedwizard.factories.items.passives.health;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.Item;

/**
 * Created by Home on 10/04/2017.
 */

public class Medicine implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        com.bryjamin.wickedwizard.ecs.components.StatComponent sc = player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class);
        int i = com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.Health.increase(1);
        sc.maxHealth = sc.maxHealth + i;
        sc.health = (sc.health + i >= sc.maxHealth) ? sc.maxHealth : sc.health + i;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Health.medicine;
    }
}


