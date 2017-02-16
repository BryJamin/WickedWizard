package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.spawns.RoomEnemyWaves;

/**
 * Created by Home on 24/12/2016.
 */
public class BattleRoom extends Room {

    private RoomEnemyWaves roomEnemyWaves;

    private int numberOfWaves = 1;

    public BattleRoom(MapCoords mapCoords){
        super(mapCoords);
        roomEnemyWaves = new RoomEnemyWaves(this);
    }

    public BattleRoom(int numberOfWaves, MapCoords mapCoords){
        super(mapCoords);

        this.numberOfWaves = numberOfWaves;
    }



    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        //if(state != STATE.ENTRY && state != STATE.EXIT) {

           // System.out.println(state);

            if (getRoomEnemyUpdater().areAllEnemiesKilled()) {
                unlock();
            } else {
                lock();
                if (getRoomEnemyUpdater().areAllEnemiesKilled()) {
                    //roomEnemyWaves.nextWaveTest(this.getEnemies());
                    //numberOfWaves--;
                }
            }

        //}

    }




    public void draw(SpriteBatch batch){
        super.draw(batch);
    }









}
