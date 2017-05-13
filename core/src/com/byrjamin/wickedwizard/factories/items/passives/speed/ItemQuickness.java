package com.byrjamin.wickedwizard.factories.items.passives.speed;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemQuickness implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).speed += PresetStatIncrease.Speed.major;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/Quickness", 0);
    }

    @Override
    public String getName() {
        return "Quickness";
    }

    @Override
    public String getDescription() {
        return "Speed+";
    }
}
