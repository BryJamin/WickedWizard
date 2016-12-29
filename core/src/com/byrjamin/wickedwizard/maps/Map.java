package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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

    private com.byrjamin.wickedwizard.maps.rooms.helper.RoomTransitionAnim roomTransitionAnim;

    private float SCREENMOVEMENT = 100f;

    private float timer;

    Vector2 screenWipePosition;

    public Map(){

        rooms = new Room[]{null, new BattleRoom(), new BattleRoom(), new BattleRoom(), new BattleRoom(), new BattleRoom(), null};


        roomSetup();


        activeRoom = 1;

        shapeRenderer = new ShapeRenderer();

    }

    public void roomSetup(){

        for(int i = 0; i < rooms.length; i++){

            if(rooms[i] == null){
                continue;
            }

            if(rooms[i - 1] != null){
                rooms[i].setLeftExit(true);
            }

            if(rooms[i + 1] != null){
                rooms[i].setRightExit(true);
            }

        }


    }




    public void update(float dt, OrthographicCamera gamecam){
            rooms[activeRoom].update(dt, gamecam);

            if(rooms[activeRoom].isExitTransitionFinished() && rooms[activeRoom].state == Room.STATE.EXIT){

                if(rooms[activeRoom].isExitPointRight()){
                    activeRoom++;
                    rooms[activeRoom].enterRoom(Room.ENTRY_POINT.LEFT);
                } else if(rooms[activeRoom].isExitPointLeft()){
                    activeRoom--;
                    rooms[activeRoom].enterRoom(Room.ENTRY_POINT.RIGHT);
                }

            }
    }


    public void draw(SpriteBatch batch){
        rooms[activeRoom].draw(batch);
    }

    public Room getActiveRoom() {
        return rooms[activeRoom];
    }
}
