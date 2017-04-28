package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 28/04/2017.
 */

public interface ArenaBuildScheme {

    Array<TextureRegion> getBackgroundTextures();
    Color getBackgroundTint();

    TextureRegion getWallTexture();
    Color getWallTint();



}
