package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 27/05/2017.
 */

public class ActionAfterTimeComponent extends Component {

    //TODO possible expansion. Repeat 'x' times. Or if set to -1 repeat endlessly;

    public Action action;
    public float timeUntilAction;
    public float resetTime;

    public boolean repeat = false;

    public ActionAfterTimeComponent(){

        action = new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        timeUntilAction = 0;
    }

    public ActionAfterTimeComponent(Action action, float timeUntilAction){
        this.action = action;
        this.timeUntilAction = timeUntilAction;
        this.resetTime = timeUntilAction;
    }



}
