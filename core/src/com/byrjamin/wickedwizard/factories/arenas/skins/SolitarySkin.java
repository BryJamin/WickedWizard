package com.byrjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 28/04/2017.
 */

public class SolitarySkin extends AbstractSkin {

    public SolitarySkin(TextureAtlas atlas) {
        super(atlas);
    }

    @Override
    public Array<? extends TextureRegion> getWallTexture() {
        return atlas.findRegions("brick");
    }

}
