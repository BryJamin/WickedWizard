package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.maps.rooms.spawns.RoomEnemyWaves;

/**
 * Created by Home on 24/12/2016.
 */
public class BattleRoom extends Room {

    private RoomEnemyWaves roomEnemyWaves;

    private int numberOfWaves = 0;

    public BattleRoom(){
        super();
        roomEnemyWaves = new RoomEnemyWaves(this);
        //arenaWaves.nextWaveTest(this.getEnemies());
        //arenaWaves.nextWave(this.getEnemies());
    }

    public BattleRoom(int numberOfWaves){
        super();

        this.numberOfWaves = numberOfWaves;

        roomEnemyWaves = new RoomEnemyWaves(this);
        //arenaWaves.nextWaveTest(this.getEnemies());
        //arenaWaves.nextWave(this.getEnemies());
    }



    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        if(state != STATE.ENTRY && state != STATE.EXIT) {

            if (numberOfWaves == 0 && getRoomEnemyUpdater().areAllEnemiesKilled()) {
                unlock();
            } else {
                lock();
                if (getRoomEnemyUpdater().areAllEnemiesKilled()) {
                    roomEnemyWaves.nextWaveTest(this.getEnemies());
                    numberOfWaves--;
                }
            }

        }

    }




    public void draw(SpriteBatch batch){
        super.draw(batch);
    }









}
