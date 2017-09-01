package com.bryjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;

/**
 * Created by Home on 22/03/2017.
 */

public class MoveToComponent extends Component {

    public Float targetX;
    public Float targetY;

    public float accelX;
    public float accelY;

    public float maxX;
    public float maxY;

    public float endSpeedX;
    public float endSpeedY;

    public Float maxEndSpeedX;
    public Float maxEndSpeedY;

    public void reset(){
        targetX = null;
        targetY = null;
    }



    public boolean hasTarget(){
        return targetX != null && targetY != null;
    }

    public boolean isMoving;
}
