package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;
import com.byrjamin.wickedwizard.sprites.enemies.EnemyPresets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Home on 10/12/2016.
 */
public class EnemyWaves {

    private Array<Array<Enemy>> incomingWaves;

    private Arena arena;

    private Array<Enemy> enemyArray;

    private int currentWave = 0;
    private List<Integer> shuffled;

    private long randomSeed = 103354434;




    public EnemyWaves(Arena a){
        arena = a;

        generateWaves();
    }


    public void blob(Array<Enemy> e){
        e.clear();
        e.add(EnemyPresets.defaultBlob(arena.ARENA_WIDTH, arena.groundHeight()));
    }

    public void blob2(Array<Enemy> e){
        e.clear();
        e.add(EnemyPresets.smallBlob(arena.ARENA_WIDTH, arena.groundHeight()));
        e.add(EnemyPresets.defaultTurret(arena.ARENA_WIDTH, arena.ARENA_HEIGHT - MainGame.GAME_UNITS * 11));
    }

    public void blob3(Array<Enemy> e){
        e.clear();
        e.add(EnemyPresets.largeBlob(arena.ARENA_WIDTH, arena.groundHeight()));
        e.add(EnemyPresets.alternarteShotsTurret(arena.ARENA_WIDTH, arena.ARENA_HEIGHT - MainGame.GAME_UNITS * 11));
        e.add(EnemyPresets.fastTurret(arena.ARENA_WIDTH, arena.ARENA_HEIGHT - MainGame.GAME_UNITS * 11));
    }

    public void turret(Array<Enemy> e){
        e.clear();
        e.add(EnemyPresets.smallBlob(arena.ARENA_WIDTH, arena.groundHeight()));
        e.add(EnemyPresets.defaultBlob(arena.ARENA_WIDTH, arena.groundHeight()));
        e.add(EnemyPresets.largeBlob(arena.ARENA_WIDTH, arena.groundHeight()));
    }

    interface Waves {
        void spawnWave(Array<Enemy> enemies);
    }

    private Waves[] spawnWave = new Waves[] {
            new Waves() { public void spawnWave(Array<Enemy> enemies) { blob(enemies); } },
            new Waves() { public void spawnWave(Array<Enemy> enemies) { blob2(enemies); } },
            new Waves() { public void spawnWave(Array<Enemy> enemies) { turret(enemies); } },
            new Waves() { public void spawnWave(Array<Enemy> enemies) { blob3(enemies); } },
    };


    /**
     * Current this creates an array with the number 1..n (n being the size of the spawnWave array
     * This then converts the array into a list and shuffles it using the global random seed.
     */
    public void generateWaves(){
        Integer[] a = new Integer[spawnWave.length];
        for (int i=0;i < spawnWave.length;++i){
            a[i]=i;
        }

        shuffled = Arrays.asList(a);

        Collections.shuffle(shuffled, new Random (randomSeed));


        for(int i=0; i< shuffled.size(); i++){
            System.out.println(shuffled.get(i));
        }

    }

    /**
     * currentWave counts until the number of waves and then resets to zero.
     * Since shuffled is a randomized number between the size of spawnwave and 0 it can be used
     * to spawn the nextwave in a random order.
     *
     * In future I will need to decide if waves will have more random elements
     *
     * @param index
     * @param enemies
     */
    public void nextWave(int index, Array<Enemy> enemies) {
        spawnWave[shuffled.get(currentWave)].spawnWave(enemies);

        currentWave++;
        if(currentWave >= spawnWave.length){
            currentWave = 0;
        }
    }


    public Array<Array<Enemy>> getIncomingWaves() {
        return incomingWaves;
    }

    public void setIncomingWaves(Array<Array<Enemy>> incomingWaves) {
        this.incomingWaves = incomingWaves;
    }
}
