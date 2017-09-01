package com.bryjamin.wickedwizard.factories.items.passives.luck;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.factories.items.Item;
import com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by Home on 13/05/2017.
 */

public class ItemForgottenFigment implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).luck -= PresetStatIncrease.minor;
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).damage += PresetStatIncrease.major;
        player.getComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).fireRate += PresetStatIncrease.minor;
        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.Luck.forgottenFigment;
    }


}
