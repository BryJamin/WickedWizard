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

    private Array<Task> phaseArray = new Array<Task>();

    public Array<Integer> phaseSequence = new Array<Integer>();
    public Array<Integer> stateSequence = new Array<Integer>();


    public void addPhase(Float time, Task task){
        phaseArray.add(task);
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

    public Task getCurrentPhase(int phaseNo){
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
