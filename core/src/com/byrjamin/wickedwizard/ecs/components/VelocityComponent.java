package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Home on 02/03/2017.
 */
public class VelocityComponent extends Component{

    public Vector2 velocity;

    public VelocityComponent(float vlx, float vly) {
        velocity = new Vector2(vlx, vly);
    }

    public VelocityComponent(){
        this(0,0);
    }
}
