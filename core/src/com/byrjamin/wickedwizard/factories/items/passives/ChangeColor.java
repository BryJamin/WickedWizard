package com.byrjamin.wickedwizard.factories.items.passives;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.factories.items.AbstractItem;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 09/04/2017.
 */

public class ChangeColor extends AbstractItem {

    @Override
    public boolean applyEffect(World world, Entity player) {
        player.getComponent(TextureRegionComponent.class).color = new Color(Color.CYAN);
        player.getComponent(TextureRegionComponent.class).DEFAULT = new Color(Color.CYAN);
        return super.applyEffect(world, player);
    }

    @Override
    public Pair<String, Integer> getRegionName() {
        return new Pair<String, Integer>("squ_dash", 0);
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
