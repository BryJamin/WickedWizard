package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 16/02/2017.
 */
public abstract class RoomLayout {


    public enum ROOM_LAYOUT {
        OMNI,
        WIDTH_2,
        HEIGHT_2,
        L_BLOCK
    }

    protected ROOM_LAYOUT layout;


    public RoomLayout(ROOM_LAYOUT layout) {
        this.layout = layout;
    }

    public void applyLayout(Room r){
        r.setLayout(layout);
    }

    protected final float SECTION_WIDTH = MainGame.GAME_WIDTH;
    protected final float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

}
