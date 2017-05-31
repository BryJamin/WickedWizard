package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by ae164 on 20/05/17.
 */

public class ConditionalActionComponent extends Component {

    public Condition condition;
    public Action action;


    public ConditionalActionComponent(Condition condition, Action action) {
        this.condition = condition;
        this.action = action;
    }


    public ConditionalActionComponent(){

        condition = new Condition() {
            @Override
            public boolean condition(World world, Entity entity) {
                return false;
            }
        };


        action = new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };


    }



}
