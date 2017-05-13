package com.byrjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.factories.items.passives.damage.Anger;

/**
 * Created by Home on 17/04/2017.
 */

public class AltarComponent extends Component {

    public PickUp pickUp;
    public boolean hasItem = true;

    public AltarComponent(){
        pickUp = new Anger();
    }

    public AltarComponent(PickUp pickUp){
        this.pickUp = pickUp;
    }

}
