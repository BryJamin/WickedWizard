package com.byrjamin.wickedwizard.factories.items.passives.speed;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemQuickness implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).speed += PresetStatIncrease.Speed.massive;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Speed.quickness;
    }
}
