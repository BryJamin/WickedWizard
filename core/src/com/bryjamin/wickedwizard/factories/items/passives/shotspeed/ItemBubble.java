package com.bryjamin.wickedwizard.factories.items.passives.shotspeed;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by Home on 06/08/2017.
 */

public class ItemBubble implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).shotSpeed += PresetStatIncrease.major;
        player.getComponent(StatComponent.class).armor += 1;
        return true;
    }
    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.ShotSpeed.bubble;
    }
}
