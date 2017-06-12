package com.byrjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;

/**
 * Created by Home on 22/04/2017.
 */

public class LootComponent extends Component {
    public int moneyDrops = 1;

    public int lootDrops = 0;


    public LootComponent() {}

    public LootComponent(int moneyDrops){
        this.moneyDrops = moneyDrops;
    }

    public LootComponent(int moneyDrops, int lootDrops){
        this.moneyDrops = moneyDrops;
        this.lootDrops = lootDrops;
    }


}
