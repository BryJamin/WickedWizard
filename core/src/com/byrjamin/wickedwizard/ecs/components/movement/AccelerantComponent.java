package com.byrjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;

/**
 * Created by Home on 28/03/2017.
 */

public class AccelerantComponent extends Component {

    public float maxX;
    public float maxY;

    public float accelX;
    public float accelY;

    public AccelerantComponent(){
/*        maxX = Measure.units(5);
        accelX = Measure.units(5);*/
    }


    public AccelerantComponent(float maxX, float maxY){
        this.maxX = maxX;
        this.maxY = maxY;
        this.accelX = maxX;
        this.accelY = maxY;
    }

    public AccelerantComponent(float accelX, float accelY, float maxX, float maxY) {
        this.accelX = accelX;
        this.accelY = accelY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
}
