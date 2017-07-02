package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.InCombatActionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LinkComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level1Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level2Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level3Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level4Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level5Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.TutorialFactory;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.Level1BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory;
import com.byrjamin.wickedwizard.factories.arenas.presets.ShopFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemVitaminC;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.WeightedObject;
import com.byrjamin.wickedwizard.utils.WeightedRoll;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Home on 22/03/2017.
 */

public class JigsawGenerator {

    private int noBattleRooms;

    private ChangeLevelSystem.Level currentLevel;

    private Random rand;

    private Arena startingArena;

    private ArenaMap startingMap;

    private AssetManager assetManager;
    private Level1Rooms level1Rooms;
    private Level2Rooms level2Rooms;
    private Level3Rooms level3Rooms;
    private Level4Rooms level4Rooms;
    private Level5Rooms level5Rooms;
    private Level1BossMaps level1BossMaps;
    private TutorialFactory tutorialFactory;
    private ArenaShellFactory arenaShellFactory;
    private ItemArenaFactory itemArenaFactory;
    private ShopFactory shopFactory;
    private ArenaSkin arenaSkin;


    private HashMap<BossTeleporterComponent, ArenaMap> mapTracker = new HashMap<BossTeleporterComponent, ArenaMap>();

    private Array<Item> itemPool;

    private com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;

    public boolean generateTutorial = true;

    public JigsawGenerator(AssetManager assetManager, ArenaSkin arenaSkin, int noBattleRooms, Array<Item> itemPool, Random rand){
        this.assetManager = assetManager;
        this.level1Rooms = new Level1Rooms(assetManager, arenaSkin, rand);
        this.level2Rooms = new Level2Rooms(assetManager, arenaSkin, rand);
        this.level3Rooms = new Level3Rooms(assetManager, arenaSkin, rand);
        this.level4Rooms = new Level4Rooms(assetManager, arenaSkin, rand);
        this.level5Rooms = new Level5Rooms(assetManager, arenaSkin, rand);
        this.tutorialFactory = new TutorialFactory(assetManager, arenaSkin);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.itemArenaFactory = new ItemArenaFactory(assetManager, arenaSkin);
        this.shopFactory = new ShopFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.level1BossMaps = new Level1BossMaps(assetManager, arenaSkin);
        this.noBattleRooms = noBattleRooms;
        this.arenaSkin = arenaSkin;
        this.itemPool = itemPool;
        this.rand = rand;
        this.currentLevel = ChangeLevelSystem.Level.ONE;
    }

    public void setSkin(ArenaSkin arenaSkin) {
        this.arenaSkin = arenaSkin;
        this.level1Rooms = new Level1Rooms(assetManager, arenaSkin, rand);
        this.level2Rooms = new Level2Rooms(assetManager, arenaSkin, rand);
        this.level3Rooms = new Level3Rooms(assetManager, arenaSkin, rand);
        this.level4Rooms = new Level4Rooms(assetManager, arenaSkin, rand);
        this.level5Rooms = new Level5Rooms(assetManager, arenaSkin, rand);
        this.level1BossMaps = new Level1BossMaps(assetManager, arenaSkin);
        this.tutorialFactory = new TutorialFactory(assetManager, arenaSkin);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.itemArenaFactory = new ItemArenaFactory(assetManager, arenaSkin);
        this.shopFactory = new ShopFactory(assetManager, arenaSkin);
    }

    public void setCurrentLevel(ChangeLevelSystem.Level currentLevel){
        this.currentLevel = currentLevel;
    }

    public void setNoBattleRooms(int noBattleRooms){
        this.noBattleRooms = noBattleRooms;
    }

    public ObjectSet<MapCoords> createUnavaliableMapCoords(Array<Arena> arenas){
        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();
        for(Arena a : arenas){
            unavaliableMapCoords.addAll(a.getCotainingCoords());
        }
        return unavaliableMapCoords;
    }

    public OrderedSet<DoorComponent> createAvaliableDoorSet(Array<Arena> arenas){
        OrderedSet<DoorComponent> avaliableDoors = new OrderedSet<DoorComponent>();

        Array<Arena> protectionFromIteratorError = new Array<Arena>();
        protectionFromIteratorError.addAll(arenas);

        for(Arena a : arenas){
            for(DoorComponent dc : a.doors){
                if(findRoom(dc.leaveCoords, protectionFromIteratorError) == null){
                    avaliableDoors.add(dc);
                }
            }
        }
        return avaliableDoors;
    }


    public Array<Arena> generateMapAroundPresetPoints(Array<Arena> presetRooms, Array<ArenaGen> arenaGenArray,
                                                      OrderedSet<DoorComponent> avaliableDoorsSet, int noOfRoomsPlaced){

        Array<Arena> placedArenas = new Array<Arena>();
        placedArenas.addAll(presetRooms);
        ObjectSet<MapCoords> unavaliableMapCoords = createUnavaliableMapCoords(presetRooms);


        WeightedRoll<ArenaGen> roll = new WeightedRoll<ArenaGen>(rand);
        for(ArenaGen ag : arenaGenArray) roll.addWeightedObject(new WeightedObject<ArenaGen>(ag, 20));

        int placedRooms = 0;
        int loops = 0;

        while(placedRooms < noOfRoomsPlaced && loops < noOfRoomsPlaced * 3) {

            WeightedObject<ArenaGen> weightedObject = roll.rollForWeight();
            Arena nextRoomToBePlaced = weightedObject.obj().createArena(new MapCoords());
            if(placeRoomUsingDoors(nextRoomToBePlaced, avaliableDoorsSet, unavaliableMapCoords, rand)){

                //TODO make this it's own method for mandatory doors within rooms.
                if(nextRoomToBePlaced.mandatoryDoors.size > 0) {

                    Array<Arena> mockPlacedArenas = new Array<Arena>();
                    mockPlacedArenas.addAll(placedArenas);

                    OrderedSet<DoorComponent> mockAvaliableDoorSet = new OrderedSet<DoorComponent>();
                    mockAvaliableDoorSet.addAll(mockAvaliableDoorSet);

                    boolean isAllDoorsUsed = true;

                    for (DoorComponent dc : nextRoomToBePlaced.mandatoryDoors) {
                        if(findDoorWithinFoundRoom(dc, mockPlacedArenas)) {
                            continue;
                        }

                        int tries = 0;
                        boolean placedRoom = false;
                        while(tries <= 10){
                            WeightedObject<ArenaGen> weightedArenaGen = roll.rollForWeight();
                            Arena nextInnerRoomToBePlaced = weightedArenaGen.obj().createArena(new MapCoords());
                            if(nextInnerRoomToBePlaced.mandatoryDoors.size == 0) {
                                if (fillMandatoryDoor(nextInnerRoomToBePlaced, dc, mockPlacedArenas, mockAvaliableDoorSet)) {
                                    weightedArenaGen.setWeight((weightedArenaGen.getWeight() / 5 > 0) ? weightedArenaGen.getWeight() / 5 : 1);
                                    placedRoom = true;
                                    break;
                                }
                            }
                            tries++;
                        }
                        if(!placedRoom) {
                            isAllDoorsUsed = false;
                        }
                        if(!isAllDoorsUsed) {
                            break;
                        }
                    }
                    if(isAllDoorsUsed){
                        int preSize = placedArenas.size;

                        placedArenas = mockPlacedArenas;
                        avaliableDoorsSet = mockAvaliableDoorSet;
                        unavaliableMapCoords = createUnavaliableMapCoords(placedArenas);
                        addArenaToMap(nextRoomToBePlaced, placedArenas, unavaliableMapCoords, avaliableDoorsSet);
                        weightedObject.setWeight((weightedObject.getWeight() / 5 > 0) ? weightedObject.getWeight() / 5 : 1);
                        int diff = placedArenas.size - preSize;
                        placedRooms+= diff;
                    }



                } else {
                    addArenaToMap(nextRoomToBePlaced, placedArenas, unavaliableMapCoords, avaliableDoorsSet);
                    weightedObject.setWeight((weightedObject.getWeight() / 5 > 0) ? weightedObject.getWeight() / 5 : 1);
                    placedRooms++;
                }




            } else {
                loops++;
                //TODO expand this to retry and replace the same set of rooms. Or just use a subset of omni rooms to hit
                //TODO the room count as omni rooms can fir in most areas.
                //arenaGenArray.removeValue(arenaGenArray.get(i), false);
            }
        }

        return placedArenas;

    }



    public boolean fillMandatoryDoor(Arena arena, DoorComponent mandatoryDoor, Array<Arena> placedArenas, OrderedSet<DoorComponent> avaliableDoors){

        OrderedSet<DoorComponent> doors = new OrderedSet<DoorComponent>();
        doors.add(mandatoryDoor);

        ObjectSet<MapCoords> unavaliableMapCoords = createUnavaliableMapCoords(placedArenas);

        int tries = 0;
        while(tries < 15) {

            if (placeRoomUsingDoors(arena, doors, unavaliableMapCoords, rand)) {
                addArenaToMap(arena, placedArenas, unavaliableMapCoords, avaliableDoors);
                return true;
            }
            tries++;
        }

        return false;
    }



    /**
     * Adds an Arena to an Array of arenas that have been placed inside of map. And checks if the doors can be added
     * to doors that are available by seeing if the doors point to an unavailable co-ordinate.
     * @param roomToBePlaced - The arena to be placed
     * @param placedArenas - The array of already placedArenas
     * @param unavaliableMapCoords - co-ordinates that are unavailiable for an arena inside of
     * @param avaliableDoorsSet - Set of doors that are avaliable for arenas to be attached to.
     */
    public void addArenaToMap(Arena roomToBePlaced, Array<Arena> placedArenas, ObjectSet<MapCoords> unavaliableMapCoords, OrderedSet<DoorComponent> avaliableDoorsSet) {
        placedArenas.add(roomToBePlaced);
        updateUnavaliableCoordsAndLeaveDoors(roomToBePlaced, unavaliableMapCoords, avaliableDoorsSet);
    }

    public void updateUnavaliableCoordsAndLeaveDoors(Arena roomToBePlaced, ObjectSet<MapCoords> unavaliableMapCoords, OrderedSet<DoorComponent> avaliableDoorsSet){
        unavaliableMapCoords.addAll(roomToBePlaced.getCotainingCoords());
        for (DoorComponent dc : roomToBePlaced.getDoors()) {
            if(!unavaliableMapCoords.contains(dc.leaveCoords)) {
                avaliableDoorsSet.add(dc);
            }
        }
    }


    public void generate(){

        mapTracker.clear();

        Array<Arena> arenas;

        if(generateTutorial){
            arenas = generateTutorialJigsaw();
        } else {
            arenas = generateJigsaw();
        }

        //this.startingMap = level1BossMaps.boomyMap(new BossTeleporterComponent());

        this.cleanArenas(arenas);
    }



    public ArenaMap generateBossMap(BossTeleporterComponent btc){
        WeightedRoll<ArenaMap> roll = new WeightedRoll<ArenaMap>(rand);
        switch (currentLevel){
            case ONE: roll.addWeightedObject(new WeightedObject<ArenaMap>(level1BossMaps.blobbaMap(btc), 20));
            default:
                break;
            case TWO:  roll.addWeightedObject(new WeightedObject<ArenaMap>(level1BossMaps.giantKugelMap(btc), 20));
                break;
            case THREE: roll.addWeightedObject(new WeightedObject<ArenaMap>(level1BossMaps.boomyMap(btc), 20));
                break;
            case FOUR: roll.addWeightedObject(new WeightedObject<ArenaMap>(level1BossMaps.wandaMap(btc), 20));
                break;
            case FIVE:
                roll.addWeightedObject(new WeightedObject<ArenaMap>(level1BossMaps.wandaMap(btc), 20));
                roll.addWeightedObject(new WeightedObject<ArenaMap>(level1BossMaps.blobbaMap(btc), 20));
                roll.addWeightedObject(new WeightedObject<ArenaMap>(level1BossMaps.giantKugelMap(btc), 20));
                roll.addWeightedObject(new WeightedObject<ArenaMap>(level1BossMaps.wandaMap(btc), 20));
                break;
        }
        ArenaMap map = roll.roll();
        cleanArenas(map.getRoomArray());
        return map;
    }

    public Array<Arena> generateTutorialJigsaw(){

        Array<Arena> placedArenas = new Array<Arena>();

        startingArena = tutorialFactory.groundMovementTutorial(new MapCoords(0,0));

        placedArenas.add(startingArena);
        placedArenas.add(tutorialFactory.jumpTutorial(new MapCoords(1, 0)));
        placedArenas.add(tutorialFactory.platformTutorial(new MapCoords(4,0)));
        placedArenas.add(tutorialFactory.grappleTutorial(new MapCoords(5,0)));
        placedArenas.add(tutorialFactory.enemyTurtorial(new MapCoords(5,3)));
        placedArenas.add(tutorialFactory.endTutorial(new MapCoords(6,3)));

        Arena f = arenaShellFactory.createOmniArenaSquareCenter(new MapCoords(7,3));
        placedArenas.add(f);

        OrderedSet<DoorComponent> avaliableDoorsSet = new OrderedSet<DoorComponent>();
        avaliableDoorsSet.addAll(f.getDoors());


        placedArenas = generateMapAroundPresetPoints(placedArenas,level1Rooms.getLevel1RoomArray(), avaliableDoorsSet, noBattleRooms);

        placeItemRoom(placedArenas, createAvaliableDoorSet(placedArenas));
        placeShopRoom(placedArenas, createAvaliableDoorSet(placedArenas));
        int range = (int) ((Math.sqrt(placedArenas.size) - 7 /*tutorial rooms */) / 2);

        LinkComponent teleportLink = new LinkComponent();

        BossTeleporterComponent btc = new BossTeleporterComponent(teleportLink);

        Arena bossRoom = arenaShellFactory.createSmallArena(new MapCoords(), true, true, false ,true);
        bossRoom.roomType = Arena.RoomType.BOSS;
        bossRoom.addEntity(decorFactory.mapPortal(bossRoom.getWidth() / 2, bossRoom.getHeight() / 2 + Measure.units(5f), btc));

        placeBossRoom(bossRoom, placedArenas, createAvaliableDoorSet(placedArenas), range);
        startingMap = new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());
        mapTracker.put(btc, startingMap);

        btc = new BossTeleporterComponent(teleportLink);
        mapTracker.put(btc, generateBossMap(btc));





        return placedArenas;

    }

    public Array<Arena> generateJigsaw() {
        Array<Arena> placedArenas = new Array<Arena>();

        startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords());

        //startingArena = tutorialFactory.grappleTutorial(new MapCoords());

        //startingArena = level5Rooms.room30Height3ThroughRoomWithHorizontalLasers().createArena(new MapCoords());

        placedArenas.add(startingArena);

        OrderedSet<DoorComponent> avaliableDoorsSet = new OrderedSet<DoorComponent>();
        avaliableDoorsSet.addAll(startingArena.getDoors());

        placedArenas = generateMapAroundPresetPoints(placedArenas, arenaGennerators(), avaliableDoorsSet, noBattleRooms);

        avaliableDoorsSet = createAvaliableDoorSet(placedArenas);

        placeItemRoom(placedArenas, avaliableDoorsSet);
        placeShopRoom(placedArenas, avaliableDoorsSet);
        int range = (int) ((Math.sqrt(placedArenas.size) - 1) / 2);



        LinkComponent teleportLink = new LinkComponent();

        BossTeleporterComponent btc = new BossTeleporterComponent(teleportLink);

        Arena bossRoom = arenaShellFactory.createSmallArena(new MapCoords(), true, true, false ,true);
        bossRoom.roomType = Arena.RoomType.BOSS;
        bossRoom.addEntity(decorFactory.mapPortal(bossRoom.getWidth() / 2, bossRoom.getHeight() / 2 + Measure.units(5f), btc));

        placeBossRoom(bossRoom, placedArenas, avaliableDoorsSet, range);
        startingMap = new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());
        mapTracker.put(btc, startingMap);

        btc = new BossTeleporterComponent(teleportLink);
        mapTracker.put(btc, generateBossMap(btc));




        return placedArenas;
    }



    public Array<ArenaGen> arenaGennerators(){

        Array<ArenaGen> arenaGens;

        switch(currentLevel){
            case ONE: arenaGens = level1Rooms.getLevel1RoomArray(); //level3Rooms.getLevel3RoomArray(); //arenaGens = level2Rooms.getLevel2RoomArray();
                break;
            case TWO: arenaGens = level2Rooms.getLevel2RoomArray();
                break;
            case THREE: arenaGens = level3Rooms.getLevel3RoomArray();
                break;
            case FOUR: arenaGens = level4Rooms.getLevel4RoomArray();
                break;
            case FIVE: arenaGens = level5Rooms.getLevel5RoomArray();
                break;
            default: arenaGens = level1Rooms.getLevel1RoomArray();
                break;
        }


        return arenaGens;

    }



    public boolean placeItemRoom(Array<Arena> placedArenas, OrderedSet<DoorComponent> avaliableDoors) {

        Arena itemRoom = itemArenaFactory.createItemRoom(new MapCoords(), generateItem(itemPool), generateItem(itemPool));
        if(placeRoomUsingDoors(itemRoom, avaliableDoors, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(itemRoom);
            return true;
        }
        return false;
    }

    public Item generateItem(Array<Item> itemPool){
        Item item;
        if(itemPool.size > 0) {
            int i = rand.nextInt(itemPool.size);
            item = itemPool.get(i);
            itemPool.removeValue(item, true);
        } else {
            item = new ItemVitaminC();
        }
        return item;
    }

    public boolean placeShopRoom(Array<Arena> placedArenas, OrderedSet<DoorComponent> avaliableDoors) {

        Item item1 = generateItem(itemPool);
        Item item2 = generateItem(itemPool);

        Arena shopRoom = shopFactory.createShop(item1, item2);
        if(placeRoomUsingDoors(shopRoom, avaliableDoors, createUnavaliableMapCoords(placedArenas), rand)){
            placedArenas.add(shopRoom);
            return true;
        }
        return false;
    }

    public boolean placeBossRoom(Arena bossRoom, Array<Arena> placedArenas, OrderedSet<DoorComponent> avaliableDoors, int range) {

        if (placeRoomAtRangeWithDoors(bossRoom,
                avaliableDoors,createUnavaliableMapCoords(placedArenas), rand, range)) {
            placedArenas.add(bossRoom);
            return true;
        }
        return false;
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

    public HashMap<BossTeleporterComponent, ArenaMap> getMapTracker() {
        return mapTracker;
    }

    public ArenaMap getStartingMap() {
        return startingMap;
    }



    public void cleanArenas(Array<Arena> arenas){
        for(int i = 0; i < arenas.size; i++) {
            Arena a = arenas.get(i);
            for(int j = a.getDoors().size - 1; j >=0; j--) {//for (DoorComponent dc : a.getDoors()) {
                DoorComponent dc = a.getDoors().get(j);
                if (!findDoorWithinFoundRoom(dc, arenas)) {
                    Bag<Component> bag = a.findBag(dc);
                    if (BagSearch.contains(ActiveOnTouchComponent.class, bag) || BagSearch.contains(InCombatActionComponent.class, bag)) {
                        a.getBagOfEntities().remove(bag);
                    } else {
                        CollisionBoundComponent cbc = BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag);
                        if(cbc != null) {
                            a.getBagOfEntities().remove(bag);
                            a.addEntity(decorFactory.wallBag(cbc.bound.x, cbc.bound.y, cbc.bound.getWidth(), cbc.bound.getHeight(), a.getArenaSkin()));
                        }
                    }
                    a.adjacentCoords.removeValue(dc.leaveCoords, false);
                    a.doors.removeValue(dc, true);
                }
            }
        }
    }


    /**
     * Uses a given door component to see if it links to a room that exists with the array of rooms
     * @param dc - DoorComponent
     * @param arenas - Array of arenas
     * @return - Returns true if door leads to another rooms, returns false if the door does not lead to another room.
     */
    @SuppressWarnings("SimplifiableIfStatement")
    public boolean findDoorWithinFoundRoom(DoorComponent dc, Array<Arena> arenas){
        Arena a = findRoom(dc.leaveCoords, arenas);
        if(a != null) {
            return a.adjacentCoords.contains(dc.currentCoords, false);
        }
        return false;
    }

    /**
     * Finds an arena in an array of Arenas that contains the given MapCoords.
     * @param mc - The given MapCoords
     * @param arenas - The array of arenas
     * @return - Returns an Arena if an Arena is found otherwise returns a null value
     */
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