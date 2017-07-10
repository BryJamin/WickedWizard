package com.byrjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.assets.PreferenceStrings;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;

/**
 * Created by Home on 03/06/2017.
 */

public class ActionOnTouchSystem extends EntitySystem {


    public ActionOnTouchSystem() {
        super(Aspect.all(ActionOnTouchComponent.class, CollisionBoundComponent.class));
    }

    @Override
    protected void processSystem() {

    }


    public void touch(float x, float y){
        for(Entity e : this.getEntities()) {
            if (e.getComponent(CollisionBoundComponent.class).bound.contains(x, y)) {
                e.getComponent(ActionOnTouchComponent.class).action.performAction(world, e);
            }
        }
    }

}
