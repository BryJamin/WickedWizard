package com.byrjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 28/04/2017.
 */

public interface ArenaSkin {

    Array<? extends TextureRegion> getBackgroundTextures();
    Color getBackgroundTint();

    Array<? extends  TextureRegion> getWallTexture();
    Color getWallTint();



}
