package com.bryjamin.wickedwizard.factories.items.passives.armor;

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

//TODO maybe tweak into an item that monitors current armor

public class ItemXisOldScarf implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).armor += 1;
        player.getComponent(StatComponent.class).fireRate += PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).shotSpeed += PresetStatIncrease.minor;

        for(int i = 0; i < player.getComponent(StatComponent.class).armor; i++){
            player.getComponent(StatComponent.class).crit+=  2.0f;
        }

        return true;
    }

    @Override
    public ItemLayout getValues() {
        return ItemResource.Armor.xisOldScarf;
    }
}