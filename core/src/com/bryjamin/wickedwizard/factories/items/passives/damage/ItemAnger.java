package com.bryjamin.wickedwizard.factories.items.passives.damage;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;


/**
 * Created by Home on 15/04/2017.
 */

public class ItemAnger implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).damage += PresetStatIncrease.major;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Damage.anger;
    }
}
