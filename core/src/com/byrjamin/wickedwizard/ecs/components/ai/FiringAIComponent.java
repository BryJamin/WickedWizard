package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;

/**
 * Created by Home on 11/03/2017.
 */
public class FiringAIComponent extends Component{

    public enum AI {
        TARGETED, UNTARGETED
    }

    public AI ai;

    //uses radians
    public double firingAngleInRadians;

    public float firingDelay;

    public float offsetX;
    public float offsetY;

    public FiringAIComponent(){
        ai = AI.TARGETED;
    }

    public FiringAIComponent(AI ai, float offsetX, float offsetY){
        this.ai = ai;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public FiringAIComponent(AI ai, double firingAngleInRadians, float offsetX, float offsetY){
        this.ai = ai;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public FiringAIComponent(double firingAngleInDegrees) {
        this.ai = AI.UNTARGETED;
        this.firingAngleInRadians = Math.toRadians(firingAngleInDegrees);
    }

    public FiringAIComponent(double firingAngleInDegrees, float firingDelay) {
        this.ai = AI.UNTARGETED;
        this.firingAngleInRadians = Math.toRadians(firingAngleInDegrees);
        this.firingDelay = firingDelay;
    }

}
