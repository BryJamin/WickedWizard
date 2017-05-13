package com.byrjamin.wickedwizard.factories.items.passives.damage;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemTrigonometry implements Item {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean applyEffect(World world, Entity player) {
        return false;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return null;
    }
}
