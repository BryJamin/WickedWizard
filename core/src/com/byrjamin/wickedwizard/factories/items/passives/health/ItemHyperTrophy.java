package com.byrjamin.wickedwizard.factories.items.passives.health;

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

public class ItemHyperTrophy implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        HealthComponent hc = player.getComponent(HealthComponent.class);
        hc.maxHealth = hc.maxHealth + 1;
        hc.health = (hc.health + 1 >= hc.maxHealth) ? hc.maxHealth : hc.health + 2;

        player.getComponent(StatComponent.class).speed -= PresetStatIncrease.Speed.major;
        player.getComponent(StatComponent.class).damage += PresetStatIncrease.minor;

        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/Hypertrophy", 0);
    }

    @Override
    public String getName() {
        return "Hypertrophy";
    }

    @Override
    public String getDescription() {
        return "Health+ Speed- Damage+";
    }
}
