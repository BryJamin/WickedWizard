package com.bryjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.bryjamin.wickedwizard.assets.TextureStrings;


/**
 * Created by Home on 28/04/2017.
 */

public abstract class AbstractSkin implements ArenaSkin {

    public AbstractSkin() {
    }

    @Override
    public String getBackgroundTextures() {
        return TextureStrings.BLOCK;
    }

    @Override
    public Color getBackgroundTint() {
        return Color.WHITE;
    }

    @Override
    public String getWallTexture() {
        return TextureStrings.BLOCK;
    }

    @Override
    public Color getWallTint() {
        return Color.WHITE;
    }
}
