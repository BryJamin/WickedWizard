package com.byrjamin.wickedwizard.factories.items.passives.shotsize;

import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemResource;
import com.byrjamin.wickedwizard.factories.items.passives.PresetStatIncrease;

/**
 * Created by BB on 28/08/2017.
 */

public class CannonCube implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).damage += PresetStatIncrease.massive;
        player.getComponent(StatComponent.class).shotSize += PresetStatIncrease.major;


        player.getComponent(StatComponent.class).fireRate -= PresetStatIncrease.minor;
        player.getComponent(StatComponent.class).shotSpeed -= PresetStatIncrease.minor;


        return true;
    }

    @Override
    public ItemResource.ItemValues getValues() {
        return ItemResource.Range.quadonometry;
    }
}
