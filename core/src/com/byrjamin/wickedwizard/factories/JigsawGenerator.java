package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.ecs.components.DoorComponent;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomGrate;

import java.util.Random;

/**
 * Created by Home on 22/03/2017.
 */

public class JigsawGenerator {

    private int noBattleRooms;

    private Random rand;

    private Arena startingArena;

    public JigsawGenerator(int noBattleRooms, Random rand){
        this.noBattleRooms = noBattleRooms;
        this.rand = rand;
    }

    public Array<Arena> generateJigsaw(){

        Array<Arena> arenas = new Array<Arena>();
        Array<Arena> placedArenas = new Array<Arena>();

        for(int i = 0; i < noBattleRooms; i++){

            Arena a;

            Array<SpawnerFactory.Spawner> s = new Array<SpawnerFactory.Spawner>();

            s.add(new SpawnerFactory.Spawner() {
                @Override
                public Bag<Component> spawnBag(float x, float y) {
                    return BlobFactory.blobBag(x,y);
                }
            });


            s.add(new SpawnerFactory.Spawner() {
                @Override
                public Bag<Component> spawnBag(float x, float y) {
                    return BlobFactory.smallblobBag(x,y);
                }
            });


            if(rand.nextBoolean()){
                a = RoomFactory.createWidth2Arena();
                a.addEntity(SpawnerFactory.spawnerBag(a.getWidth() / 2, a.getHeight() / 2, s));
            } else {
                a = RoomFactory.createOmniArena();
                a.addEntity(SpawnerFactory.spawnerBag(a.getWidth() / 2, a.getHeight() / 2, s));
            }

            arenas.add(a);
            //if(i == 1) {

            //}
            //arenas.add(RoomFactory.createOmniArena());
        }

        startingArena = arenas.first();


        OrderedSet<MapCoords> avaliableMapCoordsSet = new OrderedSet<MapCoords>();
        OrderedSet<DoorComponent> avaliableDoorsSet = new OrderedSet<DoorComponent>();
        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();

        unavaliableMapCoords.addAll(startingArena.getCotainingCoords());
        avaliableMapCoordsSet.addAll(startingArena.adjacentCoords);
        avaliableDoorsSet.addAll(startingArena.getDoors());

        arenas.removeValue(startingArena, true);
        placedArenas.add(startingArena);


        while(arenas.size > 0) {
            int i = rand.nextInt(arenas.size);
            Arena nextRoomToBePlaced = arenas.get(i);
            arenas.removeIndex(i);
            if(placeRoomUsingDoors(nextRoomToBePlaced, avaliableDoorsSet, unavaliableMapCoords, rand)){
                placedArenas.add(nextRoomToBePlaced);
            }
            unavaliableMapCoords.addAll(nextRoomToBePlaced.getCotainingCoords());
            for (DoorComponent dc : nextRoomToBePlaced.getDoors()) {
                if(!unavaliableMapCoords.contains(dc.leaveCoords)) {
                    avaliableDoorsSet.add(dc);
                }
            }
        }


        return placedArenas;

    }


    public boolean placeRoomUsingDoors(Arena room, OrderedSet<DoorComponent> avaliableDoorsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand){


        Array<DoorComponent> avaliableDoorsArray = new Array<DoorComponent>();
        avaliableDoorsArray.addAll(avaliableDoorsSet.orderedItems());

        boolean roomPlaced = false;

        while(!roomPlaced) {

            if(avaliableDoorsArray.size <= 0){
                break;
            }

            int avaliableDoorSelector = rand.nextInt(avaliableDoorsArray.size);
            //The available co-ordinates we can shift to.
            DoorComponent selectedAvaliableDoor = avaliableDoorsArray.get(avaliableDoorSelector);

            avaliableDoorsArray.removeIndex(avaliableDoorSelector);

            Array<DoorComponent> linkableDoorsArray = new Array<DoorComponent>();

            for(DoorComponent dc : room.getDoors()) {
                switch (selectedAvaliableDoor.exit){
                    case left: if(dc.exit == DoorComponent.DIRECTION.right)
                        linkableDoorsArray.add(dc);
                        break;
                    case right: if(dc.exit == DoorComponent.DIRECTION.left)
                        linkableDoorsArray.add(dc);
                        break;
                    case up: if(dc.exit == DoorComponent.DIRECTION.down)
                        linkableDoorsArray.add(dc);
                        break;
                    case down: if(dc.exit == DoorComponent.DIRECTION.up)
                        linkableDoorsArray.add(dc);
                        break;
                }
            }

            System.out.println(linkableDoorsArray.size + " Door arrasy size");

            while(!roomPlaced && linkableDoorsArray.size > 0) {
                if(linkableDoorsArray.size <= 0){
                    break;
                }

                int linkableExitsSelector = rand.nextInt(linkableDoorsArray.size);
                //The available co-ordinates we can shift to.
                DoorComponent selectedLinkableDoor = linkableDoorsArray.get(linkableExitsSelector);

                linkableDoorsArray.removeIndex(linkableExitsSelector);



                MapCoords shiftCoords = generateShiftCoords(selectedAvaliableDoor.leaveCoords, selectedLinkableDoor.currentCoords);

                //Mocks moving the room
                Array<MapCoords> mockCoords = mockShiftCoordinatePosition(room, shiftCoords);

                for (int j = 0; j < mockCoords.size; j++) {
                    if (!unavaliableMapCoords.contains(mockCoords.get(j))) {
                        roomPlaced = true;
                    } else {
                        roomPlaced = false;
                        break;
                    }
                }

                if (roomPlaced) {
                    shiftCoordinatePosition(room, shiftCoords);
                    avaliableDoorsSet.remove(selectedAvaliableDoor);
                }

            }

        }

        return roomPlaced;

    }


    public MapCoords generateShiftCoords(MapCoords newPosition, MapCoords oldPosition) {
        int diffX = newPosition.getX() - oldPosition.getX();
        int diffY = newPosition.getY() - oldPosition.getY();

        System.out.println(diffX);
        System.out.println(diffY);

        return new MapCoords(diffX, diffY);
    }



    public Array<MapCoords> mockShiftCoordinatePosition(Arena a, MapCoords shiftCoords){

        int diffX = shiftCoords.getX();
        int diffY = shiftCoords.getY();

        Array<MapCoords> mockCoords = new Array<MapCoords>();

        for(MapCoords mc : a.getCotainingCoords()) {
            mockCoords.add(new MapCoords(mc));
        }

        for(MapCoords m : mockCoords) {
            m.addX(diffX);
            m.addY(diffY);
        }

        return mockCoords;
    }

    public void shiftCoordinatePosition(Arena a, MapCoords shiftCoords){

        int diffX = shiftCoords.getX();
        int diffY = shiftCoords.getY();

        System.out.println(diffX);


        for(MapCoords m : a.getCotainingCoords()) {
            m.addX(diffX);
            m.addY(diffY);
        }

/*
        for(MapCoords m : a.getAdjacentCoords()) {
            m.addX(diffX);
            m.addY(diffY);
        }*/


        for(DoorComponent dc : a.getDoors()){
            dc.leaveCoords.add(diffX, diffY);
            dc.currentCoords.add(diffX, diffY);
        }
    }


    public Arena getStartingRoom() {
        return startingArena;
    }





}




/*


public class MapJigsawGenerator {

    public Array<Room> generateJigsaw(){


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

        int range = (int) ((Math.sqrt(totalRooms) - 1) / 2);
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
            if(re.getLeaveCoords().getX() <= minRange && re.getLeaveCoords().getY() <= minRange &&
                    re.getLeaveCoords().getX() >= -minRange && re.getLeaveCoords().getY() >= -minRange ) {
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




 */
