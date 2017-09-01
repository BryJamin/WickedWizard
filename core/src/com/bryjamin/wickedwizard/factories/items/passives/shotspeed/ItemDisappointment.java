package com.bryjamin.wickedwizard.factories.items.passives.shotspeed;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 27/08/2017.
 */

public class ItemDisappointment implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).maxHealth += PresetStatIncrease.Health.increase(1);
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Health.disappointment;
    }


}
