package com.byrjamin.wickedwizard.factories.items.passives.armor;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemSquareBuckler implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        player.getComponent(StatComponent.class).armor += 2;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Armor.squareBuckler;
    }



}
