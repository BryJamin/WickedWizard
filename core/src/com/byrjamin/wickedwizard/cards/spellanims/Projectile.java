package com.byrjamin.wickedwizard.cards.spellanims;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 07/11/2016.
 */
public class Projectile {

    double projectAngle;

    float gradient;

    float initial_x;
    float initial_y;

    Sprite sprite;

    public Projectile(float x1,float y1, float x2, float y2){

        sprite = PlayScreen.atlas.createSprite("blob_0");
        sprite.setSize((float) MainGame.GAME_UNITS * 5, MainGame.GAME_UNITS * 5);
        sprite.setPosition(x1, y1);
        initial_x = x1;
        initial_y = y1;
        calculateGradient(x1,y1,x2,y2);
    }


//TODO More math T---T


    public void calculateAngle(float x1,float y1, float x2, float y2){

        projectAngle = (Math.atan2(y2 - y1, x2 - x1));

        initial_x = x1;
        initial_y = y1;

        System.out.println("Angle is: " + projectAngle);

        System.out.println("In degrees is: " + Math.toDegrees(projectAngle));

/*        if (projectAngle < 0.0) {
            projectAngle += 360.0;
        }

        System.out.println("Angle is: " + projectAngle);*/

    }


    public void calculateGradient(float x1,float y1, float x2, float y2){
        gradient = (y2 - y1) / (x2 - x1);
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
        this.getSprite().translateX(1000f * dt);
        this.getSprite().setY(calculateY());
        //this.getSprite()
    }


    /**
     * Uses the y = mx + c formula to calculate the new y position after x has been translated
     *
     * @return
     */
    public float calculateY(){
        float return_x_to_zero = this.getSprite().getX() - initial_x;
        float new_y = return_x_to_zero * gradient;
        return initial_y + new_y;
    }


    public void draw(SpriteBatch batch){
        this.getSprite().draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
