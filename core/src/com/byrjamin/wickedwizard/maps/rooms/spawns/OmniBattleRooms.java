package com.byrjamin.wickedwizard.maps.rooms.spawns;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.entity.enemies.Enemy;
import com.byrjamin.wickedwizard.entity.enemies.EnemyPresets;
import com.byrjamin.wickedwizard.entity.enemies.SilverHead;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.rooms.Room;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Home on 10/12/2016.
 */
public class OmniBattleRooms {

    public OmniBattleRooms(){
    }


    public static void blob(Room room){
        room.addEnemy((EnemyPresets.defaultBlob(room.WIDTH - 500, room.groundHeight())));
        room.addEnemy(new SilverHead.SilverHeadBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
    }



    public static void blob2(Room room){
      //  e.clear();
        room.addEnemy(EnemyPresets.smallBlob(room.WIDTH - 500, room.groundHeight()));
        room.addEnemy(EnemyPresets.defaultTurret(room.WIDTH / 4, room.HEIGHT - Measure.units(15)));
    }

    public static void blob3(Room room){
      //  e.clear();
        room.addEnemy(EnemyPresets.largeBlob(room.WIDTH - 500, room.groundHeight() + 200));
        room.addEnemy(EnemyPresets.fastTurret(room.WIDTH / 4 * 3, room.HEIGHT - Measure.units(15)));
    }


    public static void blob4(Room room){
     //   e.clear();
        room.addEnemy(new SilverHead.SilverHeadBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
        room.addEnemy(EnemyPresets.alternarteShotsTurret(room.WIDTH / 2, room.HEIGHT - Measure.units(15)));
    }

    public static void threeblobs(Room room){
        room.addEnemy(EnemyPresets.smallBlob(room.WIDTH - 500, room.groundHeight()));
        room.addEnemy(EnemyPresets.defaultBlob(room.WIDTH - 500, room.groundHeight()));
        room.addEnemy(EnemyPresets.largeBlob(room.WIDTH - 500, room.groundHeight()));
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
            new Waves() { public void spawnWave(Room room) { add2EnemyWave(room); } },
    };
}
