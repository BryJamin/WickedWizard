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
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory;
import com.byrjamin.wickedwizard.factories.arenas.presets.ShopFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemStore;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.WeightedObject;
import com.byrjamin.wickedwizard.utils.WeightedRoll;
import com.byrjamin.wickedwizard.utils.comparator.FarSort;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Home on 22/03/2017.
 */

public class JigsawGenerator {

    private AssetManager assetManager;
    private ArenaMap startingMap;

    private int noBattleRooms;

    private Random random;

    private FarSort farSort;

    private ShopFactory shopFactory;
    private ArenaSkin arenaSkin;

    private Array<ArenaCreate> arenaGens;
    private Array<BossMapCreate> bossMapGens;

    private HashMap<BossTeleporterComponent, ArenaMap> mapTracker = new HashMap<BossTeleporterComponent, ArenaMap>();

    private ItemStore itemStore;

    private DecorFactory decorFactory;


    public JigsawGenerator(JigsawGeneratorConfig jigsawGeneratorConfig){
        this.assetManager = jigsawGeneratorConfig.assetManager;
        this.random = jigsawGeneratorConfig.random;
        this.noBattleRooms = jigsawGeneratorConfig.noBattleRooms;
        this.itemStore = new ItemStore(random);
        this.shopFactory = new ShopFactory(assetManager);
        this.setStartingMap(jigsawGeneratorConfig.startingMap);
        this.arenaGens = jigsawGeneratorConfig.arenaGens;
        this.farSort = new FarSort(getStartingMap().getCurrentArena().getStartingCoords());
        this.bossMapGens = jigsawGeneratorConfig.bossMapGens;
        this.arenaSkin = jigsawGeneratorConfig.arenaSkin;
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
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

    public OrderedSet<DoorComponent> createAvaliableDoorSet(Arena... arenas){
        return createAvaliableDoorSet(new Array<Arena>(arenas));
    }

    public OrderedSet<DoorComponent> createAvaliableDoorSet(Array<Arena> arenas){
        OrderedSet<DoorComponent> avaliableDoors = new OrderedSet<DoorComponent>();

        Array<Arena> protectionFromIteratorError = new Array<Arena>();
        protectionFromIteratorError.addAll(arenas);

        for(Arena a : arenas){

            if(a.roomType != Arena.RoomType.BOSS &&
                    a.roomType != Arena.RoomType.ITEM &&
                    a.roomType != Arena.RoomType.SHOP) {

                for (DoorComponent dc : a.doors) {
                    if (findRoom(dc.leaveCoords, protectionFromIteratorError) == null) {
                        avaliableDoors.add(dc);
                    }
                }

            }
        }
        return avaliableDoors;
    }


    public Array<Arena> generateMapAroundPresetPoints(Array<Arena> presetRooms, Array<ArenaCreate> arenaGenArray,
                                                      OrderedSet<DoorComponent> avaliableDoorsSet, int noOfRoomsPlaced){

        Array<Arena> placedArenas = new Array<Arena>();
        placedArenas.addAll(presetRooms);
        ObjectSet<MapCoords> unavaliableMapCoords = createUnavaliableMapCoords(presetRooms);


        WeightedRoll<ArenaCreate> roll = new WeightedRoll<ArenaCreate>(random);
        for(ArenaCreate ag : arenaGenArray) roll.addWeightedObject(new WeightedObject<ArenaCreate>(ag, 20));

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
                            WeightedObject<ArenaCreate> weightedArenaGen = roll.rollForWeight();
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
        generate(new ArenaMap(new ArenaShellFactory(assetManager, arenaSkin).createOmniArenaHiddenGrapple(new MapCoords())));
    }



    public void generate(ArenaMap arenaMap){

        mapTracker.clear();
        //if(noBattleRooms <= 0) return;

        Array<Arena> placedArenas = new Array<Arena>();

        placedArenas.addAll(arenaMap.getRoomArray());
        placedArenas = generateMapAroundPresetPoints(placedArenas, arenaGens, createAvaliableDoorSet(arenaMap.getRoomArray()), noBattleRooms);

        placeItemRoom(placedArenas, createAvaliableDoorSet(placedArenas));
        placeShopRoom(placedArenas, createAvaliableDoorSet(placedArenas));

        LinkComponent teleportLink = new LinkComponent();
        BossTeleporterComponent btc = new BossTeleporterComponent(teleportLink);

/*        for(DoorComponent dc : createAvaliableDoorSet(placedArenas)){
            System.out.println(findRoom(dc.leaveCoords, placedArenas));
        }*/

        placeBossRoom(new BossMaps(assetManager, arenaSkin).bossTeleportArena(new MapCoords(), btc), placedArenas, createAvaliableDoorSet(placedArenas));

        startingMap = new ArenaMap(arenaMap.getCurrentArena(), placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());
        mapTracker.put(btc, startingMap);

        btc = new BossTeleporterComponent(teleportLink);
        ArenaMap bossMap = bossMapGens.get(random.nextInt(bossMapGens.size)).createBossMap(btc, itemStore.generateItemRoomItem());
        cleanArenas(bossMap.getRoomArray());

        mapTracker.put(btc, bossMap);


        this.cleanArenas(startingMap.getRoomArray());

    }


    public void setStartingMap(ArenaMap startingMap) {
        this.startingMap = startingMap;
    }


    public boolean placeItemRoom(Array<Arena> placedArenas, OrderedSet<DoorComponent> avaliableDoors) {
        Arena itemRoom = new ItemArenaFactory(assetManager, arenaSkin).createItemRoom(new MapCoords(), itemStore.generateItemRoomItem(), itemStore.generateItemRoomItem());
        if(placeRoomUsingDoors(itemRoom, avaliableDoors, createUnavaliableMapCoords(placedArenas), random)){
            placedArenas.add(itemRoom);
            cleanArena(itemRoom, placedArenas);
            return true;
        }
        return false;
    }


    public void removeAdditionalDoorsFromArena(Arena arena){
        
    }


    public boolean placeShopRoom(Array<Arena> placedArenas, OrderedSet<DoorComponent> avaliableDoors) {

        Item item1 = itemStore.generateItemRoomItem();
        Item item2 = itemStore.generateItemRoomItem();

        Arena shopRoom = shopFactory.createShop(item1, item2);
        if(placeRoomUsingDoors(shopRoom, avaliableDoors, createUnavaliableMapCoords(placedArenas), random)){
            placedArenas.add(shopRoom);
            cleanArena(shopRoom, placedArenas);
            return true;
        }

        return false;
    }

    public boolean placeBossRoom(Arena bossRoom, Array<Arena> placedArenas, OrderedSet<DoorComponent> avaliableDoors) {


        Array<DoorComponent> doorComponentArray = avaliableDoors.orderedItems();
        doorComponentArray.sort(farSort.DOOR_FAR_MAPCOORDS);

        if (placeRoomUsingDoorsInOrder(bossRoom,
                doorComponentArray ,createUnavaliableMapCoords(placedArenas), random)) {
            placedArenas.add(bossRoom);

            return true;
        }
        return false;
    }


    public boolean placeRoomUsingDoors(Arena room, OrderedSet<DoorComponent> avaliableDoorsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand){
        Array<DoorComponent> adc = avaliableDoorsSet.orderedItems();
        adc.shuffle();
        return placeRoomUsingDoorsInOrder(room, adc, unavaliableMapCoords, rand);

    }


    public boolean placeRoomUsingDoorsInOrder(Arena room, Array<DoorComponent> availableDoorsArray, ObjectSet<MapCoords> unavaliableMapCoords, Random rand){

        Array<DoorComponent> temporaryDoorsArray = new Array<DoorComponent>();
        temporaryDoorsArray.addAll(availableDoorsArray);

        boolean roomPlaced = false;

        while(!roomPlaced) {

            if(temporaryDoorsArray.size <= 0){
                break;
            }

            //The available co-ordinates we can shift to.
            DoorComponent selectedAvaliableDoor = temporaryDoorsArray.first();
           // System.out.println("Array size is " + temporaryDoorsArray.size);
            temporaryDoorsArray.removeValue(selectedAvaliableDoor, false);

           // System.out.println("Array size is " + temporaryDoorsArray.size);



            Array<DoorComponent> linkableDoorsArray = createLinkableDoorsArray(selectedAvaliableDoor, room);

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


    private Array<DoorComponent> createLinkableDoorsArray(DoorComponent selectedDoor, Arena arena){

        Array<DoorComponent> linkableDoorsArray = new Array<DoorComponent>();

        for(DoorComponent dc : arena.getDoors()) {



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

        for(DoorComponent dc : a.getDoors()){
            dc.leaveCoords.add(diffX, diffY);
            dc.currentCoords.add(diffX, diffY);
        }
    }


    public Arena getStartingRoom() {
        return startingMap.getCurrentArena();
    }

    public HashMap<BossTeleporterComponent, ArenaMap> getMapTracker() {
        return mapTracker;
    }

    public ArenaMap getStartingMap() {
        return startingMap;
    }


    /**
     * Scans arenas within an array arena to see if any doors link to co-ordinates that are not occupied or
     * do not have any doors linking back.
     *
     * The doors are then replaced with a wall
     *
     * @param arenas
     */
    public void cleanArenas(Array<Arena> arenas){
        for(int i = 0; i < arenas.size; i++) {
            Arena a = arenas.get(i);
            cleanArena(a, arenas);
        }
    }

    public void cleanArena(Arena a, Array<Arena> arenas){

        for(int j = a.getDoors().size - 1; j >=0; j--) {//for (DoorComponent dc : a.getDoors()) {
            DoorComponent dc = a.getDoors().get(j);
            if (!findDoorWithinFoundRoom(dc, arenas)) {
                replaceDoorWithWall(dc, a);
            }
        }

    }

    private void replaceDoorWithWall(DoorComponent dc, Arena arena){

        Bag<Component> bag = arena.findBag(dc);
        if (BagSearch.contains(ActiveOnTouchComponent.class, bag) || BagSearch.contains(InCombatActionComponent.class, bag)) {
            arena.getBagOfEntities().remove(bag);
        } else {
            CollisionBoundComponent cbc = BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag);
            if(cbc != null) {
                arena.getBagOfEntities().remove(bag);
                arena.addEntity(decorFactory.wallBag(cbc.bound.x, cbc.bound.y, cbc.bound.getWidth(), cbc.bound.getHeight(), arena.getArenaSkin()));
            }
        }
        //arena.adjacentCoords.removeValue(dc.leaveCoords, false);
        arena.doors.removeValue(dc, true);

    }

    public void cleanArenas(){
       cleanArenas(startingMap.getRoomArray());
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
}