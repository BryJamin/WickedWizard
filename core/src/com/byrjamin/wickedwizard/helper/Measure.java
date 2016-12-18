package com.byrjamin.wickedwizard.helper;

import com.byrjamin.wickedwizard.MainGame;

/**
 * Small class for using the game measurements.
 */
public class Measure {

    private static final float unit = MainGame.GAME_UNITS;

    /**
     * multiples the number by the uni of measurement being used in the application
     * @param i
     * @return
     */
    public static float units(float i){
        return unit * i;
    }



}
