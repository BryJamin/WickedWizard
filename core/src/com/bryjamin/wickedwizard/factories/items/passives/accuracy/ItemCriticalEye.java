package com.bryjamin.wickedwizard.factories.items.passives.accuracy;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by Home on 06/08/2017.
 */

public class ItemCriticalEye implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).accuracy += PresetStatIncrease.massive;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Accuracy.CriticalEye;
    }

}
