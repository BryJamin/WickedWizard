package com.bryjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;

/**
 * Created by BB on 20/09/2017.
 */

public class ItemLuckPlus10 implements Item {


    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).luck += 10f;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Luck.luckPlus10;
    }


}
