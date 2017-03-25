package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 07/03/2017.
 */
public class StateComponent extends Component {

    public enum State {

        LOCKED(1), UNLOCKED(2);

        State(int state) {
            this.state = state;
        }

        private int state;

        public int getState() {
            return state;
        }
    }

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
