package com.byrjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Home on 28/04/2017.
 */

public class FoundarySkin extends AbstractSkin {


    @Override
    public Color getBackgroundTint() {
        return new Color(176/255f, 196/255f, 222/255f, 1);
    }

    //999999

    @Override
    public Color getWallTint() {
        return new Color(66/255f, 99/255f, 99/255f, 1);
    }
}
