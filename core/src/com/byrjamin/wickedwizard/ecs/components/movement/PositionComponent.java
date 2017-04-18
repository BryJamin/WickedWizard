package com.byrjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Home on 02/03/2017.
 */
public class PositionComponent extends Component {

    public Vector3 position;

    public PositionComponent(float x, float y){
        position = new Vector3(x,y,0);
    }

    public PositionComponent(){
        this(0,0);
    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }


}
