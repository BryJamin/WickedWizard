package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.PickUp;
import com.byrjamin.wickedwizard.factories.items.pickups.HealthUp;
import com.byrjamin.wickedwizard.factories.items.pickups.KeyUp;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.byrjamin.wickedwizard.utils.WeightedObject;
import com.byrjamin.wickedwizard.utils.WeightedRoll;

import java.util.Random;

/**
 * Created by Home on 21/04/2017.
 */

public class LuckSystem extends BaseSystem {

    @Override
    protected void processSystem() {

    }



    public void spawnPickUp(float x , float y){

        Random random = new Random();

        Array<WeightedObject<PickUp>> pickUps = new Array<WeightedObject<PickUp>>();
        pickUps.add(new WeightedObject<PickUp>(null, 50));
        pickUps.add(new WeightedObject<PickUp>(new HealthUp(), 5));
        pickUps.add(new WeightedObject<PickUp>(new KeyUp(), 5));
        pickUps.add(new WeightedObject<PickUp>(new MoneyPlus1(), 40));

        WeightedRoll<PickUp> weightedRoll = new WeightedRoll<PickUp>(pickUps, random);

        PickUp chosen = weightedRoll.roll();

        if(chosen != null) {

            Entity e = world.createEntity();
            if(chosen instanceof MoneyPlus1) {
                for(Component c : ItemFactory.createIntangibleFollowingPickUpBag(x, y, chosen)) e.edit().add(c);
            } else {
                for (Component c : ItemFactory.createPickUpBag(x, y, chosen)) e.edit().add(c);
            }

        }




    }

}
