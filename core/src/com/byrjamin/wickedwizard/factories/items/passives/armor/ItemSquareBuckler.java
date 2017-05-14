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

public class ItemSquareBuckler implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        //TODO increase Speed and 1Armor
        player.getComponent(StatComponent.class).armor += 2;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/SquareBuckler", 0);
    }

    @Override
    public String getName() {
        return "Square Buckler";
    }

    @Override
    public String getDescription() {
        return "Good for two hits";
    }
}
