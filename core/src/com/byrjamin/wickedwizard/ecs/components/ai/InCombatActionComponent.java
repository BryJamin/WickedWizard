package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;

/**
 * Created by ae164 on 20/05/17.
 */

/**
 * Performs an Task when combat starts,
 * Cleans Up action when Combat is over.
 */
public class InCombatActionComponent extends Component {
    public Task task;

    private boolean isInCombat;

    public InCombatActionComponent() {
        task = new Task() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };
    }


    public InCombatActionComponent(Task task){
        this.task = task;
    }

}

