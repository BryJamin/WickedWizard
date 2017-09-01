package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Queue;

/**
 * Created by Home on 29/03/2017.
 */

public class PhaseComponent extends Component {

    public float currentPhaseTime = 0;

    public boolean hasActionBeenPerformed = false;

    public Queue<com.bryjamin.wickedwizard.utils.Pair<Task, Condition>> phaseQueue = new Queue<com.bryjamin.wickedwizard.utils.Pair<Task, Condition>>();

    public void addPhase(Float time, Task task){
        phaseQueue.addLast(new com.bryjamin.wickedwizard.utils.Pair<Task, Condition>(task, phaseTimeCondition(time)));
    }

    public void addPhase(com.bryjamin.wickedwizard.utils.Pair<Task, Condition> taskConditionPair){
        phaseQueue.addLast(taskConditionPair);
    }

    public void addPhase(Task task, Condition condition){
        phaseQueue.addLast(new com.bryjamin.wickedwizard.utils.Pair<Task, Condition>(task, condition));
    }

    private Condition phaseTimeCondition(final float time) {
        return new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return entity.getComponent(PhaseComponent.class).currentPhaseTime > time;
            }
        };
    }


    public void addPhaseSequence(com.bryjamin.wickedwizard.utils.Pair<Task, Condition>... sequence){
        for(com.bryjamin.wickedwizard.utils.Pair<Task, Condition> taskConditionPair : sequence){
            phaseQueue.addLast(taskConditionPair);
        }
    }

    public Task getCurrentPhase(){
        return phaseQueue.first().getLeft();
    }

}
