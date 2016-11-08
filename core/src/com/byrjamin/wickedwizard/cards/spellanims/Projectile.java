package com.byrjamin.wickedwizard.cards.spellanims;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Home on 07/11/2016.
 */
public class Projectile {

    double projectAngle;

    float initial_x;

    Sprite sprite;

    public Projectile(){

    }


//TODO More math T---T


    public void calculateAngle(float x1,float y1, float x2, float y2){

        projectAngle = (Math.atan2(y2 - y1, x2 - x1));

        initial_x = x1;

        System.out.println("Angle is: " + projectAngle);

        System.out.println("In degrees is: " + Math.toDegrees(projectAngle));

/*        if (projectAngle < 0.0) {
            projectAngle += 360.0;
        }

        System.out.println("Angle is: " + projectAngle);*/

    }

    public void calculateLine(float x1, float y1, float x2, float y2){

        float oppositeL = y2 - y1;
        float adjacentL = x2 - x1;

        float endX = x2 - x1;



        //For testing purposes prints out the y co-ordinate as the x-axis increases
        //If you do opp/adj you should get the same angle generated earlier.
       for(float i = 0; i <= endX; i+=1){
            System.out.print("X axies is: " + i);
            System.out.println("Y axies is: " + i * Math.tan(projectAngle));
        }


    }


   // public void calculate


    public void update(float dt){




    }


    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
