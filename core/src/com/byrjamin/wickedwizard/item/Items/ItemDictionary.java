package com.byrjamin.wickedwizard.item.Items;

import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.item.ItemPresets;

import java.util.HashMap;

/**
 * Created by Home on 07/01/2017.
 */
public class ItemDictionary {

    private HashMap<ItemPresets, Item> itemHashMap = new HashMap<ItemPresets, Item>();

    public ItemDictionary(){
        itemHashMap.put(ItemPresets.ATTACK_UP, new AttackUp());
        itemHashMap.put(ItemPresets.HEALTH_UP, new HealthUp());
    }


    public Item getItem(ItemPresets key){
        return itemHashMap.get(key);
    }

}
