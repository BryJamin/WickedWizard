package com.bryjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Home on 29/04/2017.
 */

public class Bourbon extends AbstractSkin{


    @Override
    public Color getBackgroundTint() {
        return new Color(213f / 255f, 196f / 255f, 144f / 255f,1);
    }

    @Override
    public Color getWallTint() {
        return new Color(165f / 255f, 69f / 255f, 43f / 255f,1);
    }

}
