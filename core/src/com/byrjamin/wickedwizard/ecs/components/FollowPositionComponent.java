package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;

/**
 * Created by Home on 05/04/2017.
 */

public class FollowPositionComponent extends Component{

    public Vector2 trackedPosition;

    public float offsetX;
    public float offsetY;

    public FollowPositionComponent(){
        trackedPosition = new Vector2();
    }

    public FollowPositionComponent(Vector2 trackedPosition) {
        this(trackedPosition, 0,0);
    }

    public FollowPositionComponent(Vector2 trackedPosition, float offsetX, float offsetY) {
        this.trackedPosition = trackedPosition;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
