package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.entity.player.Wizard;

/**
 * Created by Home on 08/01/2017.
 */

//TODO extend rectangle class add a listener to ignore collision bounds?
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

        if(wizard.getY() >= getY() + getHeight()) {
            collisionOn = true;
        } else if(wizard.isFallThrough()){
            collisionOn = false;
        }


/*        if(toggle) {

            if (wizard.getY() > getY() + getHeight() + 10) {
                collisionOn = true;
                toggle = false;
            } else {
                collisionOn = false;
            }
        }


        if(wizard.isFallThrough() && !(wizard.getY() > getY() + getHeight() + 10)){
            collisionOn = false;
            toggle = true;
        }*/

        //collisionOn =  wizard.getY() >= y + height;

/*        System.out.println("Wizard y is :" + wizard.getY());
        System.out.println("platform y is :" +  y + height);
        System.out.println(collisionOn);
        System.out.println(wizard.isFallThrough());*/
/*        System.out.println(collisionOn);

        System.out.println(wizard.getY());
        System.out.println(y);*/

    }

    public void turnOffCollsion(){
        collisionOn = false;
    }

    public void turnOnCollsion(){
        collisionOn = true;
    }


}
