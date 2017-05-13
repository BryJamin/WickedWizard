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

public class ItemVitaminC implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        //TODO increase Speed and 1Armor
        player.getComponent(StatComponent.class).armor += 1;
        player.getComponent(StatComponent.class).speed += PresetStatIncrease.Speed.major;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/VitaminC", 0);
    }

    @Override
    public String getName() {
        return "Vitamin C";
    }

    @Override
    public String getDescription() {
        return "You no longer have Scurvy";
    }
}