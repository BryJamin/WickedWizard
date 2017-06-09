package com.byrjamin.wickedwizard.ecs.components.ai;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 17/04/2017.
 */

public class ProximityTriggerAIComponent extends Component{

    public Array<HitBox> proximityHitBoxes = new Array<HitBox>();//Make a hitbox class with an offsetx and offset y?

    public Action action;
    public boolean triggered = false;

    public ProximityTriggerAIComponent(){

    }

    public ProximityTriggerAIComponent(Action action, HitBox... hitBoxes){
        proximityHitBoxes.addAll(hitBoxes);
        this.action = action;
    }

}
