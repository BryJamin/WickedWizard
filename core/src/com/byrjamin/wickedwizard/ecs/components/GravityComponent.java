package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.byrjamin.wickedwizard.helper.Measure;

/**
 * Created by Home on 04/03/2017.
 */
public class GravityComponent extends Component{

    public float gravity = -Measure.units(3f);
    public boolean ignoreGravity = false;

    public GravityComponent (float gravity){
        this.gravity = gravity;
    }

    public GravityComponent(){
        this(-Measure.units(1.5f));
    }


}
