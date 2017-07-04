package com.byrjamin.wickedwizard.ecs.components.texture;

import com.artemis.Component;
import com.badlogic.gdx.utils.Queue;

/**
 * Created by Home on 07/03/2017.
 */
public class AnimationStateComponent extends Component {

    public enum State {

        LOCKED(1), UNLOCKED(2), DEFAULT(0);

        State(int state) {
            this.state = state;
        }

        private int state;

        public int getState() {
            return state;
        }
    }
    public static final int DEFAULT = 0;
    public static final int FIRING = 10;


    private int defaultState = 0;

    private int currentState = 0;

    private Queue<Integer> stateQueue = new Queue<Integer>();

    public float stateTime = 0.0f;


    public AnimationStateComponent(){
        defaultState = DEFAULT;
    }

    public AnimationStateComponent(int startState){
        defaultState = startState;
    }

    public int getDefaultState() {
        return defaultState;
    }

    public void setDefaultState(int defaultState) {
        this.defaultState = defaultState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
        this.stateTime = 0.0f;
    }

    public Queue<Integer> getStateQueue() {
        return stateQueue;
    }

    public void queueAnimationState(int state){
        if(stateQueue.size != 0){
            if(stateQueue.first() == state) this.stateTime = 0.0f;
        } else {
            stateQueue.addFirst(state);
        }
    }

    public int getCurrentState() {
        return currentState;
    }
}
