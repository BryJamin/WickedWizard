package com.bryjamin.wickedwizard.factories.items.passives.armor;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.Item;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemVitaminC implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).armor += 1;
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).speed += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.Speed.minor;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Armor.vitaminC;
    }

}