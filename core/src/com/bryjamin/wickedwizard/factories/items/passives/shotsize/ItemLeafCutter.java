package com.bryjamin.wickedwizard.factories.items.passives.shotsize;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 03/09/2017.
 */

public class ItemLeafCutter implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        player.getComponent(StatComponent.class).fireRate += PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).shotSize -= PresetStatIncrease.major;
        player.getComponent(StatComponent.class).shotSpeed += PresetStatIncrease.major;

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.ShotSize.leafCutter;
    }

}


