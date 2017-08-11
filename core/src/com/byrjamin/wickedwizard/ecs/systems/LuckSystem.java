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
import com.byrjamin.wickedwizard.factories.items.pickups.PickUpArmorUp;
import com.byrjamin.wickedwizard.factories.items.pickups.PickUpFullHealthUp;
import com.byrjamin.wickedwizard.factories.items.pickups.PickUpHalfHealthUp;
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


    private static final int halfHeartChance = 50;
    private static final int fullHeartChance = 25;
    private static final int armorHeartChance = 50;
    private static final int enemyNullChance = 1250;

    private static final int startFullChance = 100;
    private static final int minimumChanceForItem = 25;

    private static final float moneyLuckMultiplier = 0.2f;
    private static final float itemLuckMultiplier = 1.5f;
    private static final float enemyLuckMultiplier = 1.5f;

    private float luck;

    private Random random;


    public LuckSystem(Random random){
        this.random = random;
    }



    @Override
    protected void processSystem() {
        luck = world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).luck;
    }


    public void rollForLoot(LootComponent lc, float x, float y){


        if(lc.type == LootComponent.TYPE.CHEST){
            chestDrops(lc, x, y);
        } else {

            for (int i = 0; i < lc.moneyDrops; i++) {
                spawnMoney(x, y);
            }

            for (int i = 0; i < lc.lootDrops; i++) {
                spawnEnemyPickUp(x, y);
            }
        }

    }



    public void chestDrops(LootComponent lc, float x, float y){

        //Money

        for(int i = 0; i < lc.minMoneyDrops; i++){
            spawnMoney(x, y);
        }

        float startingChance = 100;

        for(int i = lc.minMoneyDrops; i < lc.moneyDrops; i++){

            if(random.nextFloat() * 100 <= startingChance){
                spawnMoney(x,y);
                startingChance -= (10 - (luck * moneyLuckMultiplier));
            } else {
                break;
            }

        }



        float totalChance = startFullChance - (luck * itemLuckMultiplier);


        for(int i = 0; i < lc.lootDrops; i++) {


            if(random.nextFloat() * totalChance <= minimumChanceForItem) {

                Array<WeightedObject<PickUp>> pickUps = new Array<WeightedObject<PickUp>>();
                pickUps.add(new WeightedObject<PickUp>(new PickUpHalfHealthUp(), halfHeartChance));
                pickUps.add(new WeightedObject<PickUp>(new PickUpFullHealthUp(), fullHeartChance));
                pickUps.add(new WeightedObject<PickUp>(new PickUpArmorUp(), armorHeartChance));

                WeightedRoll<PickUp> weightedRoll = new WeightedRoll<PickUp>(pickUps, random);

                PickUp chosen = weightedRoll.roll();

                if (chosen != null) {

                    Entity e = world.createEntity();
                    ItemFactory itemFactory = new ItemFactory(world.getSystem(RenderingSystem.class).getAssetManager());
                    for (Component c : itemFactory.createPickUpBag(x, y, chosen)) e.edit().add(c);

                }

                totalChance += totalChance;

                System.out.println(totalChance);



            } else {

                System.out.println("OH NO I'M OUT!" + totalChance);
                break;
            }


        }

    }




    public void spawnMoney(float x, float y){
        Entity e = world.createEntity();
        ItemFactory itemFactory = new ItemFactory(world.getSystem(RenderingSystem.class).getAssetManager());
        for(Component c : itemFactory.createIntangibleFollowingPickUpBag(x, y, new MoneyPlus1())) e.edit().add(c);
    }


    public void spawnEnemyPickUp(float x , float y){
        Array<WeightedObject<PickUp>> pickUps = new Array<WeightedObject<PickUp>>();
        //TODO apply luck
        pickUps.add(new WeightedObject<PickUp>(null, enemyNullChance));
        pickUps.add(new WeightedObject<PickUp>(new PickUpHalfHealthUp(), halfHeartChance));
        pickUps.add(new WeightedObject<PickUp>(new PickUpFullHealthUp(), fullHeartChance));
        pickUps.add(new WeightedObject<PickUp>(new PickUpArmorUp(), armorHeartChance));

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
        pickUps.add(new WeightedObject<PickUp>(new PickUpHalfHealthUp(), chestHeart + (int) luck));
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
