package com.bryjamin.wickedwizard.factories.items.passives.accuracy;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;

/**
 * Created by Home on 06/08/2017.
 */

public class ItemAimAssist implements com.bryjamin.wickedwizard.factories.items.Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).accuracy += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.major;
        player.getComponent(StatComponent.class).range += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.minor;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Accuracy.aimAssist;
    }

}
