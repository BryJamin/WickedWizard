package com.byrjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Home on 29/04/2017.
 */

public class FreedomSkin extends AbstractSkin {

    public FreedomSkin(TextureAtlas atlas) {
        super(atlas);
    }


    @Override
    public Color getBackgroundTint() {
        return new Color(0.5f,0.5f,1,0.5f);
    }

    @Override
    public Color getWallTint() {
        return new Color(0.8f,0.8f,1,0.5f);
    }





}
