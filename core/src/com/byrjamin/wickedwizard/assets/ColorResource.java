package com.byrjamin.wickedwizard.assets;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Home on 25/06/2017.
 */

public class ColorResource {


    public static final Color AMOEBA_BLUE = new Color(59f / 255f, 134f / 255f, 134f / 255f, 1);


    public static final Color GHOST_BULLET_COLOR = new Color(147f / 255f, 76f / 255f, 206f / 255f, 1f);
    public static final Color ENEMY_BULLET_COLOR = new Color(Color.RED);




    public static Color RGBtoColor(float r, float g, float b, float a){
        return new Color(r / 255f, g / 255f, b / 255f, a);
    }


}
