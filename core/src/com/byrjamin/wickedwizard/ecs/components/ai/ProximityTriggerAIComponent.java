package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Home on 17/04/2017.
 */

public class ProximityTriggerAIComponent extends Component{
    public Rectangle bound;
    public Action action;
    public boolean triggered = false;

    public ProximityTriggerAIComponent(){
        bound = new Rectangle();
    }

    public ProximityTriggerAIComponent(Rectangle bound, Action action){
        this.bound = bound;
        this.action = action;
    }

}
