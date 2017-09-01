package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by BB on 27/05/2017.
 *
 * Component used in the Action After Time System.
 *
 * Stores the action to perform after a period of time
 */

public class ActionAfterTimeComponent extends Component {

    //TODO possible expansion. Repeat 'x' times. Or if set to -1 repeat endlessly;

    public Action action;
    public float timeUntilAction;
    public float resetTime;

    public boolean repeat = false;

    public ActionAfterTimeComponent(){

        action = new Task() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        timeUntilAction = 0;
    }

    public ActionAfterTimeComponent(Action action){
        this(action, 0, false);
    }

    public ActionAfterTimeComponent(Action action, float timeUntilAction){
        this(action, timeUntilAction, false);
    }

    public ActionAfterTimeComponent(Action action, float timeUntilAction, boolean repeat){
        this(action, timeUntilAction, timeUntilAction, repeat);
    }

    public ActionAfterTimeComponent(Action action, float timeUntilAction, float resetTime, boolean repeat){
        this.action = action;
        this.timeUntilAction = timeUntilAction;
        this.resetTime = resetTime;
        this.repeat = repeat;
    }


}
