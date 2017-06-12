package com.byrjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.EntitySystem;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
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


    public class gesture extends GestureDetector.GestureAdapter {

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return super.tap(x, y, count, button);
        }
    }


}
