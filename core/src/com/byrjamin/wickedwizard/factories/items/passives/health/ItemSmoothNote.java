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

public class ItemSmoothNote implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        StatComponent sc = player.getComponent(StatComponent.class);
        sc.maxHealth = sc.maxHealth + PresetStatIncrease.Health.increase(1);
        sc.health = (sc.health + PresetStatIncrease.Health.increase(2) >= sc.maxHealth) ? sc.maxHealth : sc.health + PresetStatIncrease.Health.increase(2);
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/SootheNote", 0);
    }

    @Override
    public String getName() {
        return "Soothe Note";
    }

    @Override
    public String getDescription() {
        return "Soothing";
    }
}
