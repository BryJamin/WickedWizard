package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by ae164 on 20/05/17.
 */

/**
 * Performs an Action when combat starts,
 * Cleans Up action when Combat is over.
 */
public class InCombatActionComponent extends Component {
    public Action action;

    private boolean isInCombat;

    public InCombatActionComponent() {
        action = new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };
    }


    public InCombatActionComponent(Action action){
        this.action = action;
    }

}

