package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.maps.rooms.BattleRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {




    //private Array<Room> rooms;

    private Room[] rooms;

    private int activeRoom;

    private boolean isTransitioning = true;

    ShapeRenderer shapeRenderer;

    private RoomTransitionAnim roomTransitionAnim;

    private float SCREENMOVEMENT = 100f;

    private float timer;

    Vector2 screenWipePosition;

    public Map(){

        rooms = new Room[]{new BattleRoom(), new BattleRoom(), new BattleRoom(), new BattleRoom(), new BattleRoom()};

        rooms[0] = new BattleRoom();
        rooms[1] = new BattleRoom();
        rooms[2] = new BattleRoom();

        activeRoom = 0;

        shapeRenderer = new ShapeRenderer();

    }




    public void update(float dt, OrthographicCamera gamecam){
            rooms[activeRoom].update(dt, gamecam);

            if(rooms[activeRoom].isExitTransitionFinished() && rooms[activeRoom].state == Room.STATE.EXIT){
                activeRoom++;
            }
    }


    public void draw(SpriteBatch batch){
        rooms[activeRoom].draw(batch);
    }

    public Room getActiveRoom() {
        return rooms[activeRoom];
    }
}
