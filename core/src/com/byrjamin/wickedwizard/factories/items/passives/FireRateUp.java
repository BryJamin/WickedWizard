package com.byrjamin.wickedwizard.factories.items.passives;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 16/04/2017.
 */

public class FireRateUp implements Item {


    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(StatComponent.class).fireRate += 1f;
        return true;
    }

    @Override
    public String getRegionName() {
        return "bullet_red";
    }

    @Override
    public String getName() {
        return "Red Sphere";
    }

    @Override
    public String getDescription() {
        return "Fire Rate Up!";
    }
}
