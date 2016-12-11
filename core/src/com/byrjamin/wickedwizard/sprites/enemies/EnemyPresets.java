package com.byrjamin.wickedwizard.sprites.enemies;

import com.byrjamin.wickedwizard.MainGame;
/**
 * Created by Home on 10/12/2016.
 */
public class EnemyPresets {

    public static Blob defaultBlob(float posX, float posY){
        return new Blob.BlobBuilder(posX, posY).build();
    }

    public static Blob largeBlob(float posX, float posY){
        return new Blob.BlobBuilder(posX, posY)
                .health(10)
                .speed(0.5f)
                .scale(2.0f)
                .build();
    }

    public static Blob smallBlob(float posX, float posY){
        return new Blob.BlobBuilder(posX, posY)
                .health(2)
                .speed(3.0f)
                .scale(0.5f)
                .build();
    }

    public static Turret slowTurret(float posX, float posY){
        return new Turret(posX, posY);
    }



}
