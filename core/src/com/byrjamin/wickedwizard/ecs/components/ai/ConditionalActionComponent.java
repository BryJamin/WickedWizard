package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by ae164 on 20/05/17.
 */

public class ConditionalActionComponent extends Component {


    public Array<Pair<Task, Condition>> taskConditionPairArray = new Array<Pair<Task, Condition>>();


    public ConditionalActionComponent(Condition condition, Task task) {
        taskConditionPairArray.add(new Pair<Task, Condition>(task, condition));
    }


    public ConditionalActionComponent(Pair<Task, Condition>... taskConditionPairs) {
        for(Pair<Task, Condition> taskConditionPair : taskConditionPairs){
            taskConditionPairArray.add(taskConditionPair);
        }
    }


    public void add(Condition condition, Task task){
        taskConditionPairArray.add(new Pair<Task, Condition>(task, condition));
    }


    public ConditionalActionComponent(){
    }



}
