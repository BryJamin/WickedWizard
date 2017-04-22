package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 09/04/2017.
 */

public interface Item extends PickUp {


    String getName();
    String getDescription();


}
