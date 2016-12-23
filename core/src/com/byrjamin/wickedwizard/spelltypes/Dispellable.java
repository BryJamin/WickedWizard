package com.byrjamin.wickedwizard.spelltypes;

/**
 * Give it health?
 */
public class Dispellable {

    public enum DISPELL {
        VERTICAL, HORIZONTAL, NONE
    }

    private DISPELL dispel;

    private boolean dispelled;


    public Dispellable(DISPELL dispel) {
        this.dispel = dispel;
        dispelled = false;
    }


    public void dispel(DISPELL dispel){
        if(this.dispel == dispel){
            dispelled = true;
        }
    }

    public DISPELL getDispel() {
        return dispel;
    }

    public boolean isDispelled() {
        return dispelled;
    }
}
