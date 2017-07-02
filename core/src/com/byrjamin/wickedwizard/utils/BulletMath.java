package com.byrjamin.wickedwizard.utils;

/**
 * Created by ae164 on 20/05/17.
 */

public class BulletMath {

    public static double angleOfTravel(float startX, float startY, float endX, float endY) {
        return (Math.atan2(endY - startY, endX - startX));
    }


    public static float velocityX(float speed, double angleInRadians){
        return (float) (speed * Math.cos(angleInRadians));
    }

    public static float velocityY(float speed, double angleInRadians){
        return (float) (speed * Math.sin(angleInRadians));
    }



}
