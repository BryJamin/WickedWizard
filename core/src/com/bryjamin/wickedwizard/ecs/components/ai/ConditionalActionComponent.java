package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

/**
 * Created by ae164 on 20/05/17.
 */

public class ConditionalActionComponent extends Component {


    public Array<com.bryjamin.wickedwizard.utils.Pair<Action, Condition>> actionConditionPairArray = new Array<com.bryjamin.wickedwizard.utils.Pair<Action, Condition>>();


    public ConditionalActionComponent(Condition condition, Action action) {
        actionConditionPairArray.add(new com.bryjamin.wickedwizard.utils.Pair<Action, Condition>(action, condition));
    }


    public ConditionalActionComponent(com.bryjamin.wickedwizard.utils.Pair<Action, Condition>... actionConditionPairs) {
        for(com.bryjamin.wickedwizard.utils.Pair<Action, Condition> actionConditionPair : actionConditionPairs){
            actionConditionPairArray.add(actionConditionPair);
        }
    }


    public void add(Condition condition, Action action){
        actionConditionPairArray.add(new com.bryjamin.wickedwizard.utils.Pair<Action, Condition>(action, condition));
    }

    public void add(com.bryjamin.wickedwizard.utils.Pair<Action, Condition>... actionConditionPairs){
        for(com.bryjamin.wickedwizard.utils.Pair<Action, Condition> actionConditionPair : actionConditionPairs){
            actionConditionPairArray.add(actionConditionPair);
        }
    }


    public ConditionalActionComponent(){
    }



}
