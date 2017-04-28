package com.byrjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 20/04/2017.
 */

public class KeyUp implements PickUp {
    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(CurrencyComponent.class).keys += 1;
        return true;
    }

    @Override
    public String getRegionName() {
        return "key";
    }
}
