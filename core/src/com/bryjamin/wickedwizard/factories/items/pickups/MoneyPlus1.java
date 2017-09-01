package com.bryjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.factories.items.ItemResource;
import com.bryjamin.wickedwizard.factories.items.PickUp;

/**
 * Created by Home on 18/04/2017.
 */

public class MoneyPlus1 implements PickUp {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(CurrencyComponent.class).money += 1;
        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.PickUp.moneyUp;
    }
}
