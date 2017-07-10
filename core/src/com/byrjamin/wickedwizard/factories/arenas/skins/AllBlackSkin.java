package com.byrjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Home on 09/07/2017.
 */

public class AllBlackSkin extends AbstractSkin{


    public AllBlackSkin(TextureAtlas atlas) {
        super(atlas);
    }

    @Override
    public Color getBackgroundTint() {
        return new Color(Color.BLACK);
    }

    @Override
    public Color getWallTint() {
        return new Color(Color.BLACK);
    }

}
