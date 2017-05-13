package com.byrjamin.wickedwizard.factories.items.passives.firerate;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.AbstractItem;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemTacticalKnitwear extends AbstractItem {


    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).fireRate  += PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).accuracy  += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/TacticalKnitwear", 0);
    }

    @Override
    public String getName() {
        return "Tactical Knitwear";
    }

    @Override
    public String getDescription() {
        return "FireRate+ Accuracy+";
    }
}
