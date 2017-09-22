package com.bryjamin.wickedwizard.factories.items.passives.firerate;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;


/**
 * Created by BB on 27/08/2017.
 */

public class ItemStatueOfAdoj implements Item {


    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).fireRate  += PresetStatIncrease.major;
        player.getComponent(StatComponent.class).shotSpeed  += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.FireRate.statueOfAdoj;
    }

}