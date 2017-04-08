package com.byrjamin.wickedwizard.archive.maps.rooms.layout;

import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomGrate;
import com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomWall;

/**
 * Created by Home on 05/02/2017.
 */
public class BasicRoomLayout extends RoomLayout{

    public float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

    public float WIDTH = MainGame.GAME_WIDTH;
    public float HEIGHT = MainGame.GAME_HEIGHT;

    public float WALLWIDTH = Measure.units(5);

    private com.byrjamin.wickedwizard.archive.maps.MapCoords defaultCoords = new com.byrjamin.wickedwizard.archive.maps.MapCoords(0,0);


    public BasicRoomLayout() {
        super(ROOM_LAYOUT.OMNI);
    }


    public void applyLayout(com.byrjamin.wickedwizard.archive.maps.rooms.Room r){
        super.applyLayout(r);

        com.byrjamin.wickedwizard.archive.maps.MapCoords defaultCoords = r.getStartCoords();

        //LEFT
        r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomDoor(0, Measure.units(10),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.LEFT));
        r.add(new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT, WALLWIDTH));


        //RIGHT
        r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomDoor(WIDTH - WALLWIDTH, Measure.units(10),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.RIGHT));
        r.add(new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT, WALLWIDTH));


        //TELEPORT UP
        RoomGrate rt = new RoomGrate(new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.UP);
        rt.setCenter(WIDTH / 2, HEIGHT - WALLWIDTH * 3);
        r.add(rt);


        //TELEPORT DOWN
        rt = new RoomGrate(new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX(), defaultCoords.getY() - 1),
                com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.DOWN);
        rt.setCenter(WIDTH / 2, WALLWIDTH * 4);
        r.add(rt);


        //CEILING
        r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        //r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));


        //GROUND
        //r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));

        r.setRoomBackground(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomBackground(r.getX(), r.getY(), WIDTH, HEIGHT, Measure.units(15)));

        r.getRoomExits().addAll(r.getRoomDoors());
        r.getRoomExits().addAll(r.getRoomGrates());

    }


}
