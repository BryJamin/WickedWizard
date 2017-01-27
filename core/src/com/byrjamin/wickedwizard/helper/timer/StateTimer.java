package com.byrjamin.wickedwizard.helper.timer;

/**
 * Created by Home on 01/01/2017.
 */
public class StateTimer {

    private float startTime;
    private float countDown;

    public StateTimer(float startTime){
        this.startTime = startTime;
        this.countDown = startTime;
    }


    public void update(float dt){
        if(countDown > 0) {
            countDown -= dt;
        }
    }

    public boolean isFinished(){
        return countDown <= 0;
    }

    public void reset(){
        countDown = startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }


    public void setCountDown(float countDown) {
        this.countDown = countDown;
    }
}
