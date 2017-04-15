package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 10/04/2017.
 */

public class PlusOne implements Item{

    @Override
    public boolean applyEffect(World world, Entity e) {
        HealthComponent hc = e.getComponent(HealthComponent.class);
        hc.maxHealth = hc.maxHealth + 2;
        hc.health = (hc.health + 1 >= hc.maxHealth) ? hc.maxHealth : hc.health + 1;
        return true;
    }

    @Override
    public TextureRegion getRegion() {
        return PlayScreen.atlas.findRegion("heart", 2);
    }
}


