package com.byrjamin.wickedwizard.factories.items.passives.shotspeed;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 06/08/2017.
 */

public class ItemLostLettersShotSpeedAccuracy implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).shotSpeed += PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).accuracy += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/LostLettersShotSpeedAccuracy", 0);
    }

    @Override
    public String getName() {
        return "Lost Letters";
    }

    @Override
    public String getDescription() {
        return "S and A?";
    }
}