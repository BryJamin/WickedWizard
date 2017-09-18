package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;


/**
 * Created by BB on 25/08/2017.
 *
 * Actions that take place as a room is being loaded
 *
 */

public class DuringRoomLoadActionComponent extends Component {

    public Action action;
    public boolean repeat = false;

    public DuringRoomLoadActionComponent(){
        action = new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }
        };
    }

    public DuringRoomLoadActionComponent(Action action){
        this.action = action;
    }

}
