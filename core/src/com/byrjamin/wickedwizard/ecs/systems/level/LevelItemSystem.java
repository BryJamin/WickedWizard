package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.byrjamin.wickedwizard.factories.items.ItemStore;

import java.util.Random;

/**
 * Created by Home on 13/05/2017.
 */

//TODO currently not relevant but may be depending on if items can drop randomly from certain areas
public class LevelItemSystem extends BaseSystem {


    private ItemStore itemStore;
    private Random random;

    public LevelItemSystem(ItemStore itemStore, Random random){
        this.random = random;
        this.itemStore = itemStore;
    }

    @Override
    protected void processSystem() {


    }

}
