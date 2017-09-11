package com.bryjamin.wickedwizard.utils;

/**
 * Created by BB on 11/09/2017.
 */

public class TableMath {





    public static float getXPos(float startX, int number, int maxColumns, float width, float gapWidth){
        int mod = number % maxColumns;
        return startX + (width * mod) + (gapWidth * mod);
    }


    public static float getYPos(float startY, int number, int maxColumns, float width, float gapWidth){
        int div = number / maxColumns;
        return startY - (div * width) - (div * gapWidth);
    }










}
