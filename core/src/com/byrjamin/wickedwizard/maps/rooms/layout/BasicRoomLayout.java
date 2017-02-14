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
import com.byrjamin.wickedwizard.maps.rooms.components.RoomGrate;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;

/**
 * Created by Home on 05/02/2017.
 */
public class BasicRoomLayout {

    public float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

    public float WIDTH = MainGame.GAME_WIDTH;
    public float HEIGHT = MainGame.GAME_HEIGHT;

    public float WALLWIDTH = Measure.units(5);

    private Array<? extends TextureRegion> backgroundTextures;
    private Array<? extends TextureRegion> wallTextures;

    private MapCoords defaultCoords = new MapCoords(0,0);

    public BasicRoomLayout(MapCoords defaultCoords, Array<? extends TextureRegion> backgroundTextures, Array<? extends TextureRegion> wallTextures){
        this.backgroundTextures = backgroundTextures;
        this.wallTextures = wallTextures;
        this.defaultCoords = defaultCoords;
    }

    public BasicRoomLayout(Array<? extends TextureRegion> backgroundTextures, Array<? extends TextureRegion> wallTextures){
        this.backgroundTextures = backgroundTextures;
        this.wallTextures = wallTextures;
    }


    public void applyLayout(Room r){

        MapCoords defaultCoords = r.getStartCoords();

        //LEFT
        r.add(new RoomDoor(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                RoomExit.EXIT_DIRECTION.LEFT));
        r.add(new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT, WALLWIDTH, wallTextures));


        //RIGHT
        r.add(new RoomDoor(WIDTH - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                RoomExit.EXIT_DIRECTION.RIGHT));
        r.add(new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT, WALLWIDTH, wallTextures));


        //TELEPORT UP
        RoomGrate rt = new RoomGrate(new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                RoomExit.EXIT_DIRECTION.UP);
        rt.setCenter(WIDTH / 2, HEIGHT - WALLWIDTH * 3);
        r.add(rt);


        //TELEPORT DOWN
        rt = new RoomGrate(new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() - 1),
                RoomExit.EXIT_DIRECTION.DOWN);
        rt.setCenter(WIDTH / 2, WALLWIDTH * 4);
        r.add(rt);


        //CEILING
        r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH, wallTextures));
        r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH, wallTextures));


        //GROUND
        r.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH, wallTextures));
        r.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH, wallTextures));

        r.setRoomBackground(new RoomBackground(backgroundTextures, 0, 0 , WIDTH, HEIGHT, Measure.units(15)));

        for(RoomWall rw : r.getRoomWalls()){
            r.getGroundBoundaries().add(rw.getBounds());
        }

        r.getRoomExits().addAll(r.getRoomDoors());
        r.getRoomExits().addAll(r.getRoomGrates());

    }


}
