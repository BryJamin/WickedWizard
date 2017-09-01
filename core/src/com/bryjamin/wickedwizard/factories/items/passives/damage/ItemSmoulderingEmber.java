package com.bryjamin.wickedwizard.factories.items.passives.damage;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.Item;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemSmoulderingEmber implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).damage += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.minor;
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).fireRate += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.minor;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Damage.smoulderingEmber;
    }
}
