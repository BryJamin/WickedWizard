package com.byrjamin.wickedwizard.factories.items.passives.health;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 10/04/2017.
 */

public class Medicine implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        HealthComponent hc = player.getComponent(HealthComponent.class);
        hc.maxHealth = hc.maxHealth + 2;
        hc.health = (hc.health + 1 >= hc.maxHealth) ? hc.maxHealth : hc.health + 2;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/Medicine", 0);
    }

    @Override
    public String getName() {
        return "Medicine";
    }

    @Override
    public String getDescription() {
        return "Health +";
    }
}


