package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 31/05/2017.
 */

public class OnDeathActionComponent extends Component {

    public Task task;

    public OnDeathActionComponent() {
        task = new Task() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };
    }


    public OnDeathActionComponent(Task task){
        this.task = task;
    }

}

