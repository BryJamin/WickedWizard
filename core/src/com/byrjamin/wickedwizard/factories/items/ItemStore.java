package com.byrjamin.wickedwizard.factories.items;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemAce;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemAimAssist;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemCriticalEye;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemKeenEye;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemBlockOfEnergy;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemIronBody;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemSlimeCoat;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemSquareBuckler;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemVitaminC;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemAnger;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemLuckyShot;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemMiniCatapult;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemSmoulderingEmber;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemStability;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemElasticity;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemMinorAccelerant;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemRunedFragment;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemSwiftShot;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemTacticalKnitwear;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemHyperTrophy;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemIronFragment;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemSarcasticLion;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemSootheNote;
import com.byrjamin.wickedwizard.factories.items.passives.health.Medicine;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemForgottenFigment;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemGoldenFigment;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemJadeFigment;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemThreeLeafClover;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemClearSight;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemFireSight;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemLaserScope;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemLostLettersRangeFireRate;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemNeatCube;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemQuadonometry;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemScope;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemBoringRock;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemBubble;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemDullFeather;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemLostLettersShotSpeedAccuracy;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemMomentum;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemShinyFeather;
import com.byrjamin.wickedwizard.factories.items.passives.speed.ItemQuickness;

import java.util.Random;

/**
 * Created by Home on 04/07/2017.
 */

public class ItemStore {



    //TODO the general idea of this class is to create a tagging system for all items
    //TODO by this I mean I'll create an array with Pair<WeightedObject<Item>, Array<Available>>
    //TODO when you get an item you go through the pair array and see if the avaliable is relevant to
    //TODO what you're after. e.g. You need a boss item (it only grabs boss items
    //TODO you then roll for an item and return it. You then remove the

    public enum Available {
        SHOP, BOSS, ITEM
    }


    private Array<ItemOptions> itemOptionsArray = new Array<ItemOptions>();

    private Random random;

    public ItemStore(Random random){
        this.random = random;

        //Accuracy
        createItem(new ItemAce());
        createItem(new ItemAimAssist());
        createItem(new ItemBlockOfEnergy());
        createItem(new ItemCriticalEye());
        createItem(new ItemKeenEye());


        //Armor
        createItem(new ItemIronBody());
        createItem(new ItemSlimeCoat());
        createItem(new ItemSquareBuckler());
        createItem(new ItemVitaminC());

        //Damage
        createItem(new ItemAnger(), Available.BOSS);
        createItem(new ItemLuckyShot());
        createItem(new ItemMiniCatapult());
        createItem(new ItemSmoulderingEmber());
        createItem(new ItemStability());

        //Firerate
        createItem(new ItemElasticity());
        createItem(new ItemMinorAccelerant());
        createItem(new ItemRunedFragment());
        createItem(new ItemSwiftShot());
        createItem(new ItemTacticalKnitwear());

        //Health
        createItem(new ItemHyperTrophy());
        createItem(new ItemIronFragment());
        createItem(new ItemSarcasticLion());
        createItem(new ItemSootheNote());
        createItem(new Medicine());

        //Luck
        createItem(new ItemForgottenFigment());
        createItem(new ItemGoldenFigment());
        createItem(new ItemJadeFigment());
        createItem(new ItemThreeLeafClover());

        //Range
        createItem(new ItemClearSight());
        createItem(new ItemFireSight());
        createItem(new ItemLaserScope());
        createItem(new ItemLostLettersRangeFireRate());
        createItem(new ItemNeatCube());
        createItem(new ItemQuadonometry());
        createItem(new ItemScope());

        //shotspeed
        createItem(new ItemBoringRock());
        createItem(new ItemBubble());
        createItem(new ItemDullFeather());
        createItem(new ItemLostLettersShotSpeedAccuracy());
        createItem(new ItemMomentum(), Available.ITEM);
        createItem(new ItemShinyFeather());

        //Speed
        createItem(new ItemQuickness());

        //Other

    }

    private void createItem(Item item){
        itemOptionsArray.add(new ItemOptions(item, Available.SHOP, Available.ITEM));
    }


    private void createItem(Item item, Available... avability){
        itemOptionsArray.add(new ItemOptions(item, avability));
    }


    public Item generateItemRoomItem(){
        return generateRoomItem(Available.ITEM);
    }

    public Item generateShopRoomItem(){
        return generateRoomItem(Available.SHOP);
    }

    public Item generateBossRoomItem(){
        return generateRoomItem(Available.BOSS);
    }

    private Item generateRoomItem(Available available){

        Array<ItemOptions> itemOptionsArray = new Array<ItemOptions>();

        for(ItemOptions io : this.itemOptionsArray){
            if(io.availables.contains(available, true)) itemOptionsArray.add(io);
        }

        Item item;
        if(itemOptionsArray.size > 0) {
            int i = random.nextInt(itemOptionsArray.size);
            item = itemOptionsArray.get(i).item;
            if(!itemOptionsArray.get(i).repeatable) {
                this.itemOptionsArray.removeValue(itemOptionsArray.get(i), true);
            }
        } else {
            item = new ItemVitaminC();
        }
        return item;
    }


    /**
     *  Updates the list of avaliable items within the item store
     *  using item ids.
     *
     *  The item ids used to identify what should be within the itemstore
     *
     * @param itemIds - Ids that will be uploaded to itemOptions. If the
     */
    public void updateItemOptions(Array<String> itemIds){


        Array<ItemOptions> copyArray = new Array<ItemOptions>();
        copyArray.addAll(itemOptionsArray);

        for(String id : itemIds){
            for(ItemOptions itemOptions : itemOptionsArray){
                if(itemOptions.item.getValues().id.equals(id)) {
                    copyArray.removeValue(itemOptions, true);
                }
            }
        }


        for(ItemOptions itemOptions : copyArray){
            itemOptionsArray.removeValue(itemOptions, false);
        }


        System.out.println("After: " + itemOptionsArray.size);

    }




    public Array<String> getItemStringArray(){

        Array<String> stringArray = new Array<String>();

        for(ItemOptions io: itemOptionsArray){
            stringArray.add(io.item.getValues().id);
        }

        return stringArray;
    }




    public class ItemOptions {

        public Item item;

        private boolean repeatable = false;

        public Array<Available> availables = new Array<Available>();

        ItemOptions(Item item){
            this(item, false, Available.SHOP, Available.ITEM, Available.BOSS);
        }

        ItemOptions(Item item, Available... avaliability){
            this(item, false, avaliability);
        }

        ItemOptions(Item item, boolean repeatable, Available... avaliability){
            this.item = item;
            this.repeatable = repeatable;

            for(Available a : avaliability) this.availables.add(a);
        }

    }


    public Array<ItemOptions> getItemOptionsArray() {
        return itemOptionsArray;
    }


    @Override
    public String toString() {

        String s = "Items Avaliable are :";

        System.out.println(itemOptionsArray.size);

        for(ItemOptions itemOption : this.itemOptionsArray){
            s = s + "\n" + itemOption.item.getValues().name;
        }


        return s;
    }
}
