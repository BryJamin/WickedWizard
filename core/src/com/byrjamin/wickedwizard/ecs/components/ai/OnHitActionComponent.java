package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by Home on 01/08/2017.
 */

public class OnHitActionComponent extends Component {

    public Action action;

    public OnHitActionComponent(){
        action = new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }
        };
    }

    public OnHitActionComponent(Action action) {
        this.action = action;
    }
}
