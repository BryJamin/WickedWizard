package com.bryjamin.wickedwizard.factories.items.passives.shotspeed;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;


/**
 * Created by Home on 05/08/2017.
 */

public class ItemMomentum implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).shotSpeed += PresetStatIncrease.major;
        player.getComponent(StatComponent.class).speed += PresetStatIncrease.Speed.minor;
        player.getComponent(StatComponent.class).damage += PresetStatIncrease.major;
        return true;
    }
    @Override
    public ItemLayout getValues() {
        return ItemResource.ShotSpeed.momentum;
    }
}
