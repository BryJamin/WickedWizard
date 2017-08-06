package com.byrjamin.wickedwizard.factories.items.passives.health;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 06/08/2017.
 */

public class ItemSarcasticLion implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        StatComponent sc = player.getComponent(StatComponent.class);
        sc.maxHealth = sc.maxHealth + PresetStatIncrease.Health.increase(1);
        sc.health = (sc.health + 1 >= sc.maxHealth) ? sc.maxHealth : sc.health + PresetStatIncrease.Health.increase(1);
        sc.fireRate += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/SarcasticLion", 0);
    }

    @Override
    public String getName() {
        return "Sarcastic Lion";
    }

    @Override
    public String getDescription() {
        return "Rawr";
    }
}
