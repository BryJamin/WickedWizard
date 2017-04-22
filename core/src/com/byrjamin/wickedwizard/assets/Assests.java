package com.byrjamin.wickedwizard.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Home on 19/04/2017.
 */

public class Assests {


    public static final String tutorialFont =  "tutorialFont.ttf";

    public static final String small =  "small.ttf";
    public static final String medium =  "medium.ttf";
    public static final String large =  "large.ttf";


    public static BitmapFont smallFont;
    public static BitmapFont mediumFont;
    public static BitmapFont largeFont;


    public static void initialize(AssetManager am) {
        smallFont = am.get(small, BitmapFont.class);
        mediumFont = am.get(medium, BitmapFont.class);


    }


}

