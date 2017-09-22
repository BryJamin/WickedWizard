package com.bryjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.LinkComponent;
import com.bryjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.bryjamin.wickedwizard.factories.arenas.presetrooms.ItemArenaFactory;
import com.bryjamin.wickedwizard.factories.arenas.presetrooms.RandomizerArenaFactory;
import com.bryjamin.wickedwizard.factories.arenas.presetrooms.ShopFactory;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.WeightedObject;
import com.bryjamin.wickedwizard.utils.WeightedRoll;
import com.bryjamin.wickedwizard.utils.enums.Direction;
import com.bryjamin.wickedwizard.utils.enums.Level;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Home on 22/03/2017.
 */

public class JigsawGenerator {

    private AssetManager assetManager;
    private com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap startingMap;

    private int noBattleRooms;
    private int noRandomizerRooms;

    private Random random;

    private Level level;

    private com.bryjamin.wickedwizard.utils.comparator.FarSort farSort;

    private ShopFactory shopFactory;
    private RandomizerArenaFactory randomizerArenaFactory;


    private com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin arenaSkin;

    private Array<ArenaCreate> arenaGens;
    private Array<BossMapCreate> bossMapGens;

    private HashMap<com.bryjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent, com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap> mapTracker = new HashMap<com.bryjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent, com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap>();

    private com.bryjamin.wickedwizard.factories.items.ItemStore itemStore;

    private DecorFactory decorFactory;
    private MapCleaner mapCleaner;


    private com.bryjamin.wickedwizard.utils.WeightedRoll<Comparator<DoorComponent>> typeOfSortRoller;
    private Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> typeOfSort;

    public JigsawGenerator(JigsawGeneratorConfig jigsawGeneratorConfig){
        this.assetManager = jigsawGeneratorConfig.assetManager;
        this.random = jigsawGeneratorConfig.random;
        this.noBattleRooms = jigsawGeneratorConfig.noBattleRooms;
        this.noRandomizerRooms = jigsawGeneratorConfig.noRandomizerRooms;
        this.itemStore = jigsawGeneratorConfig.itemStore;
        this.shopFactory = new ShopFactory(assetManager);

        this.setStartingMap(jigsawGeneratorConfig.startingMap);
        this.arenaGens = jigsawGeneratorConfig.arenaGens;


        System.out.println(getStartingMap().getCurrentArena());
        this.farSort = new com.bryjamin.wickedwizard.utils.comparator.FarSort(getStartingMap().getCurrentArena().getStartingCoords());
        this.bossMapGens = jigsawGeneratorConfig.bossMapGens;
        this.arenaSkin = jigsawGeneratorConfig.level.getArenaSkin();
        this.level = jigsawGeneratorConfig.level;
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.mapCleaner = new MapCleaner(decorFactory);


        this.randomizerArenaFactory = new RandomizerArenaFactory(assetManager, arenaSkin);

        this.typeOfSortRoller = new com.bryjamin.wickedwizard.utils.WeightedRoll<Comparator<DoorComponent>>(random);
        typeOfSortRoller.addWeightedObject(new com.bryjamin.wickedwizard.utils.WeightedObject<Comparator<DoorComponent>>(farSort.SORT_DOORS_BY_LARGEST_Y, 20));
        typeOfSortRoller.addWeightedObject(new com.bryjamin.wickedwizard.utils.WeightedObject<Comparator<DoorComponent>>(farSort.RIGHTMOST_DISTANCE_DOORS, 20));
        typeOfSortRoller.addWeightedObject(new com.bryjamin.wickedwizard.utils.WeightedObject<Comparator<DoorComponent>>(farSort.SORT_DOORS_BY_LARGEST_Y, 20));
        typeOfSortRoller.addWeightedObject(new com.bryjamin.wickedwizard.utils.WeightedObject<Comparator<DoorComponent>>(farSort.SORT_DOORS_BY_LOWEST_Y, 20));
        typeOfSortRoller.addWeightedObject(new com.bryjamin.wickedwizard.utils.WeightedObject<Comparator<DoorComponent>>(null, 200));

        this.typeOfSort = typeOfSortRoller.roll();
    }


    public Level getLevel(){
        return level;
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

    public OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> createAvaliableDoorSet(Arena... arenas){
        return createAvaliableDoorSet(new Array<Arena>(arenas));
    }

    public OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> createAvaliableDoorSet(Array<Arena> arenas){
        OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoors = new OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>();

        Array<Arena> protectionFromIteratorError = new Array<Arena>();
        protectionFromIteratorError.addAll(arenas);

        for(Arena a : arenas){

            if(a.arenaType != Arena.ArenaType.BOSS &&
                    a.arenaType != Arena.ArenaType.ITEM &&
                    a.arenaType != Arena.ArenaType.SHOP) {

                for (com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc : a.doors) {
                    if (findRoom(dc.leaveCoords, protectionFromIteratorError) == null) {
                        avaliableDoors.add(dc);
                    }
                }

            }
        }
        return avaliableDoors;
    }


    public Array<Arena> generateMapAroundPresetPoints(Array<Arena> presetRooms, Array<ArenaCreate> arenaGenArray,
                                                      OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoorsSet, int noOfRoomsPlaced){

        Array<Arena> placedArenas = new Array<Arena>();
        placedArenas.addAll(presetRooms);
        ObjectSet<MapCoords> unavaliableMapCoords = createUnavaliableMapCoords(presetRooms);


        WeightedRoll<ArenaCreate> roll = new WeightedRoll<ArenaCreate>(random);
        for(ArenaCreate ag : arenaGenArray) roll.addWeightedObject(new WeightedObject<ArenaCreate>(ag, 50));

        int placedRooms = 0;
        int loops = 0;

        //TODO I changed this to < and equal to, not sure of the ramifications
        while(placedRooms <= noOfRoomsPlaced && loops <= noOfRoomsPlaced * 3) {


            WeightedObject<ArenaCreate> weightedObject = roll.rollForWeight();
            Arena nextRoomToBePlaced = weightedObject.obj().createArena(new MapCoords());
            if(placeRoomUsingDoors(nextRoomToBePlaced, avaliableDoorsSet, unavaliableMapCoords, random)){

                //TODO make this it's own method for mandatory doors within rooms.
                if(nextRoomToBePlaced.mandatoryDoors.size > 0) {

                    Array<Arena> mockPlacedArenas = new Array<Arena>();
                    mockPlacedArenas.addAll(placedArenas);

                    OrderedSet<DoorComponent> mockAvaliableDoorSet = new OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>();
                    mockAvaliableDoorSet.addAll(mockAvaliableDoorSet);

                    boolean isAllDoorsUsed = true;

                    for (DoorComponent dc : nextRoomToBePlaced.mandatoryDoors) {
                        if(findDoorWithinFoundRoom(dc, mockPlacedArenas)) {
                            continue;
                        }

                        int tries = 0;
                        boolean placedRoom = false;
                        while(tries <= 10){
                            com.bryjamin.wickedwizard.utils.WeightedObject<ArenaCreate> weightedArenaGen = roll.rollForWeight();
                            Arena nextInnerRoomToBePlaced = weightedArenaGen.obj().createArena(new MapCoords());
                            if(nextInnerRoomToBePlaced.mandatoryDoors.size == 0) {
                                if (fillMandatoryDoor(nextInnerRoomToBePlaced, dc, mockPlacedArenas, mockAvaliableDoorSet)) {
                                    weightedArenaGen.setWeight((weightedArenaGen.getWeight() / 10 > 0) ? weightedArenaGen.getWeight() / 10 : 1);
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
                        weightedObject.setWeight((weightedObject.getWeight() / 10 > 0) ? weightedObject.getWeight() / 10 : 1);
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



    public boolean fillMandatoryDoor(Arena arena, com.bryjamin.wickedwizard.ecs.components.object.DoorComponent mandatoryDoor, Array<Arena> placedArenas, OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoors){

        OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> doors = new OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>();
        doors.add(mandatoryDoor);

        ObjectSet<MapCoords> unavaliableMapCoords = createUnavaliableMapCoords(placedArenas);

        int tries = 0;
        while(tries < 15) {

            if (placeRoomUsingDoors(arena, doors, unavaliableMapCoords, random)) {
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
    public void addArenaToMap(Arena roomToBePlaced, Array<Arena> placedArenas, ObjectSet<MapCoords> unavaliableMapCoords, OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoorsSet) {
        placedArenas.add(roomToBePlaced);
        updateUnavaliableCoordsAndLeaveDoors(roomToBePlaced, unavaliableMapCoords, avaliableDoorsSet);
    }

    public void updateUnavaliableCoordsAndLeaveDoors(Arena roomToBePlaced, ObjectSet<MapCoords> unavaliableMapCoords, OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoorsSet){
        unavaliableMapCoords.addAll(roomToBePlaced.getCotainingCoords());
        for (com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc : roomToBePlaced.getDoors()) {
            if(!unavaliableMapCoords.contains(dc.leaveCoords)) {
                avaliableDoorsSet.add(dc);
            }
        }
    }


    public void generate(){
        mapTracker.clear();
        generate(new ArenaMap(startingMap.getCurrentArena()));
    }



    public void generate(ArenaMap arenaMap){

        mapTracker.clear();
        //if(noBattleRooms <= 0) return;

        Array<Arena> placedArenas = new Array<Arena>();

        placedArenas.addAll(arenaMap.getRoomArray());
        placedArenas = generateMapAroundPresetPoints(placedArenas, arenaGens, createAvaliableDoorSet(arenaMap.getRoomArray()), noBattleRooms);

        placeItemRoom(placedArenas, createAvaliableDoorSet(placedArenas));
        placeShopRoom(placedArenas, createAvaliableDoorSet(placedArenas));

        for(int i = 0; i < noRandomizerRooms; i++) {
            placeRandomizerRoom(placedArenas, createAvaliableDoorSet(placedArenas));
        }


        LinkComponent teleportLink = new LinkComponent();
        BossTeleporterComponent btc = new BossTeleporterComponent(teleportLink);

/*        for(DoorComponent dc : createAvaliableDoorSet(placedArenas)){
            System.out.println(findRoom(dc.leaveCoords, placedArenas));
        }*/

        placeBossRoom(new BossMaps(assetManager, arenaSkin).bossTeleportArena(new MapCoords(), btc), placedArenas, createAvaliableDoorSet(placedArenas));

        startingMap = new ArenaMap(arenaMap.getCurrentArena(), placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());
        mapTracker.put(btc, startingMap);

        btc = new BossTeleporterComponent(teleportLink);
        ArenaMap bossMap = bossMapGens.get(random.nextInt(bossMapGens.size)).createBossMap(btc);
        mapCleaner.cleanArenas(bossMap.getRoomArray());

        mapTracker.put(btc, bossMap);

        mapCleaner.cleanArenas(startingMap.getRoomArray());

    }


    public void setStartingMap(ArenaMap startingMap) {
        this.startingMap = startingMap;
    }


    public boolean placeItemRoom(Array<Arena> placedArenas, OrderedSet<DoorComponent> avaliableDoors) {

        Array<Arena> itemRooms = new Array<Arena>();
        itemRooms.add(new ItemArenaFactory(assetManager, arenaSkin).createDownItemRoom(new MapCoords()));
        itemRooms.add(new ItemArenaFactory(assetManager, arenaSkin).createLeftItemRoom(new MapCoords()));
        itemRooms.add(new ItemArenaFactory(assetManager, arenaSkin).createRightItemRoom(new MapCoords()));
        itemRooms.add(new ItemArenaFactory(assetManager, arenaSkin).createUpItemRoom(new MapCoords()));

        itemRooms.shuffle();

        for(Arena itemRoom : itemRooms) {
            if (placeRoomUsingDoorsRandomly(itemRoom, avaliableDoors, createUnavaliableMapCoords(placedArenas), random)) {
                placedArenas.add(itemRoom);
                mapCleaner.cleanArena(itemRoom, placedArenas);
                return true;
            }
        }
        return false;
    }


    public void removeAdditionalDoorsFromArena(Arena arena){
        
    }


    public boolean placeShopRoom(Array<Arena> placedArenas, OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoors) {

        Arena shopRoom = shopFactory.createShop(new MapCoords());
        if(placeRoomUsingDoorsRandomly(shopRoom, avaliableDoors, createUnavaliableMapCoords(placedArenas), random)){
            placedArenas.add(shopRoom);
            mapCleaner.cleanArena(shopRoom, placedArenas);
            return true;
        }

        return false;
    }


    public boolean placeRandomizerRoom(Array<Arena> placedArenas, OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoors) {

        Arena shopRoom = randomizerArenaFactory.createRandomizerRoom(new MapCoords());
        if(placeRoomUsingDoorsRandomly(shopRoom, avaliableDoors, createUnavaliableMapCoords(placedArenas), random)){
            placedArenas.add(shopRoom);
            mapCleaner.cleanArena(shopRoom, placedArenas);
            return true;
        }

        return false;
    }

    public boolean placeBossRoom(Arena bossRoom, Array<Arena> placedArenas, OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoors) {


        Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> doorComponentArray = avaliableDoors.orderedItems();
        doorComponentArray.sort(farSort.DOOR_FAR_MAPCOORDS);

        if (placeRoomUsingDoorsInOrder(bossRoom,
                doorComponentArray ,createUnavaliableMapCoords(placedArenas), random)) {
            placedArenas.add(bossRoom);

            return true;
        }
        return false;
    }


    public boolean placeRoomUsingDoors(Arena room, OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoorsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand){
        Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> adc = avaliableDoorsSet.orderedItems();

        if(typeOfSort == null){
            adc.shuffle();
        } else {
            adc.sort(typeOfSort);
        }

        return placeRoomUsingDoorsInOrder(room, adc, unavaliableMapCoords, rand);

    }

    public boolean placeRoomUsingDoorsRandomly(Arena room, OrderedSet<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> avaliableDoorsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand){
        Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> adc = avaliableDoorsSet.orderedItems();
            adc.shuffle();
        return placeRoomUsingDoorsInOrder(room, adc, unavaliableMapCoords, rand);

    }


    public boolean placeRoomUsingDoorsInOrder(Arena room, Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> availableDoorsArray, ObjectSet<MapCoords> unavaliableMapCoords, Random rand){

        Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> temporaryDoorsArray = new Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>();
        temporaryDoorsArray.addAll(availableDoorsArray);

        boolean roomPlaced = false;

        while(!roomPlaced) {

            if(temporaryDoorsArray.size <= 0){
                break;
            }

            //The available co-ordinates we can shift to.
            com.bryjamin.wickedwizard.ecs.components.object.DoorComponent selectedAvaliableDoor = temporaryDoorsArray.first();
           // System.out.println("Array size is " + temporaryDoorsArray.size);
            temporaryDoorsArray.removeValue(selectedAvaliableDoor, false);

           // System.out.println("Array size is " + temporaryDoorsArray.size);



            Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> linkableDoorsArray = createLinkableDoorsArray(selectedAvaliableDoor, room);

            while(!roomPlaced && linkableDoorsArray.size > 0) {
                if(linkableDoorsArray.size <= 0){
                    break;
                }

                int linkableExitsSelector = rand.nextInt(linkableDoorsArray.size);
                //The available co-ordinates we can shift to.
                com.bryjamin.wickedwizard.ecs.components.object.DoorComponent selectedLinkableDoor = linkableDoorsArray.get(linkableExitsSelector);

                linkableDoorsArray.removeIndex(linkableExitsSelector);

                MapCoords shiftCoords = generateShiftCoords(selectedAvaliableDoor.leaveCoords, selectedLinkableDoor.currentCoords);

                //Mocks moving the room
                Array<MapCoords> mockCoords = mockShiftCoordinatePosition(room, shiftCoords);

                roomPlaced = true;

                for (int j = 0; j < mockCoords.size; j++) {

                    if (unavaliableMapCoords.contains(mockCoords.get(j))) {
                        roomPlaced = false;
                        break;
                    }
                }

                if (roomPlaced) {
                    shiftCoordinatePosition(room, shiftCoords);
                    availableDoorsArray.removeValue(selectedAvaliableDoor, false);
                    //break;
                }

            }

        }
        return roomPlaced;

    }


    private Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> createLinkableDoorsArray(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent selectedDoor, Arena arena){

        Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> linkableDoorsArray = new Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>();

        for(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc : arena.getDoors()) {



            switch (selectedDoor.exit){
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

        return linkableDoorsArray;

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

    /**
     * Shifts the co-ordinates of an arena using the x and y value of the shift co-ordinates
     * @param a - Arena to be shifted
     * @param shiftCoords - Mapcoords holding the x and y value to move the Arena by.
     */
    public void shiftCoordinatePosition(Arena a, MapCoords shiftCoords){

        int diffX = shiftCoords.getX();
        int diffY = shiftCoords.getY();

        for(MapCoords m : a.getCotainingCoords()) {
            m.addX(diffX);
            m.addY(diffY);
        }

        for(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc : a.getDoors()){
            dc.leaveCoords.add(diffX, diffY);
            dc.currentCoords.add(diffX, diffY);
        }
    }


    public Arena getStartingRoom() {
        return startingMap.getCurrentArena();
    }

    public HashMap<com.bryjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent, com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap> getMapTracker() {
        return mapTracker;
    }

    public com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap getStartingMap() {
        return startingMap;
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
            return checkAdjacentDoorsContainCoordinates(a, dc.currentCoords);
        }
        return false;
    }


    private boolean checkAdjacentDoorsContainCoordinates(Arena arena, MapCoords mapCoords){

        for(DoorComponent dc : arena.getDoors()){

            if(dc.leaveCoords.equals(mapCoords)){
                return true;
            }
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


    public void cleanArenas(){
        mapCleaner.cleanArenas(startingMap.getRoomArray());
    }


    public com.bryjamin.wickedwizard.factories.items.ItemStore getItemStore() {
        return itemStore;
    }
}