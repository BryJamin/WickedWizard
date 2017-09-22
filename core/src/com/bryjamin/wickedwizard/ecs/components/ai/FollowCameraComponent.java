package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;

/**
 * Created by BB on 19/09/2017.
 */

public class FollowCameraComponent extends Component {

    public float offsetX;
    public float offsetY;

    public FollowCameraComponent(){}

    public FollowCameraComponent(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
