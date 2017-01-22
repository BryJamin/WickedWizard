package com.byrjamin.wickedwizard.helper.collider;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.entity.Entity;

/**
 * Created by Home on 22/01/2017.
 */
public class Collider {

    public enum Collision {
        LEFT, RIGHT, TOP, BOTTOM, NONE
    }
/*

    public static boolean collisionCheck(Rectangle Actual, Rectangle mock, Rectangle wall){




        return true;
    }
*/


    public static Collision collision(Rectangle actual, Rectangle mock, Rectangle wall){
        //Checks if there is a left or right collision
        //TODO convert this into a collision task the +5 is for variance,
        if(wall.overlaps(mock) && ((actual.getY() + actual.getHeight() > wall.getY() + 5) && actual.getY() < wall.y + wall.getHeight() - 5)) {
            if (mock.getX() < wall.x) {//Hit was on left
                actual.setX(wall.x - actual.width);
                return Collision.LEFT;
            } else if (mock.getX() > wall.x) {//Hit was on right
                actual.setX(wall.x + wall.getWidth());
                return Collision.RIGHT;
            }
        } else if(wall.overlaps(mock)) { //Hit was on top
            if (mock.getY() > wall.y) {
                actual.setY(wall.y + wall.getHeight());
                return Collision.TOP;
            } else if (mock.getY() < wall.y) { //Hit was on bottom
                actual.setY(wall.y - actual.height);
                return Collision.BOTTOM;
            }
        }

        return Collision.NONE;

    }


    public static boolean isOnTop(Rectangle bound, Rectangle wall){
        return (bound.getY() == wall.getY() + wall.getHeight());
    }





}
