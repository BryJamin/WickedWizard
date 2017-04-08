package com.byrjamin.wickedwizard.archive.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Home on 24/12/2016.
 */
public class BattleRoom extends Room {

    public BattleRoom(com.byrjamin.wickedwizard.archive.maps.MapCoords mapCoords){
        super(mapCoords);
    }

    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);
        if (getRoomEnemyUpdater().areAllWavesKilled()) {
            unlock();
        } else {
            lock();
        }
    }




    public void draw(SpriteBatch batch){
        super.draw(batch);
    }









}
