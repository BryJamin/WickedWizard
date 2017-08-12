package com.byrjamin.wickedwizard.ecs.components.graphics;

import com.artemis.Component;

/**
 * Created by BB on 12/08/2017.
 *
 * Used by the camera system to identify the strength to the shake the camera
 *
 */

public class CameraShakeComponent extends Component {


    public float intensity;

    public CameraShakeComponent(){
        this.intensity = 0;
    }

    public CameraShakeComponent(float intensity){
        this.intensity = intensity;
    }


}
