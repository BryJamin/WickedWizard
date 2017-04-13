package com.byrjamin.wickedwizard.utils.timer;

/**
 * Created by Home on 01/01/2017.
 */
public class StateTimer {

    private float resetTime;
    private float timeRemaining;

    public StateTimer(float timer){
        this.resetTime = timer;
        this.timeRemaining = timer;
    }

    public StateTimer(float resetTime, float timeRemaining){
        this.resetTime = resetTime;
        this.timeRemaining = timeRemaining;
    }



    public void update(float dt){
        if(timeRemaining > 0) {
            timeRemaining -= dt;
        }
    }

    public boolean isFinished(){
        return timeRemaining <= 0;
    }

    /**
     * Checks if the timer is finished and resets the timer if it is
     * @return - Returns if the timer has finished or not
     */
    public boolean isFinishedAndReset(){
        if(isFinished()){
            reset();
            return true;
        }
        return false;
    }

    /**
     * Resets the timer back to the defaultTime
     */
    public void reset(){
        timeRemaining = resetTime;
    }

    public void skip(){
        timeRemaining = 0;
    }

    public void setResetTime(float resetTime) {
        this.resetTime = resetTime;
    }

    public float getResetTime() {
        return resetTime;
    }

    public float getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(float timeRemaining) {
        this.timeRemaining = timeRemaining;
    }
}
