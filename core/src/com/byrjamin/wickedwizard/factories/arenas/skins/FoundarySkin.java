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
        return new Color(204/255f, 255/255f, 255/255f, 1);
    }

    //999999

    @Override
    public Color getWallTint() {
        return new Color(66/255f, 99/255f, 99/255f, 1);
    }
}
