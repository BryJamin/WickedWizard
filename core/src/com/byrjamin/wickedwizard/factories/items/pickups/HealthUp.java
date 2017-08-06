package com.byrjamin.wickedwizard.factories.items.pickups;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 09/04/2017.
 */

public class HealthUp implements PickUp {

    @Override
    public boolean applyEffect(World world, Entity player) {
        StatComponent sc = player.getComponent(StatComponent.class);
        if(sc.health == sc.maxHealth) return false;
        sc.health = (sc.health + 1 >= sc.maxHealth) ? sc.maxHealth : sc.health + 1;
        return true;
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("item/heart", 1);
    }
}
