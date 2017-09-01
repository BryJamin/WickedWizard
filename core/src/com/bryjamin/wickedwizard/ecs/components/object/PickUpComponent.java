package com.bryjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.bryjamin.wickedwizard.factories.items.PickUp;

/**
 * Created by Home on 09/04/2017.
 */

public class PickUpComponent extends Component{

    private PickUp pickUp;


    public PickUpComponent(){
        pickUp = new com.bryjamin.wickedwizard.factories.items.pickups.PickUpHalfHealthUp();
    }

    public PickUpComponent(PickUp pickUp){
        this.pickUp = pickUp;
    }

    public PickUp getPickUp() {
        return pickUp;
    }
}
