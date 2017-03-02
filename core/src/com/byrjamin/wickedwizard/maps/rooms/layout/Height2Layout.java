package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.components.GrapplePoint;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;

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


    public void applyLayout(Room r){
        super.applyLayout(r);

        MapCoords sc = r.getStartCoords();
        r.addCoords(new MapCoords(sc.getX(), sc.getY() + 1));
        r.HEIGHT = SECTION_HEIGHT * 2;
        r.WIDTH = SECTION_WIDTH;

        HEIGHT = SECTION_HEIGHT * 2;

    //    r.add(new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH, wallTextures));

        int xs = (int) WIDTH / (int) SECTION_WIDTH;
        int ys = (int) HEIGHT / (int) SECTION_HEIGHT;

        for(int i = 0; i < ys; i++){

            r.add(new RoomDoor(WIDTH - WALLWIDTH, Measure.units(10) + SECTION_HEIGHT * i,
                    new MapCoords(sc.getX() + xs - 1, sc.getY() + i),
                    new MapCoords(sc.getX() + xs, sc.getY() + i),
                    RoomExit.EXIT_DIRECTION.RIGHT));
            r.add(new RoomWall(WIDTH - WALLWIDTH, Measure.units(30) + SECTION_HEIGHT * i, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH));

            r.add(new RoomDoor(0, Measure.units(10) + SECTION_HEIGHT * i,
                    new MapCoords(sc.getX(), sc.getY() + i),
                    new MapCoords(sc.getX() - 1, sc.getY() + i),
                    RoomExit.EXIT_DIRECTION.LEFT));
            r.add(new RoomWall(0, Measure.units(30) + SECTION_HEIGHT * i, WALLWIDTH, (SECTION_HEIGHT) - WALLWIDTH * 4, WALLWIDTH));

        }

        //CEILING
        r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));


        //GROUND
        r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));

        for(int i = 0; i < xs; i++){
            for(int j = 0; j < ys + 1; j++){
                r.add(new GrapplePoint(WIDTH / 2, (SECTION_HEIGHT / 2) + (SECTION_HEIGHT / 2) * j));
            }
        }

        r.setRoomBackground(new RoomBackground(r.getX(), r.getY(), WIDTH, HEIGHT, Measure.units(15)));

        r.getRoomExits().addAll(r.getRoomDoors());
        r.getRoomExits().addAll(r.getRoomGrates());


    }

}
