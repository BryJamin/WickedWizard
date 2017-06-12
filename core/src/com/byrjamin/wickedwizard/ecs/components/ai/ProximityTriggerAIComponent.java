package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 17/04/2017.
 */

public class ProximityTriggerAIComponent extends Component{

    public Array<HitBox> proximityHitBoxes = new Array<HitBox>();//Make a hitbox class with an offsetx and offset y?

    public Task task;
    public boolean triggered = false;
    public boolean onCameraTrigger = false;

    public ProximityTriggerAIComponent(){

    }

    public ProximityTriggerAIComponent(Task task, boolean onCameraTrigger){
        this.task = task;
        this.onCameraTrigger = onCameraTrigger;
    }

    public ProximityTriggerAIComponent(Task task, HitBox... hitBoxes){
        proximityHitBoxes.addAll(hitBoxes);
        this.task = task;
    }

}
