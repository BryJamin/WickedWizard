package com.byrjamin.wickedwizard.maps.rooms.spawns;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.entity.enemies.Bouncer;
import com.byrjamin.wickedwizard.entity.enemies.Enemy;
import com.byrjamin.wickedwizard.entity.enemies.EnemyPresets;
import com.byrjamin.wickedwizard.entity.enemies.SilverHead;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 10/12/2016.
 */
public class OmniBattleRooms {

    public OmniBattleRooms(){
    }


    public static void blob(Room room){
        room.addSpawningEnemy((EnemyPresets.defaultBlob(room.WIDTH - 500, room.groundHeight())));
        room.addSpawningEnemy(new SilverHead.SilverHeadBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
    }



    public static void blob2(Room room){
      //  e.clear();
        room.addSpawningEnemy(EnemyPresets.smallBlob(room.WIDTH - 500, room.groundHeight()));
        room.addSpawningEnemy(EnemyPresets.defaultTurret(room.WIDTH / 4, room.HEIGHT - Measure.units(15)));
    }

    public static void blob3(Room room){
      //  e.clear();
        room.addSpawningEnemy(EnemyPresets.largeBlob(room.WIDTH - 500, room.groundHeight() + 200));
        room.addSpawningEnemy(EnemyPresets.fastTurret(room.WIDTH / 4 * 3, room.HEIGHT - Measure.units(15)));
    }


    public static void blob4(Room room){
     //   e.clear();
        room.addSpawningEnemy(new SilverHead.SilverHeadBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
        room.addSpawningEnemy(EnemyPresets.alternarteShotsTurret(room.WIDTH / 2, room.HEIGHT - Measure.units(15)));
    }

    public static void bouncer(Room room){
        room.addSpawningEnemy(new Bouncer.BouncerBuilder(room.WIDTH / 2, room.groundHeight() * 3).build());
    }

    public static void bouncerTwo(Room room){
        room.addSpawningEnemy(new Bouncer.BouncerBuilder(room.WIDTH / 4, room.groundHeight() * 2).build());
        room.addSpawningEnemy(new Bouncer.BouncerBuilder(room.WIDTH - room.WIDTH / 4, room.groundHeight() * 2).build());
    }

    public static void bouncerLarge(Room room){
        room.addSpawningEnemy(new Bouncer.BouncerBuilder(room.WIDTH / 2, room.groundHeight() * 2).scale(2.0f).speed(0.8f).build());
    }

    public static void threeblobs(Room room){
        room.addSpawningEnemy(new Bouncer.BouncerBuilder(room.WIDTH - 500, room.groundHeight()).build());
        room.addSpawningEnemy(EnemyPresets.smallBlob(room.WIDTH - 500, room.groundHeight()));
        room.addSpawningEnemy(EnemyPresets.defaultBlob(room.WIDTH - 500, room.groundHeight()));
        room.addSpawningEnemy(EnemyPresets.largeBlob(room.WIDTH - 500, room.groundHeight()));
    }


    public static void add2EnemyWave(Room room){
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
        void spawnWave(Room room);
    }

    public static Waves[] spawnWave = new Waves[] {
            new Waves() { public void spawnWave(Room room) { blob(room); } },
            new Waves() { public void spawnWave(Room room) { blob2(room); } },
            new Waves() { public void spawnWave(Room room) { threeblobs(room); } },
            new Waves() { public void spawnWave(Room room) { blob3(room); } },
            new Waves() { public void spawnWave(Room room) { blob4(room); } },
            new Waves() { public void spawnWave(Room room) { add2EnemyWave(room);} },
            new Waves() { public void spawnWave(Room room) { bouncer(room);} },
            new Waves() { public void spawnWave(Room room) { bouncerTwo(room);} },
            new Waves() { public void spawnWave(Room room) { bouncerLarge(room);} },
    };
}
