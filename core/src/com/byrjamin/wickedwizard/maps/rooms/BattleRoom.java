package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.maps.MapCoords;

/**
 * Created by Home on 24/12/2016.
 */
public class BattleRoom extends Room {

    private int numberOfWaves = 1;

    public BattleRoom(MapCoords mapCoords){
        super(mapCoords);
    }

    public BattleRoom(int numberOfWaves, MapCoords mapCoords){
        super(mapCoords);

        this.numberOfWaves = numberOfWaves;
    }



    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        //if(state != STATE.ENTRY && state != STATE.EXIT) {

           // System.out.println(state);

            if (getRoomEnemyUpdater().areAllWavesKilled()) {
                unlock();
            } else {
                lock();
                if (getRoomEnemyUpdater().areAllWavesKilled()) {
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
