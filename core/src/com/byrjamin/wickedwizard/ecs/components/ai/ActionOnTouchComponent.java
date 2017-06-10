package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 18/04/2017.
 */

public class ActionOnTouchComponent extends Component {
    public Task task;

    public ActionOnTouchComponent() {
        task = new Task() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };
    }


    public ActionOnTouchComponent(Task task){
        this.task = task;
    }

}
