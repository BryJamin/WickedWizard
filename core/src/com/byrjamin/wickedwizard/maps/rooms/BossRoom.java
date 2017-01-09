package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.byrjamin.wickedwizard.entity.bosses.BiggaBlobba;

/**
 * Created by Home on 24/12/2016.
 */
public class BossRoom extends Room {


    public BossRoom(){
        super();
        getRoomEnemyUpdater().getSpawnedEnemies().add(new BiggaBlobba(1100, 2000));
    }


    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        if(state != STATE.ENTRY && state != STATE.EXIT) {
            state = getRoomEnemyUpdater().areAllEnemiesKilled() ? STATE.UNLOCKED : STATE.LOCKED;
        }

    }


}
