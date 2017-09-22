package com.bryjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 20/09/2017.
 */

public class ItemTesseract implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).luck += PresetStatIncrease.massive;
        player.getComponent(StatComponent.class).shotSpeed += PresetStatIncrease.massive;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Luck.tesseract;
    }
}
