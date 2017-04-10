package com.byrjamin.wickedwizard.factories.items;

import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 09/04/2017.
 */

public class HealthUp implements Item {

    @Override
    public boolean applyEffect(World world) {
        HealthComponent hc = world.getSystem(FindPlayerSystem.class).getPC(HealthComponent.class);
        if(hc.health == hc.maxHealth) return false;
        hc.health = (hc.health + 1 >= hc.maxHealth) ? hc.maxHealth : hc.health + 1;
        return true;
    }

    @Override
    public TextureRegion getRegion() {
        return PlayScreen.atlas.findRegion("heart");
    }
}
