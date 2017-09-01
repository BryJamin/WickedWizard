package com.bryjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;

import static com.bryjamin.wickedwizard.ecs.components.identifiers.LootComponent.TYPE.ENEMY;

/**
 * Created by Home on 22/04/2017.
 */

public class LootComponent extends Component {

    public enum TYPE{
        CHEST, BOMBCHEST, ENEMY
    }


    public TYPE type = ENEMY;




    public int moneyDrops = 1;
    public int minMoneyDrops = 1;

    public int lootDrops = 1;


    public LootComponent() {}

    public LootComponent(int moneyDrops){
        this.moneyDrops = moneyDrops;
    }

    public LootComponent(int moneyDrops, int lootDrops){
        this.moneyDrops = moneyDrops;
        this.lootDrops = lootDrops;
    }


    public LootComponent(TYPE type, int moneyDrops, int lootDrops){
        this.type = type;
        this.moneyDrops = moneyDrops;
        this.lootDrops = lootDrops;
    }

}
