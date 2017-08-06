package com.byrjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemJadeFigment implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).luck += PresetStatIncrease.major;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/JadeScarab", 0);
    }

    @Override
    public String getName() {
        return "Jade Figment";
    }

    @Override
    public String getDescription() {
        return "Luck++";
    }
}