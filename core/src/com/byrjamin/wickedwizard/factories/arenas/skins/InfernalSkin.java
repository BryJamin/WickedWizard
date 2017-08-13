package com.byrjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.byrjamin.wickedwizard.assets.ColorResource;

/**
 * Created by BB on 13/08/2017.
 */

public class InfernalSkin extends AbstractSkin {



    @Override
    public Color getBackgroundTint() {
        return ColorResource.RGBtoColor(150, 0 , 0, 1);
    }

    @Override
    public Color getWallTint() {
        return ColorResource.RGBtoColor(0, 0 , 0, 1);
    }





}

