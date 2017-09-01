package com.bryjamin.wickedwizard.ecs.components.movement;

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

    public VelocityComponent(VelocityComponent vc) {
        velocity = new Vector2(vc.velocity.x, vc.velocity.y);
    }

    public VelocityComponent(){
        this(0,0);
    }
}
