package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.FoundarySkin;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.WeightedObject;
import com.byrjamin.wickedwizard.utils.WeightedRoll;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import java.util.Random;

/**
 * Created by Home on 22/03/2017.
 */

public class JigsawGenerator {

    private int noBattleRooms;

    private Random rand;

    private Arena startingArena;
    private AssetManager assetManager;
    private Level1Rooms level1Rooms;
    private TutorialFactory tutorialFactory;
    private ArenaShellFactory arenaShellFactory;
    private ItemArenaFactory itemArenaFactory;
    private ShopFactory shopFactory;
    private ArenaSkin arenaSkin;

    private DecorFactory decorFactory;

    public boolean generateTutorial = true;

    public JigsawGenerator(AssetManager assetManager, ArenaSkin arenaSkin, int noBattleRooms, Random rand){
        this.assetManager = assetManager;
        this.level1Rooms = new Level1Rooms(assetManager, arenaSkin);
        this.tutorialFactory = new TutorialFactory(assetManager, arenaSkin);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.itemArenaFactory = new ItemArenaFactory(assetManager, arenaSkin);
        this.shopFactory = new ShopFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.noBattleRooms = noBattleRooms;
        this.arenaSkin = arenaSkin;
        this.rand = rand;
    }

    public void setSkin(ArenaSkin arenaSkin) {
        this.level1Rooms = new Level1Rooms(assetManager, arenaSkin);
        this.tutorialFactory = new TutorialFactory(assetManager, arenaSkin);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.itemArenaFactory = new ItemArenaFactory(assetManager, arenaSkin);
        this.shopFactory = new ShopFactory(assetManager, arenaSkin);
    }

    public void setNoBattleRooms(int noBattleRooms){
        this.noBattleRooms = noBattleRooms;
    }

    public ArenaShellFactory getArenaShellFactory() {
        return arenaShellFactory;
    }

    public ObjectSet<MapCoords> createUnavaliableMapCoords(Array<Arena> arenas){
        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();
        for(Arena a : arenas){
            unavaliableMapCoords.addAll(a.getCotainingCoords());
        }
        return unavaliableMapCoords;
    }


    public Array<Arena> generateMapAroundPresetPoints(Array<Arena> presetRooms, Array<ArenaGen> arenaGenArray,
                                                      OrderedSet<DoorComponent> avaliableDoorsSet, int noOfRoomsPlaced){

        Array<Arena> placedArenas = new Array<Arena>();

        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();

        for(Arena a : presetRooms){
            placedArenas.add(a);
            unavaliableMapCoords.addAll(a.getCotainingCoords());
        }

        WeightedRoll<ArenaGen> roll = new WeightedRoll<ArenaGen>(rand);

        for(ArenaGen ag : arenaGenArray) {
            roll.addWeightedObject(new WeightedObject<ArenaGen>(ag, 20));
        }

        for(WeightedObject w : roll.getWeightedObjects()){
            System.out.println(w.getWeight());
        }


        int placedRooms = 0;
        int loops = 0;

        while(placedRooms < noOfRoomsPlaced && loops < noOfRoomsPlaced * 3) {
            int i = rand.nextInt(arenaGenArray.size);

            WeightedObject<ArenaGen> weightedObject = roll.rollForWeight();
            Arena nextRoomToBePlaced = weightedObject.obj().createArena();
            if(placeRoomUsingDoors(nextRoomToBePlaced, avaliableDoorsSet, unavaliableMapCoords, rand)){
                placedArenas.add(nextRoomToBePlaced);
                unavaliableMapCoords.addAll(nextRoomToBePlaced.getCotainingCoords());
                for (DoorComponent dc : nextRoomToBePlaced.getDoors()) {
                    if(!unavaliableMapCoords.contains(dc.leaveCoords)) {
                        avaliableDoorsSet.add(dc);
                    }
                }

                System.out.println("Weight " + weightedObject.getWeight());

                weightedObject.setWeight((weightedObject.getWeight() / 5 > 0) ? weightedObject.getWeight() / 5: 1);
                placedRooms++;
            } else {
                loops++;
                //TODO expand this to retry and replace the same set of rooms. Or just use a subset of omni rooms to hit
                //TODO the room count as omni rooms can fir in most areas.
                //arenaGenArray.removeValue(arenaGenArray.get(i), false);
            }
        }


        return placedArenas;

    }


    public Array<Arena> generate(){

        Array<Arena> arenas;

        if(generateTutorial){
            arenas = generateTutorialJigsaw();
        } else {
            arenas = generateJigsaw();
        }

        this.cleanArenas(arenas);

        return arenas;


    }

    public Array<Arena> generateTutorialJigsaw(){

        Array<Arena> placedArenas = new Array<Arena>();

        startingArena = tutorialFactory.groundMovementTutorial(new MapCoords(0,0));


        //startingArena =  ItemRoomFactory.createItemTestRoom(new MapCoords(0,0));

/*        startingArena.addEntity(ChestFactory.chestBag(500, 700));
        startingArena.addEntity(ChestFactory.chestBag(800, 700));
        startingArena.addEntity(ChestFactory.chestBag(900, 700));
        startingArena.addEntity(ChestFactory.chestBag(1200, 700));
        startingArena.addEntity(ChestFactory.chestBag(1400, 700));
        startingArena.addEntity(ChestFactory.chestBag(1700, 700));*/
        //startingArena =  ShopFactory.createShop(new MapCoords(0,0));

        //startingArena.addEntity(DeathFactory.worldPortal(600, 600));

        placedArenas.add(startingArena);
        placedArenas.add(tutorialFactory.jumpTutorial(new MapCoords(1, 0)));
        placedArenas.add(tutorialFactory.enemyTurtorial(new MapCoords(2,3)));
        placedArenas.add(tutorialFactory.grappleTutorial(new MapCoords(2,0)));
        placedArenas.add(tutorialFactory.endTutorial(new MapCoords(3,3)));

        Arena f = arenaShellFactory.createOmniArena(new MapCoords(4,3));
        placedArenas.add(f);

        OrderedSet<DoorComponent> avaliableDoorsSet = new OrderedSet<DoorComponent>();
        avaliableDoorsSet.addAll(f.getDoors());


        placedArenas = generateMapAroundPresetPoints(placedArenas,level1Rooms.getLevel1RoomArray(), avaliableDoorsSet, 5);

        Arena itemRoom = itemArenaFactory.createItemRoom();
        if(placeRoomUsingDoors(itemRoom, avaliableDoorsSet, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(itemRoom);
        }

        Arena shopRoom = shopFactory.createShop();
        if(placeRoomUsingDoors(shopRoom, avaliableDoorsSet, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(shopRoom);
        }

        //RoomFactory.cleanArenas(placedArenas);
        Arena bossRoom = arenaShellFactory.createWidth2Arena();
        bossRoom.roomType = Arena.RoomType.BOSS;
        new ArenaEnemyPlacementFactory(assetManager).biggablobba(bossRoom);

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
        startingArena = arenaShellFactory.createOmniArena();
        startingArena.addEntity(decorFactory.platform(500,500,1000));
        placedArenas.add(startingArena);

        OrderedSet<DoorComponent> avaliableDoorsSet = new OrderedSet<DoorComponent>();
        avaliableDoorsSet.addAll(startingArena.getDoors());

        placedArenas = generateMapAroundPresetPoints(placedArenas,level1Rooms.getLevel1RoomArray(), avaliableDoorsSet, noBattleRooms);


        Arena itemRoom = itemArenaFactory.createItemRoom();
        if(placeRoomUsingDoors(itemRoom, avaliableDoorsSet, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(itemRoom);
        }

        Arena shopRoom = shopFactory.createShop();
        if(placeRoomUsingDoors(shopRoom, avaliableDoorsSet, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(shopRoom);
        }

        Arena bossRoom = arenaShellFactory.createWidth2Arena();
        bossRoom.roomType = Arena.RoomType.BOSS;
        new ArenaEnemyPlacementFactory(assetManager).biggablobba(bossRoom);



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
                    case LEFT: if(dc.exit == Direction.RIGHT)
                        linkableDoorsArray.add(dc);
                        break;
                    case RIGHT: if(dc.exit == Direction.LEFT)
                        linkableDoorsArray.add(dc);
                        break;
                    case UP: if(dc.exit == Direction.DOWN)
                        linkableDoorsArray.add(dc);
                        break;
                    case DOWN: if(dc.exit == Direction.UP)
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

        for(DoorComponent dc : a.getDoors()){
            dc.leaveCoords.add(diffX, diffY);
            dc.currentCoords.add(diffX, diffY);
        }
    }


    public Arena getStartingRoom() {
        return startingArena;
    }


    public void cleanArenas(Array<Arena> arenas){
        for(int i = 0; i < arenas.size; i++) {
            Arena a = arenas.get(i);
            for(int j = a.getDoors().size - 1; j >=0; j--) {//for (DoorComponent dc : a.getDoors()) {
                DoorComponent dc = a.getDoors().get(j);
                if (!findDoorWithinFoundRoom(dc, arenas)) {
                    Bag<Component> bag = a.findBag(dc);
                    if (BagSearch.contains(ActiveOnTouchComponent.class, bag)) {
                        a.getBagOfEntities().remove(bag);
                    } else {
                        CollisionBoundComponent cbc = BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag);
                        if(cbc != null) {
                            a.getBagOfEntities().remove(bag);
                            a.addEntity(decorFactory.wallBag(cbc.bound.x, cbc.bound.y, cbc.bound.getWidth(), cbc.bound.getHeight(), arenaSkin));
                        }
                    }
                    a.adjacentCoords.removeValue(dc.leaveCoords, false);
                    a.doors.removeValue(dc, true);
                }
            }
        }
    }


    @SuppressWarnings("SimplifiableIfStatement")
    public boolean findDoorWithinFoundRoom(DoorComponent dc, Array<Arena> arenas){
        Arena a = findRoom(dc.leaveCoords, arenas);
        if(a != null) {
            //System.out.println("Find Door " + a.adjacentCoords.contains(dc.currentCoords, false));
            return a.adjacentCoords.contains(dc.currentCoords, false);
        }
        return false;
    }

    public static Arena findRoom(MapCoords mc, Array<Arena> arenas){
        for(Arena a : arenas){
            //System.out.println("Find room " + a.cotainingCoords.contains(mc, false));
            if(a.cotainingCoords.contains(mc, false)){
                return a;
            }
        }
        return null;
    }
}