package com.byrjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;

/**
 * Created by Home on 22/04/2017.
 */

public class LootComponent extends Component {
    public int maxDrops = 1;

    public LootComponent() {}

    public LootComponent(int maxDrops){
        this.maxDrops = maxDrops;
    }


}
