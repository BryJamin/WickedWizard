package com.byrjamin.wickedwizard.factories.items.passives;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 09/04/2017.
 */

public class ChangeColor implements Item {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(TextureRegionComponent.class).color = Color.CYAN;
        player.getComponent(TextureRegionComponent.class).DEFAULT = Color.CYAN;
        return true;
    }

    @Override
    public String getRegionName() {
        return "squ_dash";
    }

    @Override
    public String getName() {
        return "Color Change";
    }

    @Override
    public String getDescription() {
        return "Feeling Cyan";
    }
}
