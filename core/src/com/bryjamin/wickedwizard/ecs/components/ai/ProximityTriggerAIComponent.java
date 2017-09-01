package com.bryjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 17/04/2017.
 */

public class ProximityTriggerAIComponent extends Component{

    public Array<com.bryjamin.wickedwizard.utils.collider.HitBox> proximityHitBoxes = new Array<com.bryjamin.wickedwizard.utils.collider.HitBox>();//Make a hitbox class with an offsetx and offset y?

    public Task task;
    public boolean triggered = false;
    public boolean onCameraTrigger = false;

    public ProximityTriggerAIComponent(){

    }

    public ProximityTriggerAIComponent(Task task, boolean onCameraTrigger){
        this.task = task;
        this.onCameraTrigger = onCameraTrigger;
    }

    public ProximityTriggerAIComponent(Task task, com.bryjamin.wickedwizard.utils.collider.HitBox... hitBoxes){
        proximityHitBoxes.addAll(hitBoxes);
        this.task = task;
    }

}
