package com.bryjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.bryjamin.wickedwizard.factories.items.PickUp;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemVitaminC;

/**
 * Created by Home on 17/04/2017.
 */

public class AltarComponent extends Component {

    public PickUp pickUp;
    public boolean hasItem = true;

    public AltarComponent(){
        pickUp = new ItemVitaminC();
    }

    public AltarComponent(PickUp pickUp){
        this.pickUp = pickUp;
    }

}
