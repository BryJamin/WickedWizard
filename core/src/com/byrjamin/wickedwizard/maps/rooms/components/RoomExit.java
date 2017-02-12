package com.byrjamin.wickedwizard.maps.rooms.components;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.maps.MapCoords;

/**
 * Created by Home on 11/02/2017.
 */
public abstract class RoomExit {

    public enum EXIT_DIRECTION {
        LEFT, RIGHT, UP, DOWN, ANY
    }

    protected MapCoords roomCoords;
    protected MapCoords leaveCoords;
    protected Rectangle bounds;
    protected boolean unlocked = true;

    protected EXIT_DIRECTION direction;


/*    public RoomExit(MapCoords roomCoords, MapCoords leaveCoords){
        this.roomCoords = roomCoords;
        this.leaveCoords = leaveCoords;
        direction = EXIT_DIRECTION.ANY;
    }*/

    public RoomExit(MapCoords roomCoords, MapCoords leaveCoords, EXIT_DIRECTION direction){
        this.roomCoords = roomCoords;
        this.leaveCoords = leaveCoords;
        this.direction = direction;
    }


    public MapCoords getLeaveCoords() {
        return leaveCoords;
    }

    public void setLeaveCoords(MapCoords leaveCoords) {
        this.leaveCoords = leaveCoords;
    }

    public void lock(){
        unlocked = false;
    }

    public void unlock(){
        unlocked = true;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public MapCoords getRoomCoords() {
        return roomCoords;
    }

    public void setRoomCoords(MapCoords roomCoords) {
        this.roomCoords = roomCoords;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public EXIT_DIRECTION getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomExit roomExit = (RoomExit) o;

        if (roomCoords != null ? !roomCoords.equals(roomExit.roomCoords) : roomExit.roomCoords != null)
            return false;
        if (leaveCoords != null ? !leaveCoords.equals(roomExit.leaveCoords) : roomExit.leaveCoords != null)
            return false;
        return direction == roomExit.direction;

    }

    @Override
    public int hashCode() {
        int result = roomCoords != null ? roomCoords.hashCode() : 0;
        result = 31 * result + (leaveCoords != null ? leaveCoords.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }
}
