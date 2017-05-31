package com.byrjamin.wickedwizard.factories.arenas;

import com.byrjamin.wickedwizard.utils.MapCoords;

/**
 * Created by Home on 13/04/2017.
 */

public interface ArenaGen {
    Arena createArena(MapCoords defaultCoords);
}
