package com.bryjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.enums.Direction;

import java.util.Comparator;

/**
 * Created by Home on 12/03/2017.
 */

public class DoorComponent extends Component {

    public MapCoords currentCoords;
    public MapCoords leaveCoords;

    public boolean ignore = false;


    public Direction exit;
    public Direction entry;

    public DoorComponent(){

    }

    public DoorComponent(MapCoords currentCoords, MapCoords leaveCoords, Direction exit) {
        this.currentCoords = currentCoords;
        this.leaveCoords = leaveCoords;
        this.exit = exit;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoorComponent that = (DoorComponent) o;

        if (!currentCoords.equals(that.currentCoords)) return false;
        return leaveCoords.equals(that.leaveCoords);

    }

    @Override
    public int hashCode() {
        int result = currentCoords.hashCode();
        result = 31 * result + leaveCoords.hashCode();
        return result;
    }


    public static final Comparator<DoorComponent> NAME_COMPARATOR = new Comparator<DoorComponent>() {
        @Override
        public int compare(DoorComponent doorComponent, DoorComponent t1) {
            return 0;
        }
    };








}
