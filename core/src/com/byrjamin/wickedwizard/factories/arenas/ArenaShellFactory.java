package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;

/**
 * Created by Home on 18/03/2017.
 */

public class ArenaShellFactory {

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

        new ArenaBuilder.Builder(defaultCoords, arena)
                .section(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR)).build();
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

        new ArenaBuilder.Builder(defaultCoords, arena)
                .section(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).build();

        return arena;
    }


    public static Arena createLetterIArena(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()));
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1));
        containingCorrds.add(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2));
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 2));
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 2));

        Arena arena = new Arena(containingCorrds);

        arena.setWidth(SECTION_WIDTH * 3);
        arena.setHeight(SECTION_HEIGHT * 3);

        new ArenaBuilder.Builder(defaultCoords, arena)
                .section(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL ))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 2),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE))
                .section(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 2),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).build();

        arena.addEntity(EntityFactory.wallBag(new Rectangle(0, SECTION_HEIGHT, SECTION_WIDTH, SECTION_HEIGHT)));
        arena.addEntity(EntityFactory.wallBag(new Rectangle(SECTION_WIDTH * 2, SECTION_HEIGHT, SECTION_WIDTH, SECTION_HEIGHT)));

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
