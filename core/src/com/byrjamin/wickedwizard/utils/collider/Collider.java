package com.byrjamin.wickedwizard.utils.collider;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.utils.CenterMath;

/**
 * A class used for checking collision between dynamic and static bodies and returning the type
 * of collision to whatever called the class
 */
public class Collider {

    public enum Collision {
        LEFT, RIGHT, BOTTOM, TOP, OVERLAPS, NONE
    }

    private static final float buffer = 40f;


    private static final Rectangle innerRectangle = new Rectangle();

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


        //TODO increase buffer based on where you are?

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
        } else if(wall.overlaps(currentBound)){

            Intersector.intersectRectangles(currentBound, wall, innerRectangle);

            //TODO Possibly do the same thing for the y axis as well?

                if (currentBound.getX() == innerRectangle.getX()) {
                    currentBound.x += innerRectangle.getWidth();
                } else if (currentBound.getX() + currentBound.getWidth() == innerRectangle.getX() + innerRectangle.getWidth()) {
                    currentBound.x -= innerRectangle.getWidth();
                } else {

                    if(wall.getWidth() >= wall.getHeight()) {
                        return topOrBottomCollision(currentBound, wall);
                    } else {
                        return leftOrRightCollision(currentBound, wall);
                        //Collision.OVERLAPS;
                    }
/*                    innerRectangle.setHeight(currentBound.getHeight());
                    innerRectangle.setWidth(currentBound.getWidth()  * 0.8f);
                    innerRectangle.setX(currentBound.x + CenterMath.offsetX(currentBound.getWidth(), innerRectangle.getWidth()));
                    innerRectangle.setY(currentBound.y + CenterMath.offsetY(currentBound.getHeight(), innerRectangle.getHeight()));*/
                    // return topOrBottomCollision(innerRectangle, wall);
                   // return leftOrRightCollision(currentBound, wall);

                }

            return leftOrRightCollision(currentBound, wall);

           // return leftOrRightCollision(innerRectangle, wall);

/*
                if (currentBound.getY() == innerRectangle.getY()) {
                    currentBound.y += innerRectangle.getHeight();
                } else if (currentBound.getY() + currentBound.getHeight() == innerRectangle.getY() + innerRectangle.getHeight()) {
                    currentBound.y -= innerRectangle.getHeight();
                }
*/

/*                    innerRectangle.setHeight(currentBound.getHeight()  * 0.8f);
                    innerRectangle.setWidth(currentBound.getWidth()  * 0.8f);
                    innerRectangle.setX(currentBound.x + CenterMath.offsetX(currentBound.getWidth(), innerRectangle.getWidth()));
                    innerRectangle.setY(currentBound.y + CenterMath.offsetY(currentBound.getHeight(), innerRectangle.getHeight()));*/
                   // return topOrBottomCollision(innerRectangle, wall);

           // }

/*            if(innerRectangle.getWidth() > innerRectangle.getHeight()){
                return leftOrRightCollision(currentBound, wall);
            } else {
                return topOrBottomCollision(currentBound, wall);
            }*/


/*
            innerRectangle.setHeight(currentBound.getHeight()  * 0.8f);
            innerRectangle.setWidth(currentBound.getWidth()  * 0.8f);
            innerRectangle.setX(currentBound.x + CenterMath.offsetX(currentBound.getWidth(), innerRectangle.getWidth()));
            innerRectangle.setY(currentBound.y + CenterMath.offsetY(currentBound.getHeight(), innerRectangle.getHeight()));
*/

            //TODO maybe turn this into top of bottom
            //return topOrBottomCollision(innerRectangle, wall);
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
