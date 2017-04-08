package com.byrjamin.wickedwizard.archive.maps.rooms.layout;

import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomWall;

/**
 * Created by Home on 07/02/2017.
 */
public class Width2Layout extends RoomLayout{


    public float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

    public float WIDTH = MainGame.GAME_WIDTH;
    public float HEIGHT = MainGame.GAME_HEIGHT;

    public float WALLWIDTH = Measure.units(5);

    public Width2Layout(){
        super(ROOM_LAYOUT.WIDTH_2);
    }


    public void applyLayout(com.byrjamin.wickedwizard.archive.maps.rooms.Room r){
        super.applyLayout(r);

        com.byrjamin.wickedwizard.archive.maps.MapCoords startCoords = r.getStartCoords();
        r.addCoords(new com.byrjamin.wickedwizard.archive.maps.MapCoords(startCoords.getX() + 1, startCoords.getY()));

        r.HEIGHT = SECTION_HEIGHT;
        r.WIDTH = SECTION_WIDTH * 2;

        WIDTH = SECTION_WIDTH * 2;

        r.add(new RoomDoor(0, Measure.units(10),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(startCoords.getX(), startCoords.getY()),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(startCoords.getX() - 1, startCoords.getY()),
                com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.LEFT));
        r.add(new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH));

        int xs = (int) WIDTH / (int) SECTION_WIDTH;
        int ys = (int) HEIGHT / (int) SECTION_HEIGHT;

        r.add(new RoomDoor(WIDTH - WALLWIDTH, Measure.units(10),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(startCoords.getX() + xs - 1, startCoords.getY()),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(startCoords.getX() + xs, startCoords.getY()),
                com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.RIGHT));
        r.add(new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH));

        //CEILING
        r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));


        //GROUND
        r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));


        r.setRoomBackground(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomBackground(r.getX(), r.getY(), WIDTH, HEIGHT, Measure.units(15)));

        r.getRoomExits().addAll(r.getRoomDoors());
        r.getRoomExits().addAll(r.getRoomGrates());

    }

}
