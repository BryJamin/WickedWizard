package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.byrjamin.wickedwizard.factories.items.HealthUp;
import com.byrjamin.wickedwizard.factories.items.Item;

/**
 * Created by Home on 17/04/2017.
 */

public class AltarComponent extends Component {

    private Item item;

    public AltarComponent(){
        item = new HealthUp();
    }

    public AltarComponent(Item item){
        this.item = item;
    }

}
