package com.byrjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;

/**
 * Created by Home on 22/03/2017.
 */

public class MoveToComponent extends Component {

    public Float targetX;
    public Float targetY;

    public float speedX;
    public float speedY;

    public float endSpeedX;
    public float endSpeedY;

    public Float maxEndSpeedX;
    public Float maxEndSpeedY;

    public void reset(){
        targetX = null;
        targetY = null;
    }

    public boolean isMoving;
}
