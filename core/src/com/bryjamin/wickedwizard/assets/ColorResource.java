package com.bryjamin.wickedwizard.assets;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Home on 25/06/2017.
 */

public class ColorResource {


    public static final Color AMOEBA_BLUE = RGBtoColor(59, 134, 134, 1);
    public static final Color AMOEBA_FAST_PURPLE = RGBtoColor(98, 59, 206, 1);
    public static final Color GHOST_BULLET_COLOR = RGBtoColor(194, 73, 255, 0.9f);
    public static final Color ENEMY_BULLET_COLOR = new Color(Color.RED);

    public static final Color PLAYER_BULLET_COLOR = new Color(Color.WHITE);
    public static final Color COMPANION_BULLET_COLOR = RGBtoColor(229, 229, 229, 1);
    public static final Color COMPANION_GHOST_BULLET_COLOR = RGBtoColor(96, 68, 255, 1);

    public static final Color MONEY_YELLOW = RGBtoColor(255, 214, 0, 1);



    public static final Color BOMB_RED = RGBtoColor(246, 45f, 45f, 1);
    public static final Color BOMB_ORANGE = RGBtoColor(255f, 124f, 0f, 1);
    public static final Color BOMB_YELLOW = RGBtoColor(249f, 188f, 4f, 1);

    public static final Color BLOB_GREEN = RGBtoColor(75f, 232f, 14f, 1);
    public static final Color BLOB_RED = RGBtoColor(241f, 53f, 53f, 1);

    public static Color RGBtoColor(float r, float g, float b, float a){
        return new Color(r / 255f, g / 255f, b / 255f, a);
    }


}
