package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;
import com.byrjamin.wickedwizard.sprites.enemies.EnemyPresets;

/**
 * Created by Home on 10/12/2016.
 */
public class Events {

    private Array<Array<Enemy>> incomingWaves;

    private Arena arena;

    private Array<Enemy> enemyArray;




    public Events(Arena a){
        arena = a;
    }


    public void blob(Array<Enemy> e){
        e.clear();
        e.add(EnemyPresets.slowBlob(arena.ARENA_WIDTH, arena.groundHeight()));
    }

    public void turret(Array<Enemy> e){
        e.clear();
        e.add(EnemyPresets.slowTurret(arena.ARENA_WIDTH, arena.ARENA_HEIGHT - MainGame.GAME_UNITS * 11));
    }

    public void generateWaves(){



    }

    interface Waves {
        void spawnWave(Array<Enemy> enemies);
    }

    private Waves[] spawnWave = new Waves[] {
            new Waves() { public void spawnWave(Array<Enemy> enemies) { blob(enemies); } },
            new Waves() { public void spawnWave(Array<Enemy> enemies) { turret(enemies); } },
            new Waves() { public void spawnWave(Array<Enemy> enemies) { turret(enemies); } },
            new Waves() { public void spawnWave(Array<Enemy> enemies) { blob(enemies); } },
    };


    public void nextWave(int index, Array<Enemy> enemies) {
        spawnWave[index].spawnWave(enemies);
    }


    public Array<Array<Enemy>> getIncomingWaves() {
        return incomingWaves;
    }

    public void setIncomingWaves(Array<Array<Enemy>> incomingWaves) {
        this.incomingWaves = incomingWaves;
    }
}
