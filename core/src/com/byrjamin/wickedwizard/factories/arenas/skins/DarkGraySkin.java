package com.byrjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Home on 29/04/2017.
 */

public class DarkGraySkin extends AbstractSkin{


    @Override
    public Color getBackgroundTint() {
        return new Color(0.5f,0.5f,0.5f,1);
    }

    @Override
    public Color getWallTint() {
        return new Color(0.3f,0.3f,0.3f,1);
    }

}
