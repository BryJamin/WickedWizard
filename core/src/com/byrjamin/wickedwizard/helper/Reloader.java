package com.byrjamin.wickedwizard.helper;

/**
 * Created by Home on 09/12/2016.
 */
public class Reloader {

    private float reload;
    private float reloadSpeed = 1f;

    private boolean ready;

    public Reloader(float reloadSpeed){
        this.reloadSpeed = reloadSpeed;
    }

    public Reloader(float reloadSpeed, float initialDelay){
        this.reload = initialDelay;
        this.reloadSpeed = reloadSpeed;
    }

    public void update(float dt){
        reload -= dt;
        if(reload <= 0){
            reload += reloadSpeed;
            ready = true;
        } else {
            ready = false;
        }
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
