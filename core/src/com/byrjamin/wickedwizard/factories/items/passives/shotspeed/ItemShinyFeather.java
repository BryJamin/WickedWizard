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

public class ItemShinyFeather implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).shotSpeed += PresetStatIncrease.major;
        player.getComponent(StatComponent.class).speed += PresetStatIncrease.Speed.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/ShinyFeather", 0);
    }

    @Override
    public String getName() {
        return "Shiny Feather";
    }

    @Override
    public String getDescription() {
        return "ShotSpeed++ Speed+";
    }
}
