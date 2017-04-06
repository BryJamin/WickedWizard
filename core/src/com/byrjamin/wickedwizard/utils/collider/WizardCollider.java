package com.byrjamin.wickedwizard.utils.collider;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.archive.gameobject.player.Wizard;

public class WizardCollider {


    public static boolean collisionCheck(Wizard w, Rectangle wall, float dt){

        boolean update = true;

        Collider.Collision collision = Collider.collision(w.getBounds(), w.mockUpdate(dt), wall);

        switch(collision){
            case LEFT:
            case RIGHT:
                w.cancelMovementRetainVerticalSpeed();
                break;
            case TOP:
                w.resetGravity();
                break;
            case BOTTOM:
                w.stopMovement();
                break;
            case NONE:
                update = false;
            default:
                update = false;
        }


        if(update) w.positionUpdate();

        return true;

    }

}
