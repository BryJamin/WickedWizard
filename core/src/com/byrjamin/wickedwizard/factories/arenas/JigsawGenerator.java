package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;

import java.util.Random;

import static com.byrjamin.wickedwizard.factories.arenas.ArenaShellFactory.createOmniArena;

/**
 * Created by Home on 22/03/2017.
 */

public class JigsawGenerator {

    private int noBattleRooms;

    private Random rand;

    private Arena startingArena;

    public boolean generateTutorial = true;

    public JigsawGenerator(int noBattleRooms, Random rand){
        this.noBattleRooms = noBattleRooms;
        this.rand = rand;
    }


    public ObjectSet<MapCoords> createUnavaliableMapCoords(Array<Arena> arenas){
        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();
        for(Arena a : arenas){
            unavaliableMapCoords.addAll(a.getCotainingCoords());
        }
        return unavaliableMapCoords;
    }

    public Array<Arena> generateMapAroundPresetPoints(Array<Arena> presetRooms, Array<Arena> arenas,
                                                      OrderedSet<DoorComponent> avaliableDoorsSet){

        Array<Arena> placedArenas = new Array<Arena>();

        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();

        for(Arena a : presetRooms){
            placedArenas.add(a);
            unavaliableMapCoords.addAll(a.getCotainingCoords());
        }

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

    public Array<Arena> generateMapAroundPresetPoints(Array<Arena> presetRooms, Array<ArenaGen> arenaGenArray,
                                                      OrderedSet<DoorComponent> avaliableDoorsSet, int noOfRoomsPlaced){

        Array<Arena> placedArenas = new Array<Arena>();

        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();

        for(Arena a : presetRooms){
            placedArenas.add(a);
            unavaliableMapCoords.addAll(a.getCotainingCoords());
        }


        int placedRooms = 0;

        while(placedRooms < noOfRoomsPlaced && arenaGenArray.size > 0) {
            int i = rand.nextInt(arenaGenArray.size);
            Arena nextRoomToBePlaced = arenaGenArray.get(i).createArena();
            if(placeRoomUsingDoors(nextRoomToBePlaced, avaliableDoorsSet, unavaliableMapCoords, rand)){
                placedArenas.add(nextRoomToBePlaced);
                unavaliableMapCoords.addAll(nextRoomToBePlaced.getCotainingCoords());
                for (DoorComponent dc : nextRoomToBePlaced.getDoors()) {
                    if(!unavaliableMapCoords.contains(dc.leaveCoords)) {
                        avaliableDoorsSet.add(dc);
                    }
                }
                placedRooms++;
            } else {
                //TODO expand this to retry and replace the same set of rooms. Or just use a subset of omni rooms to hit
                //TODO the room count as omni rooms can fir in most areas.
                arenaGenArray.removeValue(arenaGenArray.get(i), false);
            }
        }


        return placedArenas;

    }


    public Array<Arena> generate(){

        if(generateTutorial){
            return generateTutorialJigsaw();
        } else {
            return generateJigsaw();
        }


    }

    public Array<Arena> generateTutorialJigsaw(){

        Array<Arena> placedArenas = new Array<Arena>();

        startingArena = TutorialFactory.groundMovementTutorial(new MapCoords(0,0));

        //startingArena =  ItemRoomFactory.createItemTestRoom(new MapCoords(0,0));

        startingArena.addEntity(ChestFactory.chestBag(500, 700));
        startingArena.addEntity(ChestFactory.chestBag(800, 700));
        startingArena.addEntity(ChestFactory.chestBag(900, 700));
        startingArena.addEntity(ChestFactory.chestBag(1200, 700));
        startingArena.addEntity(ChestFactory.chestBag(1400, 700));
        startingArena.addEntity(ChestFactory.chestBag(1700, 700));
        //startingArena =  ShopFactory.createShop(new MapCoords(0,0));

        //startingArena.addEntity(DeathFactory.worldPortal(600, 600));

        placedArenas.add(startingArena);
        placedArenas.add(TutorialFactory.jumpTutorial(new MapCoords(1, 0)));
        placedArenas.add(TutorialFactory.enemyTurtorial(new MapCoords(2,3)));
        placedArenas.add(TutorialFactory.grappleTutorial(new MapCoords(2,0)));
        placedArenas.add(TutorialFactory.endTutorial(new MapCoords(3,3)));

        Arena f = ArenaShellFactory.createOmniArena(new MapCoords(4,3));
        placedArenas.add(f);

        OrderedSet<DoorComponent> avaliableDoorsSet = new OrderedSet<DoorComponent>();
        avaliableDoorsSet.addAll(f.getDoors());


        placedArenas = generateMapAroundPresetPoints(placedArenas,Level1Rooms.getLevel1RoomArray(), avaliableDoorsSet, 5);

        Arena itemRoom = ItemArenaShellFactory.createItemRoom();
        if(placeRoomUsingDoors(itemRoom, avaliableDoorsSet, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(itemRoom);
        }

        Arena shopRoom = ShopFactory.createShop();
        if(placeRoomUsingDoors(shopRoom, avaliableDoorsSet, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(shopRoom);
        }

        //RoomFactory.cleanArenas(placedArenas);
        Arena bossRoom = ArenaShellFactory.createWidth2Arena();
        bossRoom.roomType = Arena.RoomType.BOSS;
        RoomDecorationFactory.biggablobba(bossRoom);

        int range = (int) ((Math.sqrt(placedArenas.size) - 7 /*tutorial rooms */) / 2);
        if (placeRoomAtRangeWithDoors(bossRoom,
                avaliableDoorsSet,createUnavaliableMapCoords(placedArenas), rand, range)) {
            placedArenas.add(bossRoom);
        }

        return placedArenas;

    }

    public Array<Arena> generateJigsaw() {
        Array<Arena> placedArenas = new Array<Arena>();
        //startingArena = ItemRoomFactory.createItemTestRoom(new MapCoords(0,0));
        startingArena = ArenaShellFactory.createOmniArena();
        placedArenas.add(startingArena);

        OrderedSet<DoorComponent> avaliableDoorsSet = new OrderedSet<DoorComponent>();
        avaliableDoorsSet.addAll(startingArena.getDoors());

        placedArenas = generateMapAroundPresetPoints(placedArenas,Level1Rooms.getLevel1RoomArray(), avaliableDoorsSet, noBattleRooms);


        Arena itemRoom = ItemArenaShellFactory.createItemRoom();
        if(placeRoomUsingDoors(itemRoom, avaliableDoorsSet, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(itemRoom);
        }

        Arena shopRoom = ShopFactory.createShop();
        if(placeRoomUsingDoors(shopRoom, avaliableDoorsSet, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(shopRoom);
        }

        Arena bossRoom = ArenaShellFactory.createWidth2Arena();
        bossRoom.roomType = Arena.RoomType.BOSS;
        RoomDecorationFactory.biggablobba(bossRoom);



        int range = (int) ((Math.sqrt(placedArenas.size) - 1) / 2);
        if (placeRoomAtRangeWithDoors(bossRoom,
                avaliableDoorsSet,createUnavaliableMapCoords(placedArenas), rand, range)) {
            placedArenas.add(bossRoom);
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


    public boolean placeRoomAtRangeWithDoors(Arena arena, OrderedSet<DoorComponent> avaliableDoorsSet,
                                             ObjectSet<MapCoords> unavaliableMapCoords,
                                             Random rand, int minRange){

        Array<DoorComponent> avaliableDoorsArray = new Array<DoorComponent>();
        avaliableDoorsArray.addAll(avaliableDoorsSet.orderedItems());

        OrderedSet<DoorComponent> newavaliableDoorsSet = new OrderedSet<DoorComponent>();

        for(int i = avaliableDoorsArray.size - 1; i >= 0; i--) {
            DoorComponent dc = avaliableDoorsArray.get(i);
            if(dc.leaveCoords.getX() <= minRange && dc.leaveCoords.getY() <= minRange &&
                    dc.leaveCoords.getX() >= -minRange && dc.leaveCoords.getY() >= -minRange ) {
                avaliableDoorsArray.removeValue(dc, false);
            }
        }


        newavaliableDoorsSet.addAll(avaliableDoorsArray);

        if(newavaliableDoorsSet.size != 0) {
            return placeRoomUsingDoors(arena, newavaliableDoorsSet, unavaliableMapCoords, rand);
        }

        return false;

    }


/*    public boolean placeRoomAtRange(Room room, OrderedSet<RoomExit> avaliableExitsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand, int minRange){
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

    }*/



    /**
     * Generates the co-ordinates that will be used to mock the moving of a room into a different position
     * and if the mock is successfull these co-ordinates will be used to move the room in the free position
     * @param newPosition - The Co-ordinate position of a potential spot for the room to placed.
     * @param oldPosition - The current positition of the room
     * @return - Returns A MapCoord object containing the x and y value needed to move the room
     */
    public MapCoords generateShiftCoords(MapCoords newPosition, MapCoords oldPosition) {
        int diffX = newPosition.getX() - oldPosition.getX();
        int diffY = newPosition.getY() - oldPosition.getY();
        return new MapCoords(diffX, diffY);
    }


    /**
     * Takes in an arena and uses the shift co-ordinates to 'mock' moving the arena into that position
     * by moving it's map co-ordinates
     * @param a - The arena that will have new position mocked up.
     * @param shiftCoords - The co-ordinates that will be used to move the arena
     * @return - An array of new co-ordinates that the arena potentially can move to assuming no other arena is
     * taking up that spot
     */
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

    public Array<Room> generateTutorialJigsaw(){


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
