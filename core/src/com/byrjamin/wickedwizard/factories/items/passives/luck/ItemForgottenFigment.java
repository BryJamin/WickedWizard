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

public class ItemForgottenFigment implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).luck -= PresetStatIncrease.major;
        player.getComponent(StatComponent.class).damage += PresetStatIncrease.major;
        player.getComponent(StatComponent.class).fireRate += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/ForgottenScarab", 0);
    }

    @Override
    public String getName() {
        return "Forgotten Figment";
    }

    @Override
    public String getDescription() {
        return "Something feels off...";
    }
}
