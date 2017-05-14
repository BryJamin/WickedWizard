package com.byrjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemThreeLeafClover implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        HealthComponent hc = player.getComponent(HealthComponent.class);
        hc.maxHealth = hc.maxHealth + 2;

        player.getComponent(StatComponent.class).luck += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/ThreeLeafClover", 0);
    }

    @Override
    public String getName() {
        return "Three Leaf Clover";
    }

    @Override
    public String getDescription() {
        return "Close Enough... Luck+";
    }
}
