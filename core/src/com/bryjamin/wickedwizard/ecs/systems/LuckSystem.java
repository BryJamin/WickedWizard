package com.bryjamin.wickedwizard.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.OnDeathActionComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.bryjamin.wickedwizard.factories.BombFactory;
import com.bryjamin.wickedwizard.factories.items.ItemFactory;
import com.bryjamin.wickedwizard.factories.items.PickUp;
import com.bryjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.bryjamin.wickedwizard.factories.items.pickups.PickUpArmorUp;
import com.bryjamin.wickedwizard.factories.items.pickups.PickUpFullHealthUp;
import com.bryjamin.wickedwizard.factories.items.pickups.PickUpHalfHealthUp;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.WeightedObject;
import com.bryjamin.wickedwizard.utils.WeightedRoll;

import java.util.Random;

import static com.bryjamin.wickedwizard.ecs.systems.LuckSystem.RandomizerOptions.BOMB;
import static com.bryjamin.wickedwizard.ecs.systems.LuckSystem.RandomizerOptions.ITEM;
import static com.bryjamin.wickedwizard.ecs.systems.LuckSystem.RandomizerOptions.MONEY;
import static com.bryjamin.wickedwizard.ecs.systems.LuckSystem.RandomizerOptions.PICKUP;

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


    public enum RandomizerOptions {
        ITEM, BOMB, MONEY, PICKUP
    }

    private static final int randomizerMoneyChance = 40;
    private static final int randomizerPickUpChance = 40;
    private static final int randomizerBombChance = 70;
    private static final int randomizerItemChance = 40;





    private static final int startFullChance = 100;
    private static final int minimumChanceForItem = 25;

    private static final float moneyLuckMultiplier = 0.2f;
    private static final float itemLuckMultiplier = 1.5f;
    private static final float enemyLuckMultiplier = 1.5f;

    private float luck;


    private boolean isEnemyDropsOn = true;

    private AssetManager assetManager;



    private ItemFactory itemFactory;
    private Random random;


    public LuckSystem(AssetManager assetManager, Random random){
        this.assetManager = assetManager;
        this.random = random;
        this.itemFactory = new ItemFactory(assetManager);
    }



    @Override
    protected void processSystem() {
        luck = world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).luck;
    }


    public void rollForLoot(LootComponent lc, float x, float y){

        if(!this.isEnabled()) return;

        if(lc.type == LootComponent.TYPE.CHEST){
            chestDrops(lc, x, y);
        } else {


            if(isEnemyDropsOn) {
                for (int i = 0; i < lc.moneyDrops; i++) {
                    spawnMoney(x, y);
                }

                for (int i = 0; i < lc.lootDrops; i++) {
                    spawnEnemyPickUp(x, y);
                }
            }
        }

    }



    private void chestDrops(LootComponent lc, float x, float y){

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

                Array<WeightedObject<PickUp>> pickUps = new Array<WeightedObject<com.bryjamin.wickedwizard.factories.items.PickUp>>();
                pickUps.add(new WeightedObject<PickUp>(new PickUpHalfHealthUp(), halfHeartChance));
                pickUps.add(new WeightedObject<PickUp>(new PickUpFullHealthUp(), fullHeartChance));
                pickUps.add(new WeightedObject<PickUp>(new PickUpArmorUp(), armorHeartChance));

                WeightedRoll<PickUp> weightedRoll = new WeightedRoll<PickUp>(pickUps, random);

                PickUp chosen = weightedRoll.roll();

                if (chosen != null) {

                    Entity e = world.createEntity();
                    ItemFactory itemFactory = new ItemFactory(assetManager);
                    for (Component c : itemFactory.createPickUpBag(x, y, chosen)) e.edit().add(c);

                }

                totalChance += totalChance;


            } else {
                break;
            }


        }

    }




    private void spawnMoney(float x, float y){
        Entity e = world.createEntity();
        ItemFactory itemFactory = new ItemFactory(assetManager);
        for(Component c : itemFactory.createMoneyPickUpBag(x, y, new MoneyPlus1())) e.edit().add(c);
    }


    private void spawnEnemyPickUp(float x , float y){

        Array<WeightedObject<PickUp>> pickUps = new Array<WeightedObject<com.bryjamin.wickedwizard.factories.items.PickUp>>();
        //TODO apply luck
        pickUps.add(new WeightedObject<PickUp>(null, enemyNullChance));
        pickUps.add(new WeightedObject<PickUp>(new PickUpHalfHealthUp(), halfHeartChance));
        pickUps.add(new WeightedObject<PickUp>(new PickUpFullHealthUp(), fullHeartChance));
        pickUps.add(new WeightedObject<PickUp>(new PickUpArmorUp(), armorHeartChance));

        WeightedRoll<PickUp> weightedRoll = new WeightedRoll<PickUp>(pickUps, random);

        PickUp chosen = weightedRoll.roll();

        if(chosen != null) {
            Entity e = world.createEntity();
            for (Component c : itemFactory.createPickUpBag(x, y, chosen)) e.edit().add(c);

        }

    }

    public void turnOffEnemyDrops(){
        this.isEnemyDropsOn = false;
    }


    public boolean randomizerItemSpawn(OnDeathActionComponent onDeathActionComponent, final float x, final float y, final Color altarColor){

        WeightedRoll<RandomizerOptions> weightedRoll = new WeightedRoll<RandomizerOptions>(random,
                new WeightedObject<RandomizerOptions>(ITEM, randomizerItemChance),
                new WeightedObject<RandomizerOptions>(BOMB, randomizerBombChance),
                new WeightedObject<RandomizerOptions>(MONEY, randomizerMoneyChance),
                new WeightedObject<RandomizerOptions>(PICKUP, randomizerPickUpChance)
        );

        RandomizerOptions randomizerOptions = weightedRoll.roll();

        createRandomizerItem(randomizerOptions, onDeathActionComponent, x, y, altarColor);

        return randomizerOptions == ITEM;

    }



    public void createRandomizerItem(RandomizerOptions randomizerOption, OnDeathActionComponent onDeathActionComponent, final float x, final float y, final Color altarColor){

        switch (randomizerOption){

            case BOMB:

                onDeathActionComponent.action = new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        BagToEntity.bagToEntity(world.createEntity(), new BombFactory(assetManager).bomb(x, y, BombFactory.BOMB_LIFE));
                    }
                };

                break;

            case MONEY:


                onDeathActionComponent.action = new Action() {
                    @Override
                    public void performAction(World world, Entity e) {


                        int minimumMoney = 0;
                        int maximumMoney = 10;

        /*                for(int i = 0; i < minimumMoney; i++){
                            BagToEntity.bagToEntity(world.createEntity(), itemFactory.createMoneyPickUpBag(x, y, new MoneyPlus1()));
                        }*/

                        float startingChance = 100;

                        for(int i = minimumMoney; i < maximumMoney; i++){

                            if(random.nextFloat() * 100 <= startingChance){
                                startingChance -= (10 - (luck * moneyLuckMultiplier));
                                BagToEntity.bagToEntity(world.createEntity(), itemFactory.createMoneyPickUpBag(x, y, new MoneyPlus1()));
                            } else {
                                break;
                            }

                        }

                    }
                };

                break;


            case PICKUP:

                onDeathActionComponent.action = new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        Array<WeightedObject<PickUp>> pickUps = new Array<WeightedObject<com.bryjamin.wickedwizard.factories.items.PickUp>>();
                        //TODO apply luck
                        pickUps.add(new WeightedObject<PickUp>(new PickUpHalfHealthUp(), halfHeartChance));
                        pickUps.add(new WeightedObject<PickUp>(new PickUpFullHealthUp(), fullHeartChance));
                        pickUps.add(new WeightedObject<PickUp>(new PickUpArmorUp(), armorHeartChance));

                        WeightedRoll<PickUp> pickUpRoll = new WeightedRoll<PickUp>(pickUps, random);

                        final PickUp chosen = pickUpRoll.roll();


                        BagToEntity.bagToEntity(world.createEntity(), itemFactory.createPickUpBag(x, y, chosen));

                    }
                };


                break;

            case ITEM:

                onDeathActionComponent.action = new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        Entity itemAltar = BagToEntity.bagToEntity(world.createEntity(), itemFactory.createCenteredPresetItemAltarBag(
                                x,
                                y,
                                altarColor,
                                world.getSystem(ChangeLevelSystem.class).getJigsawGenerator().getItemStore().generateAnyItem()
                                ));
                        itemAltar.getComponent(OnRoomLoadActionComponent.class).action.performAction(world, itemAltar);
                    }
                };


        }


    }






}
