package com.bryjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.bryjamin.wickedwizard.assets.ColorResource;

/**
 * Created by BB on 29/08/2017.
 */




public class FieldSkin extends AbstractSkin {

    private Color background = ColorResource.RGBtoColor(103, 208, 234, 1);
    private Color foreground = ColorResource.RGBtoColor(232, 233, 243, 1);

    @Override
    public Color getBackgroundTint() {
        return foreground;
    }

    @Override
    public Color getWallTint() {
        return background;
    }





}
