package com.bryjamin.wickedwizard.factories.arenas.skins;

/**
 * Created by Home on 19/06/2017.
 */

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Home on 07/05/2017.
 */

public class BrightWhiteSkin extends  AbstractSkin{

    private Color background = new Color(216f/255f, 222/255f, 222/255f, 1);
    private Color wall = new Color(252f/255f, 251f/255f, 249/ 255f, 1);


    @Override
    public Color getBackgroundTint() {
        return background;
    }

    //999999

    @Override
    public Color getWallTint() {
        return wall;
    }



}
