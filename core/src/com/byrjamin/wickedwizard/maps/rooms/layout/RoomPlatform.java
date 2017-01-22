package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.entity.player.Wizard;

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

        if(toggle) {
            if (wizard.getY() >= getY() + getHeight()) {
                collisionOn = true;
                toggle = false;
            } else if (wizard.isFallThrough()) {
                collisionOn = false;
            } else {
                collisionOn = false;
            }
        } else if(wizard.isFallThrough()){
            collisionOn = false;
            toggle = true;
        }
    }

    public void turnOffCollsion(){
        collisionOn = false;
    }

    public void turnOnCollsion(){
        collisionOn = true;
    }


}
