package com.bryjamin.wickedwizard.factories.items.passives.health;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;


/**
 * Created by Home on 06/08/2017.
 */

public class ItemSarcasticLion implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        com.bryjamin.wickedwizard.ecs.components.StatComponent sc = player.getComponent(StatComponent.class);
        sc.maxHealth = sc.maxHealth + PresetStatIncrease.Health.increase(1);
        sc.increaseHealth(2);
        sc.fireRate += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Health.sarcasticLion;
    }
}
