package com.byrjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.byrjamin.wickedwizard.factories.items.passives.DamageUp;
import com.byrjamin.wickedwizard.factories.items.Item;

/**
 * Created by Home on 17/04/2017.
 */

public class AltarComponent extends Component {

    public Item item;
    public boolean hasItem = true;

    public AltarComponent(){
        item = new DamageUp();
    }

    public AltarComponent(Item item){
        this.item = item;
    }

}
