package com.bryjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Home on 09/07/2017.
 */

public class AllBlackSkin extends AbstractSkin{

    @Override
    public Color getBackgroundTint() {
        return new Color(Color.BLACK);
    }

    @Override
    public Color getWallTint() {
        return new Color(Color.BLACK);
    }

}
