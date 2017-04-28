package com.byrjamin.wickedwizard.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Home on 19/04/2017.
 */

public class Assets {


    public static final String tutorialFont =  "tutorialFont.ttf";

    public static final String small =  "small.ttf";
    public static final String medium =  "medium.ttf";
    public static final String large =  "large.ttf";

    public static final Color level1Wall = Color.WHITE;
    public static final Color level2Wall = Color.BLUE;
    public static final Color level3Wall = Color.WHITE;
    public static final Color level4Wall = Color.WHITE;
    public static final Color level5Wall = Color.WHITE;

    public static Color currentWall;


    public static final Color level1bg = Color.WHITE;
    public static final Color level2bg = Color.BLUE;
    public static final Color level3bg = Color.WHITE;
    public static final Color level4bg = Color.WHITE;
    public static final Color level5bg = Color.WHITE;

    public static Color currentbg;


    public static BitmapFont smallFont;
    public static BitmapFont mediumFont;
    public static BitmapFont largeFont;


    public static void initialize(AssetManager am) {
        smallFont = am.get(small, BitmapFont.class);
        mediumFont = am.get(medium, BitmapFont.class);

        currentbg = level1bg;
        currentWall = level1Wall;
    }


}

