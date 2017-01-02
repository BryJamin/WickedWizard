package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.maps.rooms.helper.ArenaWaves;

/**
 * Created by Home on 24/12/2016.
 */
public class BattleRoom extends Room {

    private ArenaWaves arenaWaves;

    private int numberOfWaves = 2;

    public BattleRoom(){
        super();
        arenaWaves = new ArenaWaves(this);
        //arenaWaves.nextWave(this.getEnemies());
    }



    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        if(state != STATE.ENTRY && state != STATE.EXIT) {

            if (numberOfWaves == 0 && getArenaSpawner().areAllEnemiesKilled()) {
                state = STATE.UNLOCKED;
            } else {
                state = STATE.LOCKED;
                if (getArenaSpawner().areAllEnemiesKilled()) {
                    arenaWaves.nextWaveTest(this.getEnemies());
                    numberOfWaves--;
                    System.out.println("NUMBER OF WAVES: " + numberOfWaves);
                }
            }

        }

    }




    public void draw(SpriteBatch batch){
        super.draw(batch);
    }









}
