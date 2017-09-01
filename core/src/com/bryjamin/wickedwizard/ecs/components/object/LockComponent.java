package com.bryjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;

/**
 * Created by Home on 19/03/2017.
 */

public class LockComponent extends Component {

    private boolean lock;

    public LockComponent(){
        this(false);
    }

    public LockComponent(boolean lock){
        this.lock = lock;
    }

    public void lock(){
        lock = true;
    }

    public void unlock(){
        lock = false;
    }

    public boolean isLocked(){
        return lock;
    }


}
