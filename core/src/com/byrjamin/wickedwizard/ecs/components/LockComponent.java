package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 19/03/2017.
 */

public class LockComponent extends Component {

    public boolean lock;


    public LockComponent(){
        this(true);
    }

    public LockComponent(boolean lock){
        this.lock = lock;
    }


}
