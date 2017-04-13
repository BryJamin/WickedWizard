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

    public double firingAngle;

    public FiringAIComponent(){
        ai = AI.TARGETED;
    }

    public FiringAIComponent(double firingAngle) {
        this.ai = AI.UNTARGETED;
        this.firingAngle = firingAngle;
    }
}
