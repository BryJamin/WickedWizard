package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 11/03/2017.
 */
public class FiringAIComponent extends Component{

    public enum AI {
        TARGETED, UNTARGETED
    }

    public AI ai = AI.TARGETED;

    public float[] firingAngles;

    public FiringAIComponent(){
        ai = AI.TARGETED;
    }

    public FiringAIComponent(float[] firingAngles) {
        this.ai = AI.UNTARGETED;
        this.firingAngles = firingAngles;
    }
}
