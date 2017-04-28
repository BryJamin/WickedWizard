package com.byrjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 28/04/2017.
 */

public abstract class AbstractSkin implements ArenaSkin{

    protected TextureAtlas atlas;

    public AbstractSkin(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    public Array<? extends TextureRegion> getBackgroundTextures() {
        return atlas.findRegions("backgrounds/wall");
    }

    @Override
    public Color getBackgroundTint() {
        return Color.WHITE;
    }

    @Override
    public Array<? extends TextureRegion> getWallTexture() {
        return atlas.findRegions("brick");
    }

    @Override
    public Color getWallTint() {
        return Color.WHITE;
    }
}
