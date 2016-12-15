package com.byrjamin.wickedwizard.item;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by Home on 15/12/2016.
 */
public class ItemGenerator {


    private Array<Item> items;

    public ItemGenerator(){
        items = new Array<Item>();

        for(ItemsPresets.itemList i : ItemsPresets.itemList.values()){
            items.add(i.getItem());
        }
    }



    public Item getItem(long seed){
        Random rand = new Random(seed);

        return ItemsPresets.itemList.ATTACK_UP.getItem();

        //return items.get(rand.nextInt(items.size));
    }



}
