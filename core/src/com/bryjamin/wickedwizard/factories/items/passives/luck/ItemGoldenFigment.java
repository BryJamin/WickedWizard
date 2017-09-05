package com.bryjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;


/**
 * Created by Home on 13/05/2017.
 */

public class ItemGoldenFigment implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).luck += PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).range += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Luck.goldenFigment;
    }
}
