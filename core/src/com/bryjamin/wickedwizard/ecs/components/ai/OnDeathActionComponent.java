package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 31/05/2017.
 */

public class OnDeathActionComponent extends Component {

    public Action action;

    public OnDeathActionComponent() {
        action = new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }
        };
    }


    public OnDeathActionComponent(Action action){
        this.action = action;
    }

}

