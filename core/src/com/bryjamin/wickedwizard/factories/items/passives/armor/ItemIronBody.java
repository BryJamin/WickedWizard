package com.bryjamin.wickedwizard.factories.items.passives.armor;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;


/**
 * Created by Home on 13/05/2017.
 */

public class ItemIronBody implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        StatComponent sc = player.getComponent(StatComponent.class);

        sc.maxHealth = sc.maxHealth + 2;
        sc.increaseHealth(2);
        sc.armor += 1;
        sc.speed -= PresetStatIncrease.Speed.major;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Armor.ironBody;
    }
}
