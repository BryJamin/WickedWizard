package com.byrjamin.wickedwizard.utils;

import com.byrjamin.wickedwizard.MainGame;

/**
 * Small class for using the game measurements.
 */
public class Measure {

    private static final float unit = MainGame.GAME_UNITS;

    /**
     * Multiples the number by the uniy of measurement being used in the application
     * @param i - Number to be multiplied
     * @return - The Number multiplied by the unit of measurement
     */
    public static float units(float i){
        return unit * i;
    }



}
