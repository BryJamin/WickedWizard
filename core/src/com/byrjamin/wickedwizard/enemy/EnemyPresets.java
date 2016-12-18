package com.byrjamin.wickedwizard.enemy;

import com.byrjamin.wickedwizard.enemy.enemies.Blob;
import com.byrjamin.wickedwizard.enemy.enemies.Turret;
import com.byrjamin.wickedwizard.spelltypes.Projectile;

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
                .health(1)
                .speed(3.0f)
                .scale(0.5f)
                .build();
    }

    public static Turret defaultTurret(float posX, float posY){
        return new Turret.TurretBuilder(posX, posY)
                .build();
    }


    public static Turret alternarteShotsTurret(float posX, float posY){
        return new Turret.TurretBuilder(posX, posY)
                .dispellSequence(new Projectile.DISPELL[]{Projectile.DISPELL.VERTICAL, Projectile.DISPELL.HORIZONTAL, Projectile.DISPELL.VERTICAL})
                .build();
    }


    public static Turret fastTurret(float posX, float posY){
        return new Turret.TurretBuilder(posX, posY)
                .shotSpeed(2.0f)
                .build();
    }



}
