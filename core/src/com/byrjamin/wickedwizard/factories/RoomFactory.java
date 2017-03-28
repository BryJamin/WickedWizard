package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.RoomTypeComponent;
import com.byrjamin.wickedwizard.helper.BagSearch;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomGrate;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import java.util.Arrays;

/**
 * Created by Home on 18/03/2017.
 */

public class RoomFactory {

    public static float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public static float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

    public static float WIDTH = MainGame.GAME_WIDTH;
    public static float HEIGHT = MainGame.GAME_HEIGHT;

    public static float WALLWIDTH = Measure.units(5);

    public static Arena createOmniArena(){
        return createOmniArena(new MapCoords(0,0));
    }

    public static Arena createOmniArena(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds);


        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena.addEntity(BackgroundFactory.backgroundBags(0,0,
                SECTION_WIDTH,
                SECTION_HEIGHT,
                Measure.units(15),
                PlayScreen.atlas.findRegions("backgrounds/wall")));

        //LEFT WALL
        arena.addEntity(EntityFactory.wallBag(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.left));

        //RIGHT WALL
        arena.addEntity(EntityFactory.wallBag(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag(WIDTH - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.right));

        //CEILING
        arena.addEntity(EntityFactory.wallBag(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH));
        arena.addDoor(EntityFactory.grateBag(WIDTH / 2, 900,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                DoorComponent.DIRECTION.up));


        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));
        arena.addDoor(EntityFactory.grateBag(WIDTH / 2, 400,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() -1),
                DoorComponent.DIRECTION.down));

        Bag<Component> bag = new Bag<Component>();
        bag.add(new RoomTypeComponent(RoomTypeComponent.Type.BATTLE));
        arena.addEntity(bag);


        System.out.println(arena.cotainingCoords);

        return arena;
    }

    public static Arena createWidth2Arena(){
        return createWidth2Arena(new MapCoords(0,0));
    }


    public static Arena createWidth2Arena(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH * 2);
        arena.setHeight(SECTION_HEIGHT);


        arena.addEntity(BackgroundFactory.backgroundBags(0,0,
                SECTION_WIDTH * 2,
                SECTION_HEIGHT,
                Measure.units(15),
                PlayScreen.atlas.findRegions("backgrounds/wall")));

        //LEFT WALL
        arena.addEntity(EntityFactory.wallBag(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.left));

        //RIGHT WALL
        arena.addEntity(EntityFactory.wallBag((SECTION_WIDTH * 2) - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag((SECTION_WIDTH * 2) - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                DoorComponent.DIRECTION.right));

        //CEILING
        arena.addEntity(EntityFactory.wallBag(0,  HEIGHT - WALLWIDTH, (SECTION_WIDTH * 2), WALLWIDTH));
/*        arena.addDoor(EntityFactory.grateBag(WIDTH / 2, 800,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                DoorComponent.DIRECTION.up));*/


        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, (SECTION_WIDTH * 2), WALLWIDTH * 3));
/*        arena.addDoor(EntityFactory.grateBag(WIDTH / 2, 300,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() -1),
                DoorComponent.DIRECTION.down));*/


        //System.out.println(arena.cotainingCoords);

        Bag<Component> bag = new Bag<Component>();
        bag.add(new RoomTypeComponent(RoomTypeComponent.Type.BATTLE));
        arena.addEntity(bag);

        return arena;
    }



    public static void cleanArenas(Array<Arena> arenas){
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
                                a.addEntity(EntityFactory.wallBag(cbc.bound));
                            }
                        }
                        a.adjacentCoords.removeValue(dc.leaveCoords, false);
                        a.doors.removeValue(dc, true);
                    }
                }
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean findDoorWithinFoundRoom(DoorComponent dc, Array<Arena> arenas){
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

    /*public void cleanLeafs(Array<Room> rooms){
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
    } */










}
