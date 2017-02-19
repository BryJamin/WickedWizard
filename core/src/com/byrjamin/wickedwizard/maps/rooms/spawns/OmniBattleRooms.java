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

    private List<Integer> shuffled;

    private long randomSeed = 103354434;




    public OmniBattleRooms(){
    }


    public void blob(Room room){
        room.addEnemy((EnemyPresets.defaultBlob(room.WIDTH - 500, room.groundHeight())));
        room.addEnemy(new SilverHead.SilverHeadBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
    }



    public void blob2(Room room){
      //  e.clear();
        room.addEnemy(EnemyPresets.smallBlob(room.WIDTH - 500, room.groundHeight()));
        room.addEnemy(EnemyPresets.defaultTurret(room.WIDTH / 4, room.HEIGHT - Measure.units(15)));
    }

    public void blob3(Room room){
      //  e.clear();
        room.addEnemy(EnemyPresets.largeBlob(room.WIDTH - 500, room.groundHeight() + 200));
        room.addEnemy(EnemyPresets.fastTurret(room.WIDTH / 4 * 3, room.HEIGHT - Measure.units(15)));
    }


    public void blob4(Room room){
     //   e.clear();
        room.addEnemy(new SilverHead.SilverHeadBuilder(room.WIDTH - room.WIDTH / 3, room.HEIGHT - room.HEIGHT / 4).build());
        room.addEnemy(EnemyPresets.alternarteShotsTurret(room.WIDTH / 2, room.HEIGHT - Measure.units(15)));
    }

    public void threeblobs(Room room){
        room.addEnemy(EnemyPresets.smallBlob(room.WIDTH - 500, room.groundHeight()));
        room.addEnemy(EnemyPresets.defaultBlob(room.WIDTH - 500, room.groundHeight()));
        room.addEnemy(EnemyPresets.largeBlob(room.WIDTH - 500, room.groundHeight()));
    }

    public interface Waves {
        void spawnWave(Room room);
    }

    public Waves[] spawnWave = new Waves[] {
            new Waves() { public void spawnWave(Room room) { blob(room); } },
            new Waves() { public void spawnWave(Room room) { blob2(room); } },
            new Waves() { public void spawnWave(Room room) { threeblobs(room); } },
            new Waves() { public void spawnWave(Room room) { blob3(room); } },
            new Waves() { public void spawnWave(Room room) { blob4(room); } },
    };


    /**
     * Current this creates an array with the number 1..n (n being the size of the spawnWave array
     * This then converts the array into a list and shuffles it using the global random seed.
     */
    public void generateShuffler(){
        Integer[] a = new Integer[spawnWave.length];
        for (int i=0;i < spawnWave.length;++i){
            a[i]=i;
        }

        shuffled = Arrays.asList(a);

        Collections.shuffle(shuffled, new Random (randomSeed));


        for(int i=0; i< shuffled.size(); i++){
            //System.out.println(shuffled.get(i));
        }

    }

}
