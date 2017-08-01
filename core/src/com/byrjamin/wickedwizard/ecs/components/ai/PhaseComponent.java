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

    public boolean hasActionBeenPerformed = false;

    public Queue<Pair<Task, Condition>> phaseQueue = new Queue<Pair<Task, Condition>>();

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


    public void addPhaseSequence(Pair<Task, Condition>... sequence){
        for(Pair<Task, Condition> taskConditionPair : sequence){
            phaseQueue.addLast(taskConditionPair);
        }
    }

    public Task getCurrentPhase(){
        return phaseQueue.first().getLeft();
    }

}
