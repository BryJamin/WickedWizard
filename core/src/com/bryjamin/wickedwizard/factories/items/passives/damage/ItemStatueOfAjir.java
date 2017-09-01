package com.bryjamin.wickedwizard.factories.items.passives.damage;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 27/08/2017.
 */

public class ItemStatueOfAjir implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).damage += PresetStatIncrease.major;
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).shotSpeed += PresetStatIncrease.major;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Damage.statueOfAjir;
    }
}
