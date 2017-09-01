package com.bryjamin.wickedwizard.factories.items.passives.firerate;

import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemElasticity implements com.bryjamin.wickedwizard.factories.items.Item {


    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).fireRate  += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.minor;
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).range  += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.minor;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.FireRate.elasticity;
    }

}
