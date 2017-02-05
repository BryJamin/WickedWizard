package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.byrjamin.wickedwizard.entity.bosses.BiggaBlobba;
import com.byrjamin.wickedwizard.maps.MapCoords;

/**
 * Created by Home on 24/12/2016.
 */
public class BossRoom extends Room {


    public BossRoom(MapCoords mapCoords){
        super(mapCoords);
        //TODO this is temporary as there currently isn't a way for enemies to tranverse through platforms,
        //customise platform placement and generally a lot of unfinished stuff.
        getRoomEnemyUpdater().getSpawnedEnemies().add(new BiggaBlobba(1100, HEIGHT / 2));
        getPlatforms().clear();
        setUpBoundaries();
    }


    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        if(state != STATE.ENTRY && state != STATE.EXIT) {
            if(getRoomEnemyUpdater().areAllEnemiesKilled()){
                unlock();
            } else {
                lock();
            }
        }

    }


}
