package com.byrjamin.wickedwizard.helper.collider;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.entity.Entity;
import com.byrjamin.wickedwizard.helper.Measure;

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


    public static Collision collision(Rectangle currentBound, Rectangle futureBound, Rectangle wall){
        //Checks if there is a left or right collision
        //TODO figure out a way not to cheese this class using width and height

    if(wall.getHeight() > wall.getWidth()) {

        if (wall.overlaps(futureBound) && ((currentBound.getY() + currentBound.getHeight() > wall.getY() + 20) && currentBound.getY() < wall.y + wall.getHeight() - 20)) {
            if (futureBound.getX() < wall.x) {//Hit was on left
                currentBound.setX(wall.x - currentBound.width);
                return Collision.LEFT;
            } else if (futureBound.getX() > wall.x) {//Hit was on right
                currentBound.setX(wall.x + wall.getWidth());
                return Collision.RIGHT;
            }
        } else if (wall.overlaps(futureBound)) { //Hit was on top
            if (futureBound.getY() > wall.y) {
                currentBound.setY(wall.y + wall.getHeight());
                return Collision.TOP;
            } else if (futureBound.getY() < wall.y) { //Hit was on bottom
                currentBound.setY(wall.y - currentBound.height);
                return Collision.BOTTOM;
            }
        }

    } else {
        if (wall.overlaps(futureBound) && ((currentBound.getX() > wall.getX() && currentBound.getX() + currentBound.getWidth() < wall.getX() + wall.getWidth()))) {
            if (futureBound.getY() > wall.y) {
                currentBound.setY(wall.y + wall.getHeight());
                return Collision.TOP;
            } else if (futureBound.getY() < wall.y) { //Hit was on bottom
                currentBound.setY(wall.y - currentBound.height);
                return Collision.BOTTOM;
            }
        } else if (wall.overlaps(futureBound)) { //Hit was on top
            if (futureBound.getX() < wall.x) {//Hit was on left
                currentBound.setX(wall.x - currentBound.width);
                return Collision.LEFT;
            } else if (futureBound.getX() > wall.x) {//Hit was on right
                currentBound.setX(wall.x + wall.getWidth());
                return Collision.RIGHT;
            }
        }

    }
        return Collision.NONE;

    }


    public static boolean isOnTop(Rectangle bound, Rectangle wall){
        return (bound.getY() == wall.getY() + wall.getHeight());
    }

    public static boolean isOnTop(Rectangle bound, Array<Rectangle> wall){
        for(Rectangle r : wall){
            if(isOnTop(bound, r)) return true;
        }
        return false;
    }

    public static boolean isOnTop(Rectangle bound, Rectangle... wall){
        for(Rectangle r : wall){
            if(isOnTop(bound, r)) return true;
        }
        return false;
    }





}
