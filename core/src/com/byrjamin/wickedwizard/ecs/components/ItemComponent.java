package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.byrjamin.wickedwizard.factories.items.HealthUp;
import com.byrjamin.wickedwizard.factories.items.Item;

/**
 * Created by Home on 09/04/2017.
 */

public class ItemComponent extends Component{

    private Item item;


    public ItemComponent(){
        item = new HealthUp();
    }

    public ItemComponent(Item item){
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
