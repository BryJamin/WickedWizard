package com.byrjamin.wickedwizard.factories.items;

import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 09/04/2017.
 */

public class ChangeColor implements Item {

    @Override
    public boolean applyEffect(World world) {
        TextureRegionComponent trc = world.getSystem(FindPlayerSystem.class).getPC(TextureRegionComponent.class);
        trc.color = Color.BLACK;
        return true;
    }

    @Override
    public TextureRegion getRegion() {
        return PlayScreen.atlas.findRegion("heart");
    }
}
