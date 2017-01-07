package com.byrjamin.wickedwizard.item;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.item.Items.AttackUp;
import com.byrjamin.wickedwizard.item.Items.HealthUp;
import com.byrjamin.wickedwizard.item.Items.ItemDictionary;

import java.util.Random;

/**
 * Created by Home on 15/12/2016.
 */
public class ItemGenerator {

    private ItemDictionary itemDictionary = new ItemDictionary();

    private Array<ItemPresets> avaliableItems;


    /**
     * Creates an array of items from the ItemPresets list of items.
     */
    //TODO
    public ItemGenerator(){

        avaliableItems = new Array<ItemPresets>();
        avaliableItems.add(ItemPresets.ATTACK_UP);
        avaliableItems.add(ItemPresets.HEALTH_UP);

    }

    public ItemGenerator(Array<ItemPresets> avaliableItems){
        this.avaliableItems = avaliableItems;
    }



    /**
     * Picks an item from the generated item array.
     * @param seed - Random Seed
     * @return
     */
    public Item getItem(long seed){
        Random rand = new Random(seed);
        //return itemDictionary.getItem(avaliableItems.get(rand.nextInt(avaliableItems.size)));
        return itemDictionary.getItem(ItemPresets.HEALTH_UP);
        //return items.get(rand.nextInt(items.size));
    }



}
