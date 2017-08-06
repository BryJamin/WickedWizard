package com.byrjamin.wickedwizard.factories.items.passives.range;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 05/08/2017.
 */

public class ItemLostLettersRangeFireRate implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).range += PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).fireRate += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/LostLettersRangeFireRate", 0);
    }

    @Override
    public String getName() {
        return "Lost Letters";
    }

    @Override
    public String getDescription() {
        return "R and F?";
    }
}

