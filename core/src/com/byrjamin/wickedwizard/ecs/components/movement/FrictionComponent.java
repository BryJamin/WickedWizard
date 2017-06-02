package com.byrjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;

/**
 * Created by Home on 02/06/2017.
 */

public class FrictionComponent extends Component{

    public boolean verticalFriction = false;
    public boolean horizontalFriction = true;

    public FrictionComponent(){
    }

    public FrictionComponent(boolean horizontalFriction, boolean verticalFriction){
        this.horizontalFriction = horizontalFriction;
        this.verticalFriction = verticalFriction;
    }

}
