package com.bryjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;


/**
 * Created by BB on 28/08/2017.
 */

public class ItemIWishYouWell implements Item {


    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).crit += 5f;
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Luck.iWishYouWell;
    }


}