package com.byrjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;

/**
 * Created by Home on 02/06/2017.
 */

public class FrictionComponent extends Component{

    public boolean verticalFriction = false;
    public boolean horizontalFriction = true;
    public boolean airFriction = true;

    public FrictionComponent(){
    }

    public FrictionComponent(boolean horizontalFriction, boolean verticalFriction, boolean airFriction){
        this.horizontalFriction = horizontalFriction;
        this.verticalFriction = verticalFriction;
        this.airFriction = airFriction;
    }

    public FrictionComponent(boolean horizontalFriction, boolean verticalFriction){
        this.horizontalFriction = horizontalFriction;
        this.verticalFriction = verticalFriction;
    }

}
