package com.bryjamin.wickedwizard.factories.items;

import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemVitaminC;
import com.bryjamin.wickedwizard.utils.enums.ItemType;

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


        for(Item i : ItemResource.allItems){
            if(com.bryjamin.wickedwizard.screens.DataSave.isDataAvailable(i.getValues().challengeId)) {
                createItem(i);
            }
        }
    }

    private void createItem(Item item){
        itemOptionsArray.add(new ItemOptions(item));
    }


    public Item generateItemRoomItem(){
        return generateRoomItem(com.bryjamin.wickedwizard.utils.enums.ItemType.ITEM);
    }

    public Item generateShopRoomItem(){
        return generateRoomItem(com.bryjamin.wickedwizard.utils.enums.ItemType.SHOP);
    }

    public Item generateBossRoomItem(){
        return generateRoomItem(com.bryjamin.wickedwizard.utils.enums.ItemType.BOSS);
    }


    public Item generateAnyItem(){

        Array<ItemOptions> itemOptionsArray = new Array<ItemOptions>(this.itemOptionsArray);

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

    private Item generateRoomItem(ItemType itemType){

        Array<ItemOptions> itemOptionsArray = new Array<ItemOptions>();

        for(ItemOptions io : this.itemOptionsArray){
            if(io.item.getValues().itemTypes.contains(itemType, true)) itemOptionsArray.add(io);
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

        ItemOptions(Item item){
            this(item, false);
        }

        ItemOptions(Item item, boolean repeatable){
            this.item = item;
            this.repeatable = repeatable;
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
