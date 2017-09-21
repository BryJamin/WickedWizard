package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.utils.Pair;

/**
 * Created by ae164 on 20/05/17.
 */

public class ConditionalActionComponent extends Component {


    public Array<Pair<Action, Condition>> actionConditionPairArray = new Array<Pair<Action, Condition>>();


    public ConditionalActionComponent(Condition condition, Action action) {
        actionConditionPairArray.add(new Pair<Action, Condition>(action, condition));
    }


    public ConditionalActionComponent(Pair<Action, Condition>... actionConditionPairs) {
        for(Pair<Action, Condition> actionConditionPair : actionConditionPairs){
            actionConditionPairArray.add(actionConditionPair);
        }
    }


    public void add(Condition condition, Action action){
        actionConditionPairArray.add(new Pair<Action, Condition>(action, condition));
    }

    public void add(Pair<Action, Condition>... actionConditionPairs){
        for(Pair<Action, Condition> actionConditionPair : actionConditionPairs){
            actionConditionPairArray.add(actionConditionPair);
        }
    }


    public ConditionalActionComponent(){
    }



}
