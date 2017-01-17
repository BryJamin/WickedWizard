package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.entity.player.Wizard;

/**
 * Created by Home on 08/01/2017.
 */

//TODO extend rectangle class add a listener to ignore collision bounds?
public class RoomPlatform extends Rectangle{

    private boolean collisionOn = true;

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
        collisionOn =  wizard.getY() >= y;
    }


}
