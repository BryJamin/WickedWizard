package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGen;
import com.byrjamin.wickedwizard.utils.WeightedObject;

/**
 * Created by Home on 05/06/2017.
 */

public interface LevelRoomSet {


    Array<WeightedObject<ArenaGen>> getAllRooms();

    Array<WeightedObject<ArenaGen>> getOmniRooms();



}
