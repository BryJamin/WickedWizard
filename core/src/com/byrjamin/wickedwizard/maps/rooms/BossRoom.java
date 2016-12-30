package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.byrjamin.wickedwizard.enemy.bosses.BiggaBlobba;

/**
 * Created by Home on 24/12/2016.
 */
public class BossRoom extends Room {


    public BossRoom(){
        super();
        getArenaSpawner().getSpawnedEnemies().add(new BiggaBlobba(1100, 2000));
    }


    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        if(state != STATE.ENTRY && state != STATE.EXIT) {
            state = getArenaSpawner().areAllEnemiesKilled() ? STATE.UNLOCKED : STATE.LOCKED;
        }

    }


}
