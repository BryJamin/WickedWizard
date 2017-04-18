package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;

/**
 * Created by Home on 18/04/2017.
 */

public class ActionOnTouchComponent extends Component {
    public Action action;

    public ActionOnTouchComponent() {
        action = new Action() {
            @Override
            public void performAction(World w, Entity e) {

            }

            @Override
            public void cleanUpAction(World w, Entity e) {

            }
        };
    }


    public ActionOnTouchComponent(Action action){
        this.action = action;
    }

}
