package com.bryjamin.wickedwizard.factories.items.passives.damage.rankTwo;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by Home on 13/05/2017.
 */

//TODO this will most likely be an unlock as it is an upgrade to the catapult
public class ItemMiniTrebuchet implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).damage += PresetStatIncrease.major;
        player.getComponent(StatComponent.class).range += PresetStatIncrease.major;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Damage.miniTrebuchet;
    }

}

