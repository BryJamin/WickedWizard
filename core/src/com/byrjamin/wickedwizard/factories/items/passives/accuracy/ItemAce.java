package com.byrjamin.wickedwizard.factories.items.passives.accuracy;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemAce implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).accuracy += PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).luck += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/Ace", 0);
    }

    @Override
    public String getName() {
        return "Ace";
    }

    @Override
    public String getDescription() {
        return "Accuracy+ Luck+";
    }

}