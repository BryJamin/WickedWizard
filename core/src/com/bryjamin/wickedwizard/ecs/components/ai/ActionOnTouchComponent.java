package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 18/04/2017.
 */

public class ActionOnTouchComponent extends Component {
    public Action action;
    public boolean isEnabled = true;

    public ActionOnTouchComponent() {
        action = new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }
        };
    }


    public ActionOnTouchComponent(Action action){
        this.action = action;
    }

}
