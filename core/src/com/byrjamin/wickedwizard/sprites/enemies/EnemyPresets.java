package com.byrjamin.wickedwizard.sprites.enemies;

/**
 * Created by Home on 10/12/2016.
 */
public class EnemyPresets {



    public static Blob slowBlob(float posX, float posY){
        return new Blob(posX, posY);
    }


    public static Turret slowTurret(float posX, float posY){
        return new Turret(posX, posY);
    }



}
