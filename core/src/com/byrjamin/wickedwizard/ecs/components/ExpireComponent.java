package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 13/04/2017.
 */

public class ExpireComponent extends Component{

    public float expiryTime;

    public ExpireComponent(){
        this(0);
    }

    public ExpireComponent(float expiryTime){
        this.expiryTime = expiryTime;
    }


}
