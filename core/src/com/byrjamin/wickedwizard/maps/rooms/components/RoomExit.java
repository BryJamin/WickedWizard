package com.byrjamin.wickedwizard.maps.rooms.components;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.maps.MapCoords;

/**
 * Created by Home on 11/02/2017.
 */
public abstract class RoomExit {

    public enum EXIT {
        LEFT, RIGHT, UP, DOWN, ANY
    }

    protected MapCoords roomCoords;
    protected MapCoords leaveCoords;
    protected Rectangle bounds;
    protected boolean unlocked = true;


    public RoomExit(MapCoords roomCoords, MapCoords leaveCoords){
        this.roomCoords = roomCoords;
        this.leaveCoords = leaveCoords;
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
}
