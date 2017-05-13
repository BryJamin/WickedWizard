package com.byrjamin.wickedwizard.factories.items.passives.damage;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemStability implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).damage += PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).accuracy += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/Stability", 0);
    }

    @Override
    public String getName() {
        return "Stability";
    }

    @Override
    public String getDescription() {
        return "Damage+ Accuracy+";
    }
}
