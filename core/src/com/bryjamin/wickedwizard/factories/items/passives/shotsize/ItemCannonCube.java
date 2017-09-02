package com.bryjamin.wickedwizard.factories.items.passives.shotsize;

import com.artemis.Entity;
import com.artemis.World;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.factories.items.Item;

/**
 * Created by BB on 28/08/2017.
 */

public class ItemCannonCube implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).damage += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.massive;
        player.getComponent(StatComponent.class).shotSize += com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.massive;


        player.getComponent(StatComponent.class).fireRate -= com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).shotSpeed -= com.bryjamin.wickedwizard.factories.items.passives.PresetStatIncrease.minor;


        return true;
    }

    @Override
    public com.bryjamin.wickedwizard.factories.items.ItemResource.ItemValues getValues() {
        return com.bryjamin.wickedwizard.factories.items.ItemResource.ShotSize.cannonCube;
    }
}
