package com.byrjamin.wickedwizard.factories.items.passives;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 10/04/2017.
 */

public class PlusOne implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        HealthComponent hc = player.getComponent(HealthComponent.class);
        hc.maxHealth = hc.maxHealth + 2;
        hc.health = (hc.health + 1 >= hc.maxHealth) ? hc.maxHealth : hc.health + 1;
        return true;
    }

    @Override
    public String getRegionName() {
        return "heart";
    }

    @Override
    public String getName() {
        return "Empty Heart";
    }

    @Override
    public String getDescription() {
        return "Health Up!";
    }
}


