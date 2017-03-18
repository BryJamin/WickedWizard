package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.maps.MapCoords;

/**
 * Created by Home on 12/03/2017.
 */

public class DoorComponent extends Component {

    public MapCoords currentCoords;
    public MapCoords leaveCoords;

    public DoorComponent(){

    }

    public DoorComponent(MapCoords currentCoords, MapCoords leaveCoords) {
        this.currentCoords = currentCoords;
        this.leaveCoords = leaveCoords;
    }
}
