package com.byrjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 12/03/2017.
 */

public class DoorComponent extends Component {

    public MapCoords currentCoords;
    public MapCoords leaveCoords;


    public Direction exit;
    public Direction entry;

    public DoorComponent(){

    }

    public DoorComponent(MapCoords currentCoords, MapCoords leaveCoords, Direction exit) {
        this.currentCoords = currentCoords;
        this.leaveCoords = leaveCoords;
        this.exit = exit;
    }
}
