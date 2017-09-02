package com.bryjamin.wickedwizard.factories.items.passives.speed;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemQuickness implements com.bryjamin.wickedwizard.factories.items.Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).speed += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.Speed.massive;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Speed.quickness;
    }
}
