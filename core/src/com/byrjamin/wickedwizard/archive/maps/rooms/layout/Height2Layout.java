package com.byrjamin.wickedwizard.archive.maps.rooms.layout;

import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 07/02/2017.
 */
public class Height2Layout extends RoomLayout{

    public float WIDTH = MainGame.GAME_WIDTH;
    public float HEIGHT = MainGame.GAME_HEIGHT;

    public float WALLWIDTH = Measure.units(5);

    public Height2Layout(){
        super(ROOM_LAYOUT.HEIGHT_2);
    }


    public void applyLayout(com.byrjamin.wickedwizard.archive.maps.rooms.Room r){
        super.applyLayout(r);

        com.byrjamin.wickedwizard.archive.maps.MapCoords sc = r.getStartCoords();
        r.addCoords(new com.byrjamin.wickedwizard.archive.maps.MapCoords(sc.getX(), sc.getY() + 1));
        r.HEIGHT = SECTION_HEIGHT * 2;
        r.WIDTH = SECTION_WIDTH;

        HEIGHT = SECTION_HEIGHT * 2;

    //    r.add(new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH, wallTextures));

        int xs = (int) WIDTH / (int) SECTION_WIDTH;
        int ys = (int) HEIGHT / (int) SECTION_HEIGHT;

        for(int i = 0; i < ys; i++){

            r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomDoor(WIDTH - WALLWIDTH, Measure.units(10) + SECTION_HEIGHT * i,
                    new com.byrjamin.wickedwizard.archive.maps.MapCoords(sc.getX() + xs - 1, sc.getY() + i),
                    new com.byrjamin.wickedwizard.archive.maps.MapCoords(sc.getX() + xs, sc.getY() + i),
                    com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.RIGHT));
            r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomWall(WIDTH - WALLWIDTH, Measure.units(30) + SECTION_HEIGHT * i, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH));

            r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomDoor(0, Measure.units(10) + SECTION_HEIGHT * i,
                    new com.byrjamin.wickedwizard.archive.maps.MapCoords(sc.getX(), sc.getY() + i),
                    new com.byrjamin.wickedwizard.archive.maps.MapCoords(sc.getX() - 1, sc.getY() + i),
                    com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.LEFT));
            r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomWall(0, Measure.units(30) + SECTION_HEIGHT * i, WALLWIDTH, (SECTION_HEIGHT) - WALLWIDTH * 4, WALLWIDTH));

        }

        //CEILING
        r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));


        //GROUND
        r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));

        for(int i = 0; i < xs; i++){
            for(int j = 0; j < ys + 1; j++){
                r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.GrapplePoint(WIDTH / 2, (SECTION_HEIGHT / 2) + (SECTION_HEIGHT / 2) * j));
            }
        }

        r.setRoomBackground(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomBackground(r.getX(), r.getY(), WIDTH, HEIGHT, Measure.units(15)));

        r.getRoomExits().addAll(r.getRoomDoors());
        r.getRoomExits().addAll(r.getRoomGrates());


    }

}
