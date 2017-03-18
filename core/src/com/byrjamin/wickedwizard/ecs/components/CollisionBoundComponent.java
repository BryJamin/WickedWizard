package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.helper.Measure;

/**
 * Created by Home on 04/03/2017.
 */
public class CollisionBoundComponent extends Component{

    public Rectangle bound;

    public CollisionBoundComponent(Rectangle bound){
        this.bound = bound;
    }

    public CollisionBoundComponent(){
        this(new Rectangle(0,0, Measure.units(5), Measure.units(5)));
    }

    public float getCenterX() {
        return bound.x + bound.width / 2;
    }

    public float getCenterY() {
        return bound.y + bound.height / 2;
    }


}
