package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.maps.rooms.BattleRoom;
import com.byrjamin.wickedwizard.maps.rooms.BossRoom;
import com.byrjamin.wickedwizard.maps.rooms.ItemRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;
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
 * Created by Home on 14/02/2017.
 */
public class MapJigsawGenerator {

    private int noBattleRooms;
    private int noItemRooms = 1;
    private int noBossRooms = 1;

    private Random rand;

    private Room startingRoom;


    public MapJigsawGenerator(int totalRooms, Random rand){
        this.noBattleRooms = totalRooms - noItemRooms - noBossRooms - 1;
        this.rand = rand;
    }

    public Array<Room> generateJigsaw(){

        BasicRoomLayout t = new BasicRoomLayout();
        Height2Layout height2Layout = new Height2Layout();
        Width2Layout width2Layout = new Width2Layout();

        //TODO hasn't been properly built but can slot in fine
        LBlockLayout lBlockLayout = new LBlockLayout();

        Array<Room> roomPieces = new Array<Room>();
        Array<Room> bossPieces = new Array<Room>();
        Array<Room> itemPieces = new Array<Room>();
        Array<Room> actualRooms = new Array<Room>();

        for(int i = 0; i < noBattleRooms; i++) {
            Room r = new BattleRoom(new MapCoords(0,0));
            if(i == 1){
                height2Layout.applyLayout(r);
            } else if(i == 2){
                width2Layout.applyLayout(r);
            } else {
                t.applyLayout(r);
            }
            roomPieces.add(r);
        }

        for(int i = 0; i < noBossRooms; i++) {
            Room r = new BossRoom(new MapCoords(0,0));
            t.applyLayout(r);
            bossPieces.add(r);
        }

        for(int i = 0; i < noItemRooms; i++) {
            Room r = new ItemRoom(new MapCoords(0,0));
            t.applyLayout(r);
            itemPieces.add(r);
        }


        TutorialRoom tRoom = new TutorialRoom(new MapCoords(0,0));
        t.applyLayout(tRoom);
        startingRoom = tRoom;

        actualRooms.add(tRoom);

        int totalRooms = roomPieces.size + itemPieces.size + bossPieces.size;

        OrderedSet<MapCoords> avaliableMapCoordsSet = new OrderedSet<MapCoords>();
        OrderedSet<RoomExit> avaliableExitsSet = new OrderedSet<RoomExit>();


        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();
        unavaliableMapCoords.addAll(tRoom.getMapCoordsArray());

        avaliableMapCoordsSet.addAll(tRoom.getAdjacentMapCoords());
        avaliableExitsSet.addAll(tRoom.getRoomExits());

        while(roomPieces.size > 0) {
            int i = rand.nextInt(roomPieces.size);
            Room nextRoomToBePlaced = roomPieces.get(i);
            roomPieces.removeIndex(i);
            if(placeRoomUsingDoors(nextRoomToBePlaced, avaliableExitsSet, unavaliableMapCoords, rand)){
                actualRooms.add(nextRoomToBePlaced);
            }
            unavaliableMapCoords.addAll(nextRoomToBePlaced.getMapCoordsArray());
            for (RoomExit re : nextRoomToBePlaced.getRoomExits()) {
                if(!unavaliableMapCoords.contains(re.getLeaveCoords())) {
                    avaliableExitsSet.add(re);
                }
            }
        }

        for(Room r : itemPieces){
            int range = (int) Math.floor(Math.sqrt(totalRooms));
            if(placeRoomUsingDoors(r, avaliableExitsSet, unavaliableMapCoords, rand)) {
                actualRooms.add(r);
                unavaliableMapCoords.addAll(r.getMapCoordsArray());
                unavaliableMapCoords.addAll(r.getAdjacentMapCoords());
            }
        }

        int range = (int) ((Math.sqrt(totalRooms) - 1) / 2) + 1;
        System.out.println("range is " + range);
        for(Room r : bossPieces){
            //TODO if you find out a way to print out 1000 maps with this method see if -1 range is better than +0 range
            if(placeRoomAtRange(r, avaliableExitsSet, unavaliableMapCoords, rand, range)) {
                actualRooms.add(r);
            }
        }

        cleanLeafs(actualRooms);

        return actualRooms;

    }






    public boolean placeRoomUsingDoors(Room room, OrderedSet<RoomExit> avaliableExitsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand){

        Array<RoomExit> avaliableExitsArray = new Array<RoomExit>();
        avaliableExitsArray.addAll(avaliableExitsSet.orderedItems());


        boolean roomPlaced = false;

        while(!roomPlaced) {

            if(avaliableExitsArray.size <= 0){
                break;
            }

            int avaliableExitSelector = rand.nextInt(avaliableExitsArray.size);
            //The available co-ordinates we can shift to.
            RoomExit selectedAvaliableExit = avaliableExitsArray.get(avaliableExitSelector);

            avaliableExitsArray.removeIndex(avaliableExitSelector);

            Array<RoomExit> linkableExitsArray = new Array<RoomExit>();

            for(RoomExit re : room.getRoomExits()) {
                switch (selectedAvaliableExit.getDirection()){
                    case LEFT: if(re.getDirection() == RoomExit.EXIT_DIRECTION.RIGHT)
                        linkableExitsArray.add(re);
                        break;
                    case RIGHT: if(re.getDirection() == RoomExit.EXIT_DIRECTION.LEFT)
                        linkableExitsArray.add(re);
                        break;
                    case UP: if(re.getDirection() == RoomExit.EXIT_DIRECTION.DOWN)
                        linkableExitsArray.add(re);
                        break;
                    case DOWN: if(re.getDirection() == RoomExit.EXIT_DIRECTION.UP)
                        linkableExitsArray.add(re);
                        break;
                }
            }

            while(!roomPlaced && linkableExitsArray.size > 0) {

                if(linkableExitsArray.size <= 0){
                    break;
                }

                int linkableExitsSelector = rand.nextInt(linkableExitsArray.size);
                //The available co-ordinates we can shift to.
                RoomExit selectedLinkableExit = linkableExitsArray.get(linkableExitsSelector);

                linkableExitsArray.removeIndex(linkableExitsSelector);

                MapCoords shiftCoords = generateShiftCoords(selectedAvaliableExit.getLeaveCoords(), selectedLinkableExit.getRoomCoords());

                //Mocks moving the room
                Array<MapCoords> mockCoords = room.mockShiftCoordinatePosition(shiftCoords);

                for (int j = 0; j < mockCoords.size; j++) {
                    if (!unavaliableMapCoords.contains(mockCoords.get(j))) {
                        roomPlaced = true;
                    } else {
                        roomPlaced = false;
                        break;
                    }
                }

                if (roomPlaced) {
                    room.shiftCoordinatePosition(shiftCoords);
                    avaliableExitsSet.remove(selectedAvaliableExit);
                }

            }

        }

        return roomPlaced;

    }


    public boolean placeRoomAtRange(Room room, OrderedSet<RoomExit> avaliableExitsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand, int minRange){
        Array<RoomExit> avaliableMapCoordsArray = new Array<RoomExit>();
        avaliableMapCoordsArray.addAll(avaliableExitsSet.orderedItems());

        OrderedSet<RoomExit> newavaliableMapCoordsSet = new OrderedSet<RoomExit>();

        //TODO should be minRange from starting room's position inside of just guessing the room's position is 0;
        for(int i = avaliableMapCoordsArray.size - 1; i >= 0; i--) {
            RoomExit re = avaliableMapCoordsArray.get(i);
            if(re.getLeaveCoords().getX() < minRange && re.getLeaveCoords().getY() < minRange &&
                    re.getLeaveCoords().getX() > -minRange && re.getLeaveCoords().getY() > -minRange ) {
                avaliableMapCoordsArray.removeValue(re, false);
            }
        }

        newavaliableMapCoordsSet.addAll(avaliableMapCoordsArray);

        if(newavaliableMapCoordsSet.size != 0) {
            return placeRoomUsingDoors(room, newavaliableMapCoordsSet, unavaliableMapCoords, rand);
        }

        return false;

    }

    public Room getStartingRoom() {
        return startingRoom;
    }


    /**
     * Cleans up any stray Room exits that do not lead to another room.
     * @param rooms - The generated array of rooms
     */
    public void cleanLeafs(Array<Room> rooms){
        for(int j = rooms.size - 1; j >= 0; j--){
            for(int i = rooms.get(j).getRoomExits().size - 1; i >=0; i--) {
                RoomExit re = rooms.get(j).getRoomExits().get(i);
                if(!findDoor(re.getRoomCoords(), re.getLeaveCoords(), rooms)) {
                    if(re instanceof RoomDoor) {
                        rooms.get(j).replaceDoorwithWall((RoomDoor) re);
                        rooms.get(j).getRoomDoors().removeValue((RoomDoor) re, false);
                    } else if(re instanceof RoomGrate){
                        rooms.get(j).getRoomGrates().removeValue((RoomGrate) re, false);
                    }
                    rooms.get(j).getRoomExits().removeValue(re, false);
                }
            }
        }
    }

    public boolean findDoor(MapCoords EnterFrom, MapCoords LeaveTo, Array<Room> rooms){
        for(Room r : rooms) {
            if(r.containsExitWithCoords(EnterFrom, LeaveTo)){
                return true;
            }
        }
        return false;
    }

    public MapCoords generateShiftCoords(MapCoords newPosition, MapCoords oldPosition) {
        int diffX = newPosition.getX() - oldPosition.getX();
        int diffY = newPosition.getY() - oldPosition.getY();
        return new MapCoords(diffX, diffY);
    }




}
