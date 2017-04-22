package com.byrjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.screens.PlayScreen;

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
    public TextureRegion getRegion() {
        return PlayScreen.atlas.findRegion("explosion", 1);
    }
}
