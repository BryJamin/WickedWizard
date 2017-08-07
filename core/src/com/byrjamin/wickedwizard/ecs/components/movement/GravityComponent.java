package com.byrjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 04/03/2017.
 */
public class GravityComponent extends Component{

    public static final float DEFAULT_GRAVITY = -Measure.units(3f);

    public float gravity = DEFAULT_GRAVITY;
    public boolean ignoreGravity = false;

    public GravityComponent (float gravity){
        this.gravity = gravity;
    }

    public GravityComponent(){
        this(DEFAULT_GRAVITY);
    }


}
