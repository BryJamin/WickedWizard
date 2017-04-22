package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;

/**
 * Created by Home on 11/03/2017.
 */
public class FiringAIComponent extends Component{

    public enum AI {
        TARGETED, UNTARGETED
    }

    public AI ai = AI.TARGETED;

    //uses radians
    public double firingAngleInRadians;

    public FiringAIComponent(){
        ai = AI.TARGETED;
    }

    public FiringAIComponent(double firingAngleInRadians) {
        this.ai = AI.UNTARGETED;
        this.firingAngleInRadians = firingAngleInRadians;
    }
}
