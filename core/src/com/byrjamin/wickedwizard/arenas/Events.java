package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;

/**
 * Created by Home on 10/12/2016.
 */
public class Events {

    private Array<Array<Enemy>> incomingWaves;


    public Events(){

    }













    public Array<Array<Enemy>> getIncomingWaves() {
        return incomingWaves;
    }

    public void setIncomingWaves(Array<Array<Enemy>> incomingWaves) {
        this.incomingWaves = incomingWaves;
    }
}
