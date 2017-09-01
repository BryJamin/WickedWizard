package com.bryjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;
import com.bryjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 06/04/2017.
 */

public class DirectionalComponent extends Component {

    public enum PRIORITY {
        HIGHEST,
        HIGH,
        NORMAL,
        LOW,
        LOWEST,
    }

    public PRIORITY priority = PRIORITY.LOWEST;
    private Direction direction;


    public void setDirection(Direction direction, PRIORITY priority) {
        if(this.priority.compareTo(priority) >= 0) {
            this.priority = priority;
            this.direction = direction;

        }
    }

    public Direction getDirection() {
        return direction;
    }

}
