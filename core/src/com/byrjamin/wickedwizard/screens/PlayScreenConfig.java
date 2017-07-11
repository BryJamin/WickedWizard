package com.byrjamin.wickedwizard.screens;

/**
 * Created by Home on 10/07/2017. Used for Dev purposes
 */

public class PlayScreenConfig {


    public enum Spawn {
        BOSS, ARENA, TUTORIAL
    }

    public Spawn spawn;

    public int id;

    public int roomid;


    public PlayScreenConfig(Spawn spawn, int id){
        this.spawn = spawn;
        this.id = id;
    };

    public PlayScreenConfig(Spawn spawn, int id, int roomid){
        this.spawn = spawn;
        this.id = id;
    };

}
