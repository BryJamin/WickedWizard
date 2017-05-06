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
        return atlas.findRegions("block");
    }


    @Override
    public Array<? extends TextureRegion> getBackgroundTextures() {
        return atlas.findRegions("block");
    }

/*    @Override
    public Color getBackgroundTint() {
        return new Color(0.3f,0.3f,0.3f,1);
    }

    @Override
    public Color getWallTint() {
        return new Color(0.4f,0.4f,0.4f,1);
    }*/





    @Override
    public Color getBackgroundTint() {
        return new Color(204/255f, 255/255f, 255/255f, 1);
    }

    //999999

    @Override
    public Color getWallTint() {
        return new Color(66/255f, 99/255f, 99/255f, 1);
    }








}
