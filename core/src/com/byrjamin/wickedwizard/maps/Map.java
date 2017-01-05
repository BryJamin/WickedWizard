package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.maps.rooms.BattleRoom;
import com.byrjamin.wickedwizard.maps.rooms.BossRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.player.Wizard;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {

    private Room[] rooms;
    private int activeRoom;

    public Map(){

        rooms = new Room[]{null, new BattleRoom(), new BattleRoom(), new BattleRoom(), new BattleRoom(), new BossRoom(), null};
        roomSetup();
        activeRoom = 1;

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

            if(rooms[activeRoom].isExitTransitionFinished()){
                if(rooms[activeRoom].isExitPointRight()){
                    Wizard w = rooms[activeRoom].getWizard();
                    activeRoom++;
                    rooms[activeRoom].setWizard(w);
                    rooms[activeRoom].enterRoom(Room.ENTRY_POINT.LEFT);
                } else if(rooms[activeRoom].isExitPointLeft()){
                    Wizard w = rooms[activeRoom].getWizard();
                    activeRoom--;
                    rooms[activeRoom].enterRoom(Room.ENTRY_POINT.RIGHT);
                    rooms[activeRoom].setWizard(w);
                    rooms[activeRoom].getWizard().setCurrentState(Wizard.STATE.STANDING);
                }
            }
    }

    public boolean isTransitioning(){
        return rooms[activeRoom].state == Room.STATE.ENTRY || rooms[activeRoom].state == Room.STATE.EXIT;
    }


    public void draw(SpriteBatch batch){
        rooms[activeRoom].draw(batch);
    }

    public Room getActiveRoom() {
        return rooms[activeRoom];
    }
}
