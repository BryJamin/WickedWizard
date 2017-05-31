package com.byrjamin.wickedwizard.factories.items.passives.armor;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemSlimeCoat implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        //TODO increase Speed and 1Armor
        player.getComponent(StatComponent.class).armor += 1;
        player.getComponent(StatComponent.class).speed += PresetStatIncrease.Speed.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("blob", 2);
    }

    @Override
    public String getName() {
        return "Slime Coat";
    }

    @Override
    public String getDescription() {
        return "Eww..";
    }
}
