package com.byrjamin.wickedwizard.utils.enums;

/**
 * Created by Home on 06/04/2017.
 */

public enum Direction {
    LEFT, RIGHT, UP, DOWN;

    public Direction getOpposite(Direction d){
        switch (d){
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            case UP: return DOWN;
            case DOWN: return UP;
        }
        return null;
    }


}
