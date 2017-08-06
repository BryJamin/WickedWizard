package com.byrjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.byrjamin.wickedwizard.factories.items.pickups.PickUpHalfHealthUp;
import com.byrjamin.wickedwizard.factories.items.PickUp;

/**
 * Created by Home on 09/04/2017.
 */

public class PickUpComponent extends Component{

    private PickUp pickUp;


    public PickUpComponent(){
        pickUp = new PickUpHalfHealthUp();
    }

    public PickUpComponent(PickUp pickUp){
        this.pickUp = pickUp;
    }

    public PickUp getPickUp() {
        return pickUp;
    }
}
