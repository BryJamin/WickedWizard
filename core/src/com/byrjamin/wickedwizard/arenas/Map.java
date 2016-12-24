package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {

    private Array<Room> rooms;

    private Room activeRoom;


    public Map(){
        rooms = new Array<Room>();
        rooms.add(new BattleRoom());
        rooms.add(new BattleRoom());

        activeRoom = rooms.first();
    }




    public void update(float dt, OrthographicCamera gamecam){

        if(activeRoom != null) {

            activeRoom.update(dt, gamecam);

            if (activeRoom.isUnlocked() && rooms.size > 1) {
                rooms.removeIndex(0);
                activeRoom = rooms.get(0);
            }

        }

    }


    public void draw(SpriteBatch batch){
        activeRoom.draw(batch);
    }

    public Room getActiveRoom() {
        return activeRoom;
    }
}
