package com.byrjamin.wickedwizard.archive.maps.rooms.components;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.archive.gameobject.player.Wizard;

/**
 * Created by Home on 08/01/2017.
 */

//TODO extend rectangle class add a listener to ignore collisionCheck bounds?
public class RoomPlatform extends Rectangle{

    private boolean collisionOn = true;
    private boolean toggle = true;

    public RoomPlatform(float x, float y, float width, float height) {
        super(x, y, width, height);
    }


    @Override
    public boolean overlaps(Rectangle r) {
        if(collisionOn) {
            return super.overlaps(r);
        } else {
            return false;
        }
    }


    public void update(Wizard wizard){

        if (wizard.getY() >= getY() + getHeight() - 20) {
            collisionOn = true;
        }

        if(wizard.isFallThrough()) {
            collisionOn = false;
        }
    }

    public boolean isCollisionOn() {
        return collisionOn;
    }

    public void turnOffCollsion(){
        collisionOn = false;
    }

    public void turnOnCollsion(){
        collisionOn = true;
    }


}
