package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 29/03/2017.
 */

public class PhaseComponent extends Component {

    public float currentPhaseTime = 0;
    public int currentPhase;

    public boolean hasActionBeenPerformed = false;

    private Array<Float> phaseTimers = new Array<Float>();

    private Array<Task> phaseArray = new Array<Task>();


    public Queue<Pair<Task, Condition>> phaseQueue = new Queue<Pair<Task, Condition>>();

    public Array<Integer> phaseSequence = new Array<Integer>();
    public Array<Integer> stateSequence = new Array<Integer>();


    public void addPhase(Float time, Task task){
        phaseQueue.addLast(new Pair<Task, Condition>(task, phaseTimeCondition(time)));
    }

    public void addPhase(Pair<Task, Condition> taskConditionPair){
        phaseQueue.addLast(taskConditionPair);
    }

    public void addPhase(Task task, Condition condition){
        phaseQueue.addLast(new Pair<Task, Condition>(task, condition));
    }

    private Condition phaseTimeCondition(final float time) {
        return new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(PhaseComponent.class).currentPhaseTime > time;
            }
        };
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

    public void addPhaseSequence(Pair<Task, Condition>... sequence){
        for(Pair<Task, Condition> taskConditionPair : sequence){
            phaseQueue.addLast(taskConditionPair);
        }
    }

    public Task getCurrentPhase(){
        return phaseArray.get(currentPhase);
    }

    public float getCurrentPhaseTimer(int phaseNo){
        return phaseTimers.get(phaseNo);
    }

    public void setCurrentPhase(int currentPhase) {
        this.currentPhase = currentPhase;
    }


    public Array<Integer> getPhaseSequence() {
        return phaseSequence;
    }
}
