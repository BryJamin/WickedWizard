package com.byrjamin.wickedwizard.helper;

import com.byrjamin.wickedwizard.MainGame;

/**
 * Created by Home on 17/12/2016.
 */
public class Measure {

    private static final float unit = MainGame.GAME_UNITS;


    public static float units(float i){
        return unit * i;
    }



}
