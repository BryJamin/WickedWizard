package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.helper.timer.StateTimer;

/**
 * Created by Home on 05/03/2017.
 */
public class WeaponComponent extends Component{

    public float reloadTime;
    public float currentTime;

    public enum PROJECTILETYPE {
        STRAIGHT, ARC, GUIDED
    }

    public enum ONHITTYPE {
        EXPLOSIVE,
    }

    public Array<Component> additionalComponenets = new Array<Component>();

    public StateTimer timer;

    public WeaponComponent(float reloadTime, float currentTime){
        this.reloadTime = reloadTime;
        this.currentTime = currentTime;
        timer = new StateTimer(reloadTime, currentTime);
    }

    public WeaponComponent(float reloadTime){
        this(reloadTime, 0);
    }


    public WeaponComponent(){
        this(2.0f, 0);
    }


}
