package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 29/03/2017.
 */

public class PhaseComponent extends Component {

    public float currentPhaseTime = 0;
    public int currentPhase;
    private Array<Float> phaseTimers = new Array<Float>();

    private Array<Action> phaseArray = new Array<Action>();

    public Array<Integer> phaseSequence = new Array<Integer>();
    public Array<Integer> stateSequence = new Array<Integer>();


    public void addPhase(Float time, Action action){
        phaseArray.add(action);
        phaseTimers.add(time);
    }

    //TODO maybe add an error handle

    /**
     * Add sequence in which the Phases are called (May need to add something in relation to health
     * later)
     * @param sequence
     */
    public void addPhaseSequence(int... sequence){
        for(int i : sequence){
            phaseSequence.add(i);
        }
    }

    public void addStateSequence(int... sequence){
        for(int i : sequence){
            stateSequence.add(i);
        }
    }

    public Action getCurrentPhase(int phaseNo){
        return phaseArray.get(phaseNo);
    }

    public float getCurrentPhaseTimer(int phaseNo){
        return phaseTimers.get(phaseNo);
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
    }

    public int getCurrentPhase() {
        return currentPhase;
    }

    public Array<Integer> getPhaseSequence() {
        return phaseSequence;
    }
}
