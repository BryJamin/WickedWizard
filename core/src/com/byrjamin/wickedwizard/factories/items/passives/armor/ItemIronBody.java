package com.byrjamin.wickedwizard.factories.items.passives.armor;

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

public class ItemIronBody implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        HealthComponent hc = player.getComponent(HealthComponent.class);
        hc.maxHealth = hc.maxHealth + 2;
        hc.health = (hc.health + 1 >= hc.maxHealth) ? hc.maxHealth : hc.health + 2;

        //TODO increase Speed and 1Armor
        player.getComponent(StatComponent.class).armor += 1;
        player.getComponent(StatComponent.class).speed -= PresetStatIncrease.Speed.major;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/IronBody", 0);
    }

    @Override
    public String getName() {
        return "Iron Body";
    }

    @Override
    public String getDescription() {
        return "You feel heavier";
    }
}
