package com.byrjamin.wickedwizard.helper.timer;

/**
 * Class used for reloading a checking if a person can shoot.
 */
public class Reloader {

    private float reload;
    private float reloadSpeed = 1f;

    private boolean ready;

    /**
     *
     * @param reloadSpeed - How long it takes to reload
     */
    public Reloader(float reloadSpeed){
        this.reloadSpeed = reloadSpeed;
    }

    /**
     *
     * @param reloadSpeed - How long it takes to reload.
     * @param initialDelay - How long to wait before you can shoot for the first time.
     */
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


    public void addWindUp(float windUp){
        reload = windUp;
    }

}
