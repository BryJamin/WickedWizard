package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
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


    //Normal Chest DropRates
    private static final int chestMoney = 90;
    private static final int chestKey = 5;
    private static final int chestHeart = 5;

    //Rare Chest DropRates
    private static final int rareChestMoney = 85;
    private static final int rareChest5Money = 5;
    private static final int rareChestKey = 5;
    private static final int rareChestHeart = 5;

    private float luck;

    private Random random;


    public LuckSystem(Random random){
        this.random = random;
    }



    @Override
    protected void processSystem() {
        luck = world.getSystem(FindPlayerSystem.class).getPC(StatComponent.class).luck;
    }


    public void rollForLoot(LootComponent lc, float x, float y){


        for(int i = 0; i < lc.moneyDrops; i++){
            spawnMoney(x,y);
        }

        for(int i = 0; i < lc.lootDrops; i++){
            spawnPickUp(x,y);



        }

    }


    public void spawnMoney(float x, float y){
        Entity e = world.createEntity();
        ItemFactory itemFactory = new ItemFactory(world.getSystem(RenderingSystem.class).getAssetManager());
        for(Component c : itemFactory.createIntangibleFollowingPickUpBag(x, y, new MoneyPlus1())) e.edit().add(c);
    }


    public void spawnPickUp(float x , float y){
        Array<WeightedObject<PickUp>> pickUps = new Array<WeightedObject<PickUp>>();
        //TODO apply luck
        pickUps.add(new WeightedObject<PickUp>(null, 70));
        pickUps.add(new WeightedObject<PickUp>(new HealthUp(), 5));
        pickUps.add(new WeightedObject<PickUp>(new KeyUp(), 5));

        WeightedRoll<PickUp> weightedRoll = new WeightedRoll<PickUp>(pickUps, random);

        PickUp chosen = weightedRoll.roll();

        if(chosen != null) {

            Entity e = world.createEntity();
            ItemFactory itemFactory = new ItemFactory(world.getSystem(RenderingSystem.class).getAssetManager());
            for (Component c : itemFactory.createPickUpBag(x, y, chosen)) e.edit().add(c);

        }

    }



    public void spawnChestPickUp(float x , float y){

        Array<WeightedObject<PickUp>> pickUps = new Array<WeightedObject<PickUp>>();
        pickUps.add(new WeightedObject<PickUp>(new HealthUp(), chestHeart + (int) luck));
        pickUps.add(new WeightedObject<PickUp>(new KeyUp(), chestKey + (int) luck));
        pickUps.add(new WeightedObject<PickUp>(new MoneyPlus1(), chestMoney - (int) luck));

        WeightedRoll<PickUp> weightedRoll = new WeightedRoll<PickUp>(pickUps, random);

        PickUp chosen = weightedRoll.roll();

        if(chosen != null) {

            Entity e = world.createEntity();

            ItemFactory itemFactory = new ItemFactory(world.getSystem(RenderingSystem.class).getAssetManager());

            if(chosen instanceof MoneyPlus1) {
                for(Component c : itemFactory.createIntangibleFollowingPickUpBag(x, y, chosen)) e.edit().add(c);
            } else {
                for (Component c : itemFactory.createPickUpBag(x, y, chosen)) e.edit().add(c);
            }

        }

    }

}
