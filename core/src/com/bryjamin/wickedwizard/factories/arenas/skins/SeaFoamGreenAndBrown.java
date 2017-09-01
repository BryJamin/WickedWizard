package com.bryjamin.wickedwizard.factories.arenas.skins;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by BB on 31/08/2017.
 */

public class SeaFoamGreenAndBrown extends AbstractSkin{


    @Override
    public Color getBackgroundTint() {
        return Color.valueOf("#B8E0C1"); //new Color(0.4f,0.4f,0.4f,1);
    }

    @Override
    public Color getWallTint() {
        return  Color.valueOf("#4F3D2E"); //Color.valueOf("#725843"); //new Color(0.2f,0.2f,0.2f,1);
    }

}
