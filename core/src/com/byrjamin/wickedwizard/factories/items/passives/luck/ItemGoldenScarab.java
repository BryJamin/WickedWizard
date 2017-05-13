package com.byrjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemGoldenScarab implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        CurrencyComponent cc = player.getComponent(CurrencyComponent.class);
        cc.money += 5;
        player.getComponent(StatComponent.class).luck += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/GoldenScarab", 0);
    }

    @Override
    public String getName() {
        return "Golden Scarab";
    }

    @Override
    public String getDescription() {
        return "This should be worth something. Luck+";
    }
}
