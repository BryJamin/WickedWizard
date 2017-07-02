package com.byrjamin.wickedwizard.ecs.components.movement;

import com.artemis.Component;

/**
 * Created by Home on 11/03/2017.
 */
public class BounceComponent extends Component{

    public boolean horizontal;
    public boolean vertical;

    public BounceComponent(){
        horizontal = true;
        vertical = true;
    }

    public BounceComponent(boolean horizontal, boolean vertical){
        this.horizontal = horizontal;
        this.vertical = vertical;
    }





}
