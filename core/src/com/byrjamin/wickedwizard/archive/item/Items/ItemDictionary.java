package com.byrjamin.wickedwizard.archive.item.Items;

import java.util.HashMap;

/**
 * Created by Home on 07/01/2017.
 */
public class ItemDictionary {

    private HashMap<com.byrjamin.wickedwizard.archive.item.ItemPresets, com.byrjamin.wickedwizard.archive.item.Item> itemHashMap = new HashMap<com.byrjamin.wickedwizard.archive.item.ItemPresets, com.byrjamin.wickedwizard.archive.item.Item>();

    public ItemDictionary(){
        itemHashMap.put(com.byrjamin.wickedwizard.archive.item.ItemPresets.ATTACK_UP, new AttackUp());
        itemHashMap.put(com.byrjamin.wickedwizard.archive.item.ItemPresets.HEALTH_UP, new HealthUp());
    }


    public com.byrjamin.wickedwizard.archive.item.Item getItem(com.byrjamin.wickedwizard.archive.item.ItemPresets key){
        return itemHashMap.get(key);
    }

}
