package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;


/**
 * Created by BB on 25/08/2017.
 */

public class OnRoomLoadActionComponent extends Component {

    public Action action;
    public boolean repeat = false;

    public OnRoomLoadActionComponent (){
        action = new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }
        };
    }

    public OnRoomLoadActionComponent(Action action){
        this.action = action;
    }

}
