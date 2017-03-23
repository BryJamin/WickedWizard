package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.byrjamin.wickedwizard.helper.Measure;

/**
 * Created by Home on 22/03/2017.
 */

public class MoveToComponent extends Component {

    private float speed = Measure.units(50f);
    public Float target_x;
    public boolean isMoving;

    public MoveToComponent(float speed) {
        this.speed = speed;
    }

    public MoveToComponent(){

    }

    public float getSpeed() {
        return speed;
    }
}
