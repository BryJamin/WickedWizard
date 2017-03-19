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

    public static Bag<Bag<Component>> omniRoomBag(MapCoords defaultCoords){

        Bag<Bag<Component>> bagOfBags = new Bag<Bag<Component>>();
        bagOfBags.add(EntityFactory.blobBag(400, 800));
        bagOfBags.add(BackgroundFactory.backgroundBags(0,0,
                SECTION_WIDTH,
                SECTION_HEIGHT,
                Measure.units(15),
                PlayScreen.atlas.findRegions("backgrounds/wall")));

        //for(RoomWall rw : map.getActiveRoom().getRoomWalls()){
         //   bagOfBags.add(EntityFactory.wallBag(rw.getBounds()));
        //}

        //LEFT WALL
        bagOfBags.add(EntityFactory.wallBag(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        bagOfBags.add(EntityFactory.doorBag(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.left));


        //RIGHT
        bagOfBags.add(EntityFactory.wallBag(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        bagOfBags.add(EntityFactory.doorBag(WIDTH - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.right));

        //CEILING
        bagOfBags.add(EntityFactory.wallBag(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH));
        bagOfBags.add(EntityFactory.grateBag(WIDTH / 2, 800,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                DoorComponent.DIRECTION.up));

        //GROUND
        bagOfBags.add(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));
        bagOfBags.add(EntityFactory.grateBag(WIDTH / 2, 300,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() -1),
                DoorComponent.DIRECTION.down));

      return bagOfBags;

    }

    public static Arena createOmniArena(){
        return createOmniArena(new MapCoords(0,0));
    }

    public static Arena createOmniArena(MapCoords defaultCoords) {
        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        Arena arena = new Arena(containingCorrds);

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
        arena.addDoor(EntityFactory.grateBag(WIDTH / 2, 800,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                DoorComponent.DIRECTION.up));


        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));
        arena.addDoor(EntityFactory.grateBag(WIDTH / 2, 300,
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() -1),
                DoorComponent.DIRECTION.down));

        return arena;
    }



    public static void cleanArenas(Array<Arena> arenas){


        for(int i = 0; i < arenas.size; i++) {
            Arena a = arenas.get(i);
                for (DoorComponent dc : a.getDoors()) {
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
                    }
                }
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    public static boolean findDoorWithinFoundRoom(DoorComponent dc, Array<Arena> arenas){
        Arena a = findRoom(dc.leaveCoords, arenas);
        if(a != null) {
            return a.adjacentCoords.contains(dc.currentCoords, false);
        }
        return false;
    }

    public static Arena findRoom(MapCoords mc, Array<Arena> arenas){
        for(Arena a : arenas){
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
