package com.byrjamin.wickedwizard.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Home on 02/03/2017.
 */
public class PositionComponent extends Component {

    public float x;
    public float y;
    public Vector2 position;

    public PositionComponent(float x, float y){
        position = new Vector2(x,y);
    }

    public PositionComponent(){
        this(0,0);
    }


}
