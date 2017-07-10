package com.byrjamin.wickedwizard.screens;

/**
 * Created by Home on 10/07/2017. Used for Dev purposes
 */

public class PlayScreenConfig {


    public enum Spawn {
        BOSS, ROOM, TUTORIAL
    }

    public Spawn spawn;

    public int id;


    public PlayScreenConfig(Spawn spawn, int id){
        this.spawn = spawn;
        this.id = id;
    };

}
