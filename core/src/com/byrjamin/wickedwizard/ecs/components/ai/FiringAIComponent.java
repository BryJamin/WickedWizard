package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;

/**
 * Created by Home on 11/03/2017.
 */
public class FiringAIComponent extends Component{

    public enum AI {
        TARGET_PLAYER, UNTARGETED, TARGET_ENEMY
    }

    public AI ai;

    //uses radians
    public double firingAngleInRadians;

    public float firingDelay;

    public float offsetX;
    public float offsetY;

    public FiringAIComponent(){
        ai = AI.TARGET_PLAYER;
    }


    public FiringAIComponent(AI ai){
        this.ai = ai;
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
        this.firingAngleInRadians = firingAngleInRadians;
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
