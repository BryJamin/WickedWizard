package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 12/02/2017.
 */
public class LBlockLayout {


    private Array<? extends TextureRegion> backgroundTextures;
    private Array<? extends TextureRegion> wallTextures;

    private MapCoords defaultCoords = new MapCoords(0,0);

    public LBlockLayout(Array<? extends TextureRegion> backgroundTextures, Array<? extends TextureRegion> wallTextures){
        this.backgroundTextures = backgroundTextures;
        this.wallTextures = wallTextures;
    }


    public void applyLayout(Room r){

        MapCoords defaultCoords = r.getStartCoords();

        r.addCoords(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));
        r.addCoords(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1));
        r.addCoords(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 1));

        r.add(new RoomDoor(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                RoomExit.EXIT_DIRECTION.LEFT));

        r.add(new RoomDoor(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY() + 1),
                RoomExit.EXIT_DIRECTION.LEFT));

        r.add(new RoomDoor(0, Measure.units(10),
                new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 1),
                new MapCoords(defaultCoords.getX() + 3, defaultCoords.getY() + 1),
                RoomExit.EXIT_DIRECTION.RIGHT));


        r.setRoomBackground(new RoomBackground(backgroundTextures, 0, 0 , 1000, 1000, Measure.units(15)));


        r.getRoomExits().addAll(r.getRoomDoors());

    }

}
