package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 09/04/2017.
 */

public interface PickUp {

    boolean applyEffect(World world, Entity e);
    TextureRegion getRegion();

}
