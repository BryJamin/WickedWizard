package com.byrjamin.wickedwizard.archive.maps.rooms.spawns;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.archive.gameobject.enemies.Bouncer;
import com.byrjamin.wickedwizard.archive.gameobject.enemies.Enemy;
import com.byrjamin.wickedwizard.archive.gameobject.enemies.EnemyPresets;
import com.byrjamin.wickedwizard.archive.gameobject.enemies.GroundTurret;
import com.byrjamin.wickedwizard.archive.gameobject.enemies.KugelDusche;
import com.byrjamin.wickedwizard.archive.gameobject.enemies.SilverHead;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 10/12/2016.
 */
public class OmniBattleRooms {

    public OmniBattleRooms(){
    }

    public static void blob(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        room.addSpawningEnemy((EnemyPresets.defaultBlob(room.WIDTH - 500, room.groundHeight())));
        room.addSpawningEnemy(new SilverHead.SilverHeadBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
    }

    public static void blob2(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
      //  e.clear();
        room.addSpawningEnemy(EnemyPresets.smallBlob(room.WIDTH - 500, room.groundHeight()));
        room.addSpawningEnemy(EnemyPresets.defaultTurret(room.WIDTH / 4, room.HEIGHT - Measure.units(15)));
    }

    public static void blob3(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
      //  e.clear();
        room.addSpawningEnemy(EnemyPresets.largeBlob(room.WIDTH - 500, room.groundHeight() + 200));
        room.addSpawningEnemy(EnemyPresets.fastTurret(room.WIDTH / 4 * 3, room.HEIGHT - Measure.units(15)));
    }


    public static void silverhead(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
     //   e.clear();
        room.addSpawningEnemy(new SilverHead.SilverHeadBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
        room.addSpawningEnemy(EnemyPresets.alternarteShotsTurret(room.WIDTH / 2, room.HEIGHT - Measure.units(15)));
    }

    public static void bouncer(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        room.addSpawningEnemy(new Bouncer.BouncerBuilder(room.WIDTH / 2, room.groundHeight() * 3).build());
    }

    public static void bouncerTwo(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        room.addSpawningEnemy(new Bouncer.BouncerBuilder(room.WIDTH / 4, room.groundHeight() * 2).build());
        room.addSpawningEnemy(new Bouncer.BouncerBuilder(room.WIDTH - room.WIDTH / 4, room.groundHeight() * 2).build());
    }

    public static void bouncerLarge(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        room.addSpawningEnemy(new Bouncer.BouncerBuilder(room.WIDTH / 2, room.groundHeight() * 2).scale(2.0f).speed(0.8f).build());
    }

    public static void threeblobs(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        room.addSpawningEnemy(EnemyPresets.smallBlob(room.WIDTH - 500, room.groundHeight()));
        room.addSpawningEnemy(EnemyPresets.defaultBlob(room.WIDTH - 500, room.groundHeight()));
        room.addSpawningEnemy(EnemyPresets.largeBlob(room.WIDTH - 500, room.groundHeight()));
    }

    public static void newEnemy(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        room.addSpawningEnemy(new GroundTurret.GroundTurretBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
    }

    public static void groundTurret(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        room.addSpawningEnemy(new GroundTurret.GroundTurretBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
    }

    public static void kugelDusche(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        room.addSpawningEnemy(new KugelDusche.KugelDuscheBuilder(room.WIDTH / 2 - Measure.units(5), room.HEIGHT / 2).build());
    }

    public static void kugelDuscheTwoBullets(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        room.addSpawningEnemy(new KugelDusche.KugelDuscheBuilder(room.WIDTH / 2 - Measure.units(5), room.HEIGHT / 2).type(KugelDusche.TYPE.TWO_BULLETS).build());
        //room.add(new GrapplePoint(room.WIDTH / 4, room.HEIGHT / 5 * 4));
        //room.add(new GrapplePoint(0, 0));

        com.byrjamin.wickedwizard.archive.maps.rooms.components.GrapplePoint gp = new com.byrjamin.wickedwizard.archive.maps.rooms.components.GrapplePoint(0, 0);
        gp.setCenter(room.WIDTH / 4, room.HEIGHT / 5 * 4);
        room.add(gp);
        gp = new com.byrjamin.wickedwizard.archive.maps.rooms.components.GrapplePoint(0, 0);
        gp.setCenter(room.WIDTH / 4 * 3, room.HEIGHT / 5 * 4);
        room.add(gp);
    }

    public static void add2EnemyWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room){
        Array<Enemy> e = new Array<Enemy>();
        e.add(EnemyPresets.smallBlob(room.WIDTH - 500, room.groundHeight()));
        e.add(EnemyPresets.defaultBlob(room.WIDTH - 500, room.groundHeight()));
        room.addEnemyWave(e);
        e = new Array<Enemy>();
        e.add(EnemyPresets.smallBlob(500, room.groundHeight()));
        e.add(EnemyPresets.defaultBlob(500, room.groundHeight()));
        room.addEnemyWave(e);
    }

    public interface Waves {
        void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room);
    }

    public static Waves[] spawnWave = new Waves[] {
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) {
                blob(room);
            } },
            //new Waves() { public void spawnWave(Room room) { blob2(room); } },
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) { threeblobs(room); } },
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) { blob3(room); } },
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) { silverhead(room); } },
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) { add2EnemyWave(room);} },
            //new Waves() { public void spawnWave(Room room) { bouncer(room);} },
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) { bouncerTwo(room);} },
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) { bouncerLarge(room);} },
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) { groundTurret(room);} },
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) { kugelDusche(room);} },
            new Waves() { public void spawnWave(com.byrjamin.wickedwizard.archive.maps.rooms.Room room) { kugelDuscheTwoBullets(room);} },
    };
}
