package com.byrjamin.wickedwizard.utils.collider;

import com.badlogic.gdx.math.Rectangle;

/**
 * A class used for checking collision between dynamic and static bodies and returning the type
 * of collision to whatever called the class
 */
public class Collider {

    public enum Collision {
        LEFT, RIGHT, BOTTOM, TOP, NONE
    }

    private static final float buffer = 50f;

    /**
     * Checks to see if there is a collision between A static and a dynamic body using Rectangles
     * Updates bound position based on Collision
     * @param currentBound - The current collision bound position of the dynamic body
     * @param futureBound - The predicted future collision bound position of the dynamic body
     * @param wall - The Collision bound for the Static bound
     * @return - Returns if the collision occurs on the top,bottom,left,right or if there isn't one at all
     */
    public static Collision collision(Rectangle currentBound, Rectangle futureBound, Rectangle wall) {

        Collision c = cleanCollision(currentBound, futureBound, wall);
        switch (c){
            case RIGHT: currentBound.setX(wall.x - currentBound.width);
                break;
            case LEFT: currentBound.setX(wall.x + wall.getWidth());
                break;
            case BOTTOM: currentBound.setY(wall.y + wall.getHeight());
                break;
            case TOP: currentBound.setY(wall.y - currentBound.height);
        }

        return c;
    }

    /**
     * Checks to see if there is a collision between A static and a dynamic body using Rectangles
     * @param currentBound - The current collision bound position of the dynamic body
     * @param futureBound - The predicted future collision bound position of the dynamic body
     * @param wall - The Collision bound for the Static bound
     * @return - Returns if the collision occurs on the top,bottom,left,right or if there isn't one at all
     */
    public static Collision cleanCollision(Rectangle currentBound, Rectangle futureBound, Rectangle wall) {


        //TODO split it into two to see which one takes precedence left/right or top/bottom

/*        boolean leftOrRightPrecedence = (currentBound.getX() + currentBound.getWidth() <= wall.getX() + buffer ||
                currentBound.getX() >= wall.getX() + wall.getWidth() - buffer) &&
                (currentBound.getY() >= wall.getY() + wall.getHeight() - buffer ||
                        currentBound.getY() + currentBound.getHeight() <= wall.getY() + buffer);*/

        //TODO With the variance being +15 or -15 the possiblility of breaking through walls might be possible
        boolean isBetweenY = currentBound.getY() + currentBound.getHeight() > wall.getY() + buffer
                && currentBound.getY() < wall.getY() + wall.getHeight() - buffer;
        boolean isBetweenX = currentBound.getX() + currentBound.getWidth() > wall.getX() + buffer
                && currentBound.getX() < wall.getX() + wall.getWidth() - buffer;

        if (wall.overlaps(futureBound) && isBetweenY && !isBetweenX) {
            return leftOrRightCollision(futureBound, wall);
        } else if (wall.overlaps(futureBound) && !isBetweenY && isBetweenX ) { //Hit was on top
            return topOrBottomCollision(futureBound, wall);
        } else if(wall.contains(currentBound)){
/*
            System.out.println("current x" + currentBound.getX());
            System.out.println("current x" + currentBound.getX() + currentBound.getWidth());
            System.out.println("wall x " + wall.getX());
            System.out.println("YYY");
            System.out.println(currentBound.getY());
            System.out.println(wall.getY());


            boolean isGoingDown = futureBound.getY() < currentBound.getY();

            boolean isOnTop = isGoingDown && currentBound.getY() == wall.getY() + wall.getHeight();

            boolean leftOrRightPrecedence = currentBound.getX() + currentBound.getWidth() == wall.getX()
                    || currentBound.getX() == wall.getX() + wall.getHeight();*/


            //TODO maybe turn this into top of bottom 
            return leftOrRightCollision(futureBound, wall);

/*
            System.out.println(currentBound.getX());
            System.out.println(wall.getX());
            System.out.println("YYY");
            System.out.println(currentBound.getY());
            System.out.println(wall.getY());
            boolean leftOrRightPrecedence = (currentBound.getX() <=  wall.getX()
                    || currentBound.getX() + currentBound.getHeight() >= wall.getX() + wall.getHeight())
                    && ;
            System.out.println("INSIDE ELSE");
            if(leftOrRightPrecedence){
                return leftOrRightCollision(futureBound, wall);
            } else {
                return topOrBottomCollision(futureBound, wall);
            }
*/

        }

        return Collision.NONE;

    }

    private static Collision leftOrRightCollision(Rectangle currentBound, Rectangle wall){
/*        if (currentBound.getX() < wall.x + wall.getWidth() / 2) {//Hit was on left
            return Collision.RIGHT;
        } else if (currentBound.getX() > wall.x + wall.getWidth() / 2) {//Hit was on right
            return Collision.LEFT;
        }*/
        return currentBound.getX() <= wall.x + wall.getWidth() / 2 ? Collision.RIGHT : Collision.LEFT;
    }

    private static Collision topOrBottomCollision(Rectangle currentBound, Rectangle wall){
/*        if (currentBound.getX() < wall.x + wall.getWidth() / 2) {//Hit was on left
            return Collision.RIGHT;
        } else if (currentBound.getX() > wall.x + wall.getWidth() / 2) {//Hit was on right
            return Collision.LEFT;
        }*/
        return currentBound.getY() >= wall.y + wall.getHeight() / 2 ? Collision.BOTTOM : Collision.TOP;
    }



    //private Collision leftOrRightPrecedenceCollision()

}
