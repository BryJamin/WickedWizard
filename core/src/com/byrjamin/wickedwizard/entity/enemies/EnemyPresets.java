package com.byrjamin.wickedwizard.entity.enemies;

import com.byrjamin.wickedwizard.spelltypes.Dispellable;

/**
 * Created by Home on 10/12/2016.
 */
public class EnemyPresets {

    public static com.byrjamin.wickedwizard.entity.enemies.Blob defaultBlob(float posX, float posY){
        return new com.byrjamin.wickedwizard.entity.enemies.Blob.BlobBuilder(posX, posY).build();
    }

    public static Enemy largeBlob(float posX, float posY){
        return new com.byrjamin.wickedwizard.entity.enemies.Blob.BlobBuilder(posX, posY)
                .health(10)
                .speed(0.5f)
                .scale(2.0f)
                .build();
    }

    public static Enemy smallBlob(float posX, float posY){
        return new com.byrjamin.wickedwizard.entity.enemies.Blob.BlobBuilder(posX, posY)
                .health(1)
                .speed(3.0f)
                .scale(0.5f)
                .build();
    }

    public static com.byrjamin.wickedwizard.entity.enemies.Turret defaultTurret(float posX, float posY){
        return new com.byrjamin.wickedwizard.entity.enemies.Turret.TurretBuilder(posX, posY)
                .build();
    }


    public static com.byrjamin.wickedwizard.entity.enemies.Turret alternarteShotsTurret(float posX, float posY){
        return new com.byrjamin.wickedwizard.entity.enemies.Turret.TurretBuilder(posX, posY)
                .dispellSequence(new Dispellable.DISPELL[]{Dispellable.DISPELL.VERTICAL, Dispellable.DISPELL.HORIZONTAL})
                .build();
    }


    public static com.byrjamin.wickedwizard.entity.enemies.Turret fastTurret(float posX, float posY){
        return new com.byrjamin.wickedwizard.entity.enemies.Turret.TurretBuilder(posX, posY)
                .shotSpeed(2.0f)
                .build();
    }



}
