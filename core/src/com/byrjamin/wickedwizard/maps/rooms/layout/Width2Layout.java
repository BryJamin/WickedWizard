package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;

/**
 * Created by Home on 07/02/2017.
 */
public class Width2Layout {


    public float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

    public float WIDTH = MainGame.GAME_WIDTH;
    public float HEIGHT = MainGame.GAME_HEIGHT;

    public float WALLWIDTH = Measure.units(5);

    private Array<? extends TextureRegion> backgroundTextures;
    private Array<? extends TextureRegion> wallTextures;

    private MapCoords defaultCoords = new MapCoords(0,0);

    public Width2Layout(Array<? extends TextureRegion> backgroundTextures, Array<? extends TextureRegion> wallTextures){
        this.backgroundTextures = backgroundTextures;
        this.wallTextures = wallTextures;
    }


    public void applyLayout(Room r){

        MapCoords startCoords = r.getStartCoords();
        r.addCoords(new MapCoords(startCoords.getX() + 1, startCoords.getY()));

        r.HEIGHT = SECTION_HEIGHT;
        r.WIDTH = SECTION_WIDTH * 2;

        WIDTH = SECTION_WIDTH * 2;

        r.add(new RoomDoor(0, Measure.units(10),
                new MapCoords(startCoords.getX(), startCoords.getY()),
                new MapCoords(startCoords.getX() - 1, startCoords.getY()),
                RoomExit.EXIT_DIRECTION.LEFT));
        r.add(new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH, wallTextures));

        int xs = (int) WIDTH / (int) SECTION_WIDTH;
        int ys = (int) HEIGHT / (int) SECTION_HEIGHT;

        System.out.println(startCoords.getX() + xs);

        System.out.println("XS is " + xs);
        System.out.println("YS is " + ys);


        r.add(new RoomDoor(WIDTH - WALLWIDTH, Measure.units(10),
                new MapCoords(startCoords.getX() + xs - 1, startCoords.getY()),
                new MapCoords(startCoords.getX() + xs, startCoords.getY()),
                RoomExit.EXIT_DIRECTION.RIGHT));
        r.add(new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH, wallTextures));

        //CEILING
        r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH, wallTextures));
        r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH, wallTextures));


        //GROUND
        r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH, wallTextures));
        r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH, wallTextures));

        for(MapCoords m : r.getMapCoordsArray()){
            System.out.println(m.toString());
        }

        r.setRoomBackground(new RoomBackground(backgroundTextures, 0, 0 , WIDTH, HEIGHT, Measure.units(15)));

        r.getRoomExits().addAll(r.getRoomDoors());
        r.getRoomExits().addAll(r.getRoomGrates());

    }

}
