package com.byrjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.byrjamin.wickedwizard.archive.maps.MapCoords;

/**
 * Created by Home on 12/03/2017.
 */

public class DoorComponent extends Component {

    public MapCoords currentCoords;
    public MapCoords leaveCoords;

    public enum DIRECTION {
        left, right, up, down, center
    }

    public DIRECTION exit;
    public DIRECTION entry;

    public DoorComponent(){

    }

    public DoorComponent(MapCoords currentCoords, MapCoords leaveCoords, DIRECTION exit) {
        this.currentCoords = currentCoords;
        this.leaveCoords = leaveCoords;
        this.exit = exit;
    }
}
