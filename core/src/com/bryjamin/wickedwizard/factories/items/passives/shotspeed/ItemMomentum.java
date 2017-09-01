package com.bryjamin.wickedwizard.factories.items.passives.shotspeed;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by Home on 05/08/2017.
 */

public class ItemMomentum implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).shotSpeed += PresetStatIncrease.major;
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).speed += PresetStatIncrease.Speed.minor;
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).damage += PresetStatIncrease.major;
        return true;
    }
    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.ShotSpeed.momentum;
    }
}
