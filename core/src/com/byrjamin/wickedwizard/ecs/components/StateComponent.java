package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 07/03/2017.
 */
public class StateComponent extends Component {


    private int state = 0;
    public float stateTime = 0.0f;


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        this.stateTime = 0.0f;
    }

}
