package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.byrjamin.wickedwizard.factories.items.HealthUp;
import com.byrjamin.wickedwizard.factories.items.Item;

/**
 * Created by Home on 09/04/2017.
 */

public class PickUpComponent extends Component{

    private Item item;


    public PickUpComponent(){
        item = new HealthUp();
    }

    public PickUpComponent(Item item){
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
