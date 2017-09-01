package com.bryjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;

/**
 * Created by BB on 03/06/2017.
 *
 * System for running actions of Components with the ActionOnTouchComponent when the screen touches
 * their collision boundaries
 *
 */

public class ActionOnTouchSystem extends EntitySystem {


    public ActionOnTouchSystem() {
        super(Aspect.all(com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void processSystem() {}

    @Override
    protected boolean checkProcessing() {
        return false;
    }

    /**
     * Runs a check to see if the inserted co-ordinates are contained in any of this system's entities'
     * collision boundaries. If they are an action is performed
     *
     * @param x - x position of the area of the screen that was touched
     * @param y - y position of the area of the screen that was touched
     *
     */


    /**
     * Runs a check to see if the inserted co-ordinates are contained in any of this system's entities'
     * collision boundaries. If they are an action is performed
     *
     * @param x - x position of the area of the screen that was touched
     * @param y - y position of the area of the screen that was touched
     * @return - True if an entity has been touched, False otherwise
     */
    public boolean touch(float x, float y){
        for(Entity e : this.getEntities()) {
            if (e.getComponent(CollisionBoundComponent.class).bound.contains(x, y)) {
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent.class).action.performAction(world, e);
                return true;
            }
        }

        return false;
    }

}
