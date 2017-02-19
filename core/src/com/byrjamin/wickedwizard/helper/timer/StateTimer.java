package com.byrjamin.wickedwizard.helper.timer;

/**
 * Created by Home on 01/01/2017.
 */
public class StateTimer {

    private float defaultTime;
    private float countDown;

    public StateTimer(float timer){
        this.defaultTime = timer;
        this.countDown = timer;
    }

    public StateTimer(float defaultTime, float countDown){
        this.defaultTime = defaultTime;
        this.countDown = countDown;
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
        countDown = defaultTime;
    }

    public void setDefaultTime(float defaultTime) {
        this.defaultTime = defaultTime;
    }

    public float getDefaultTime() {
        return defaultTime;
    }

    public float getCountDown() {
        return countDown;
    }

    public void setCountDown(float countDown) {
        this.countDown = countDown;
    }
}
