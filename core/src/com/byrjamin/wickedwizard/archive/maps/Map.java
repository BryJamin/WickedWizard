package com.byrjamin.wickedwizard.archive.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.archive.gameobject.player.Wizard;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import java.util.Random;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {

    private Array<com.byrjamin.wickedwizard.archive.maps.rooms.Room> roomArray = new Array<com.byrjamin.wickedwizard.archive.maps.rooms.Room>();
    private OrderedSet<com.byrjamin.wickedwizard.archive.maps.rooms.Room> visitedRoomArray = new OrderedSet<com.byrjamin.wickedwizard.archive.maps.rooms.Room>();
    private MapGUI mapGUI;
    private com.byrjamin.wickedwizard.archive.maps.rooms.Room currentRoom;
    private MapJigsawGenerator mjg;

    private OrthographicCamera gamecam;
    public Map(){

        Random rand = new Random();
        mjg = new MapJigsawGenerator(1, rand);

        //TODO first generate the map
        roomArray = mjg.generateJigsaw();
        currentRoom = mjg.getStartingRoom();

        //TODO use the layouts to add to the room
        for(com.byrjamin.wickedwizard.archive.maps.rooms.Room room : roomArray) {

            for(com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomWall rw : room.getRoomWalls()) {
                rw.wallSetUp(PlayScreen.atlas.findRegions("brick"));
            }
            room.getRoomBackground().backgroundSetUp(PlayScreen.atlas.findRegions("backgrounds/wall"));

            if(room instanceof com.byrjamin.wickedwizard.archive.maps.rooms.BattleRoom) {
                switch (room.getLayout()) {
                    case OMNI:
                        com.byrjamin.wickedwizard.archive.maps.rooms.spawns.OmniBattleRooms.spawnWave[rand.nextInt(com.byrjamin.wickedwizard.archive.maps.rooms.spawns.OmniBattleRooms.spawnWave.length)].spawnWave(room);
                        //OmniBattleRooms.spawnWave[4].spawnWave(room);
                        //OmniBattleRooms.bouncerLarge(room);
                        //OmniBattleRooms.bouncer(room);
                        //OmniBattleRooms.kugelDuscheTwoBullets(room);
                        break;
                    case HEIGHT_2:
                        break;
                    default:
                        com.byrjamin.wickedwizard.archive.maps.rooms.spawns.OmniBattleRooms.spawnWave[rand.nextInt(com.byrjamin.wickedwizard.archive.maps.rooms.spawns.OmniBattleRooms.spawnWave.length)].spawnWave(room);
                      //  rew.spawnWave[3].spawnWave(room);
                        break;
                }
            }
        }

        Random random = new Random();
/*        if(random.nextBoolean()) {
            visitedRoomArray.add(currentRoom);
        } else {
            visitedRoomArray.addAll(roomArray);
        }*/
        //visitedRoomArray.add(currentRoom);
        addAdjacentRoomsToVisitedRooms(currentRoom);
        //visitedRoomArray.addAll(roomArray);

        mapGUI = new MapGUI(0,0,visitedRoomArray, currentRoom);
    }

    public void addAdjacentRoomsToVisitedRooms(com.byrjamin.wickedwizard.archive.maps.rooms.Room currentRoom){
        visitedRoomArray.add(currentRoom);
        for(com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit re : currentRoom.getRoomExits()){
            com.byrjamin.wickedwizard.archive.maps.rooms.Room r = findRoom(re.getLeaveCoords());
            if(r != null){
                visitedRoomArray.add(r);
            }
        }
    }


    public void update(float dt, OrthographicCamera gamecam){

        this.gamecam = gamecam;

        currentRoom.update(dt, gamecam);

        if(currentRoom.isExitTransitionFinished()){
            com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit currentExit = currentRoom.getCurrentExit();
            Wizard w = currentRoom.getWizard();
            currentRoom = findRoom(currentExit.getLeaveCoords());
            currentRoom.enterRoom(w, currentExit.getRoomCoords(), currentExit.getLeaveCoords());

            addAdjacentRoomsToVisitedRooms(currentRoom);
            //visitedRoomArray.add(currentRoom);



            System.out.println("VISITED ROOM SIZE IS :" + visitedRoomArray.size);
        }

        mapGUI.update(dt, gamecam, visitedRoomArray, currentRoom);

    }


    public com.byrjamin.wickedwizard.archive.maps.rooms.Room findRoom(MapCoords mc){

        for(com.byrjamin.wickedwizard.archive.maps.rooms.Room r : roomArray) {
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

    public com.byrjamin.wickedwizard.archive.maps.rooms.Room getActiveRoom() {
        return currentRoom;
    }
}
