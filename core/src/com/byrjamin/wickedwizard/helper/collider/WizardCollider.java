package com.byrjamin.wickedwizard.helper.collider;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.entity.player.Wizard;

/**
 * Created by Home on 22/01/2017.
 */
public class WizardCollider {


    public static void collisionCheck(Wizard w, Rectangle wall, float dt){

        boolean update = true;

        switch(Collider.collision(w.getBounds(), w.mockUpdate(dt), wall)){
            case LEFT:
                //w.setX(wall.x - w.WIDTH);
                w.cancelMovementRetainVerticalSpeed();
                break;
            case RIGHT:
                //w.setX(wall.x + wall.getWidth());
                w.cancelMovementRetainVerticalSpeed();
                break;
            case TOP:
                //w.setY(wall.y + wall.getHeight());
                w.land();
                break;
            case BOTTOM:
                //w.setY(wall.y - w.HEIGHT);
                w.stopMovement();
                break;
            default:
                update = false;
        }

        if(update) w.positionUpdate();

    }

}
