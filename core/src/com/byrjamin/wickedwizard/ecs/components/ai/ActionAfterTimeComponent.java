package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 27/05/2017.
 */

public class ActionAfterTimeComponent extends Component {

    //TODO possible expansion. Repeat 'x' times. Or if set to -1 repeat endlessly;

    public Action task;
    public float timeUntilAction;
    public float resetTime;

    public boolean repeat = false;

    public ActionAfterTimeComponent(){

        task = new Task() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        timeUntilAction = 0;
    }

    public ActionAfterTimeComponent(Action task, float timeUntilAction){
        this(task, timeUntilAction, false);
    }

    public ActionAfterTimeComponent(Action task, float timeUntilAction, boolean repeat){
        this(task, timeUntilAction, timeUntilAction, repeat);
    }

    public ActionAfterTimeComponent(Action task, float timeUntilAction, float resetTime, boolean repeat){
        this.task = task;
        this.timeUntilAction = timeUntilAction;
        this.resetTime = resetTime;
        this.repeat = repeat;
    }


}
