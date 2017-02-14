package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.rooms.BattleRoom;
import com.byrjamin.wickedwizard.maps.rooms.BossRoom;
import com.byrjamin.wickedwizard.maps.rooms.ItemRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.entity.player.Wizard;
import com.byrjamin.wickedwizard.maps.rooms.TutorialRoom;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomGrate;
import com.byrjamin.wickedwizard.maps.rooms.layout.BasicRoomLayout;
import com.byrjamin.wickedwizard.maps.rooms.layout.Height2Layout;
import com.byrjamin.wickedwizard.maps.rooms.layout.LBlockLayout;
import com.byrjamin.wickedwizard.maps.rooms.layout.Width2Layout;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import java.util.Random;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {

    private Array<Room> roomArray = new Array<Room>();
    private Array<Room> visitedRoomArray = new Array<Room>();
    private MapGUI mapGUI;
    private Room currentRoom;
    private MapJigsawGenerator mjg;

    private OrthographicCamera gamecam;

    ShapeRenderer mapRenderer = new ShapeRenderer();

    public Map(){

        Random rand = new Random();
        mjg = new MapJigsawGenerator(15, rand);
        roomArray = mjg.generateJigsaw();
        currentRoom = mjg.getStartingRoom();

        for(Room room : roomArray) {
            System.out.println(room.getStartCoords());
        }

        System.out.println("MAP SIZE IS SIZE" + roomArray.size);

        Random random = new Random();
/*        if(random.nextBoolean()) {
            visitedRoomArray.add(currentRoom);
        } else {
            visitedRoomArray.addAll(roomArray);
        }*/
        //visitedRoomArray.add(currentRoom);
        visitedRoomArray.addAll(roomArray);

        mapGUI = new MapGUI(0,0,visitedRoomArray, currentRoom);


    }


    public void update(float dt, OrthographicCamera gamecam){

        this.gamecam = gamecam;

        currentRoom.update(dt, gamecam);

        if(currentRoom.isExitTransitionFinished()){
            RoomExit currentExit = currentRoom.getCurrentExit();
            Wizard w = currentRoom.getWizard();
            currentRoom = findRoom(currentExit.getLeaveCoords());
            currentRoom.enterRoom(w, currentExit.getRoomCoords(), currentExit.getLeaveCoords());

            if(!visitedRoomArray.contains(currentRoom, true)){
                visitedRoomArray.add(currentRoom);
            }

            System.out.println("VISITED ROOM SIZE IS :" + visitedRoomArray.size);
        }

        mapGUI.update(dt, gamecam, visitedRoomArray, currentRoom);

    }


    public Room findRoom(MapCoords mc){

        for(Room r : roomArray) {
            if(r.containsCoords(mc)){
                return r;
            };
        }

        //TODO return an error
        return null;
    }



    public void draw(SpriteBatch batch){
        currentRoom.draw(batch);
        mapGUI.draw(batch);
    }

    public Room getActiveRoom() {
        return currentRoom;
    }
}
