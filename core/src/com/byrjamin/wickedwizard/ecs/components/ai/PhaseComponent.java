package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 29/03/2017.
 */

public class PhaseComponent extends Component {

    public float currentPhaseTime = 0;
    public int currentPhase;

    private Array<Bag<Component>> phases = new Array<Bag<Component>>();
    private Array<Float> phaseTimers = new Array<Float>();

    public Array<Integer> phaseSequence = new Array<Integer>();

    /**
     * Adds a phase to the PhaseComponent
     * @param bag - Bag of Componenets for the new Phase
     * @param time - The amount of time the phase lasts
     */
    public void addPhase(Bag<Component> bag, Float time){
        phases.add(bag);
        phaseTimers.add(time);
    }


    public void addPhase(Float time, Component... components){
        Bag<Component> bag = new Bag<Component>();
        for(Component c : components){
            bag.add(c);
        }
        phases.add(bag);
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

    public Bag<Component> getCurrentPhaseComponents(int phaseNo){
        return phases.get(phaseNo);
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
