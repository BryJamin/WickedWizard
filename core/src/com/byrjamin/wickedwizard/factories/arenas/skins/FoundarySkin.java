package com.byrjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Home on 28/04/2017.
 */

public class FoundarySkin extends AbstractSkin {
    public FoundarySkin(TextureAtlas atlas) {
        super(atlas);
    }


    @Override
    public Color getBackgroundTint() {
        return new Color(1,0,0,1);
    }

    @Override
    public Color getWallTint() {
        return new Color(1,0,0,1);
    }
}
