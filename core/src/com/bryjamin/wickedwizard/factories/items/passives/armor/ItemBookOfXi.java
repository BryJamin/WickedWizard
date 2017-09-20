package com.bryjamin.wickedwizard.factories.items.passives.armor;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.ItemLayout;
import com.bryjamin.wickedwizard.factories.items.ItemResource;

/**
 * Created by BB on 20/09/2017.
 */

public class ItemBookOfXi implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {

        StatComponent statComponent = player.getComponent(StatComponent.class);

        for(int i = 0; i < 3; i++){
            if(statComponent.getHealth() > 1){
                statComponent.increaseHealth(-1);
                statComponent.armor += 1;
            } else {
                break;
            }
        }
        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Armor.bookOfXi;
    }
}
