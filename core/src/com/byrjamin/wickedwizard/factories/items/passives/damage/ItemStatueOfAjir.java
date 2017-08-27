package com.byrjamin.wickedwizard.factories.items.passives.damage;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 27/08/2017.
 */

public class ItemStatueOfAjir implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).damage += PresetStatIncrease.major;
        player.getComponent(StatComponent.class).shotSpeed += PresetStatIncrease.major;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Damage.statueOfAjir;
    }
}
