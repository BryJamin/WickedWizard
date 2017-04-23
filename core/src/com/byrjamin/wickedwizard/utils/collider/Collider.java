package com.byrjamin.wickedwizard.utils.collider;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * A class used for checking collision between dynamic and static bodies and returning the type
 * of collision to whatever called the class
 */
public class Collider {

    public enum Collision {
        LEFT, RIGHT, TOP, BOTTOM, NONE
    }

    /**
     * Checks to see if there is a collision between A static and a dynamic body using Rectangles
     * @param currentBound - The current collision bound position of the dynamic body
     * @param futureBound - The predicted future collision bound position of the dynamic body
     * @param wall - The Collision bound for the Static bound
     * @return - Returns if the collision occurs on the top,bottom,left,right or if there isn't one at all
     */
    public static Collision collision(Rectangle currentBound, Rectangle futureBound, Rectangle wall) {

        //TODO With the variance being +15 or -15 the possiblility of breaking through walls might be possible
        boolean isBetweenY = currentBound.getY() + currentBound.getHeight() > wall.getY() + 15
                && currentBound.getY() < wall.getY() + wall.getHeight() - 15;
        boolean isBetweenX = currentBound.getX() + currentBound.getWidth() > wall.getX() + 15
                && currentBound.getX() < wall.getX() + wall.getWidth() - 15;

            if (wall.overlaps(futureBound) && isBetweenY && !isBetweenX) {
                if (futureBound.getX() < wall.x + wall.getWidth() / 2) {//Hit was on left
                    currentBound.setX(wall.x - currentBound.width);
                    return Collision.LEFT;
                } else if (futureBound.getX() > wall.x + wall.getWidth() / 2) {//Hit was on right
                    currentBound.setX(wall.x + wall.getWidth());
                    return Collision.RIGHT;
                }
            } else if (wall.overlaps(futureBound)) { //Hit was on top
                if (futureBound.getY() > wall.y + wall.getHeight() / 2) {
                    currentBound.setY(wall.y + wall.getHeight());
                    return Collision.TOP;
                } else if (futureBound.getY() < wall.y + wall.getHeight() / 2) { //Hit was on bottom
                    currentBound.setY(wall.y - currentBound.height);
                    return Collision.BOTTOM;
                }
            }

        return Collision.NONE;

    }

    public static Collision cleanCollision(Rectangle currentBound, Rectangle futureBound, Rectangle wall) {

        //TODO With the variance being +15 or -15 the possiblility of breaking through walls might be possible
        boolean isBetweenY = currentBound.getY() + currentBound.getHeight() > wall.getY() + 15
                && currentBound.getY() < wall.getY() + wall.getHeight() - 15;
        boolean isBetweenX = currentBound.getX() + currentBound.getWidth() > wall.getX() + 15
                && currentBound.getX() < wall.getX() + wall.getWidth() - 15;

        if (wall.overlaps(futureBound) && isBetweenY && !isBetweenX) {
            if (futureBound.getX() < wall.x + wall.getWidth() / 2) {//Hit was on left
                return Collision.LEFT;
            } else if (futureBound.getX() > wall.x + wall.getWidth() / 2) {//Hit was on right
                return Collision.RIGHT;
            }
        } else if (wall.overlaps(futureBound)) { //Hit was on top
            if (futureBound.getY() > wall.y + wall.getHeight() / 2) {
                return Collision.TOP;
            } else if (futureBound.getY() < wall.y + wall.getHeight() / 2) { //Hit was on bottom
                return Collision.BOTTOM;
            }
        }

        return Collision.NONE;

    }


    /**
     * Checks if a body is ontop of another static body
     * @param bound - Body A
     * @param wall - Static Body
     * @return - True if it on top
     */
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
