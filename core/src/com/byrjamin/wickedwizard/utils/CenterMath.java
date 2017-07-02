package com.byrjamin.wickedwizard.utils;

/**
 * Created by Home on 01/07/2017.
 */

public class CenterMath {

    public static float offsetX(float width, float widthToCenter){
        return (width / 2) - (widthToCenter / 2);
    }

    public static float offsetY(float height, float heightToCenter){
        return (height / 2) - (heightToCenter / 2);
    }

}
