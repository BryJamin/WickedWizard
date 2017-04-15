package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 15/04/2017.
 */

public class DamageUp implements Item {

    @Override
    public boolean applyEffect(World world, Entity e) {
        e.getComponent(StatComponent.class).damage += 1f;
        return true;
    }

    @Override
    public TextureRegion getRegion() {
        return PlayScreen.atlas.findRegion("fire");
    }
}
