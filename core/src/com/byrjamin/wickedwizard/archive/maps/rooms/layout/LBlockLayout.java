package com.byrjamin.wickedwizard.archive.maps.rooms.layout;

import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 12/02/2017.
 */
public class LBlockLayout {

    private com.byrjamin.wickedwizard.archive.maps.MapCoords defaultCoords = new com.byrjamin.wickedwizard.archive.maps.MapCoords(0,0);

    public LBlockLayout(){
    }


    public void applyLayout(com.byrjamin.wickedwizard.archive.maps.rooms.Room r){

        com.byrjamin.wickedwizard.archive.maps.MapCoords defaultCoords = r.getStartCoords();

        r.addCoords(new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1));
        r.addCoords(new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1));
        r.addCoords(new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 1));

        r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomDoor(0, Measure.units(10),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.LEFT));

        r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomDoor(0, Measure.units(10),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX() - 1, defaultCoords.getY() + 1),
                com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.LEFT));

        r.add(new com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomDoor(0, Measure.units(10),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY() + 1),
                new com.byrjamin.wickedwizard.archive.maps.MapCoords(defaultCoords.getX() + 3, defaultCoords.getY() + 1),
                com.byrjamin.wickedwizard.archive.maps.rooms.components.RoomExit.EXIT_DIRECTION.RIGHT));


        r.getRoomExits().addAll(r.getRoomDoors());

    }

}
