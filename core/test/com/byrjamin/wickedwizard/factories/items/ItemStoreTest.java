package com.byrjamin.wickedwizard.factories.items;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.byrjamin.wickedwizard.GameTest;
import com.byrjamin.wickedwizard.factories.items.ItemStore;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by Home on 05/07/2017.
 */

public class ItemStoreTest extends GameTest {


    @Test
    public void testGeneratedItemIsRemoved() throws Exception {

        ItemStore itemStore = new ItemStore(new Random());
        int i = itemStore.getItemOptions().size;
        itemStore.generateItemRoomItem();
        assertTrue(itemStore.getItemOptions().size == i - 1);

    }



    @Test
    public void testNoNullPointerWhenAllItemsGenerated() throws Exception {

        ItemStore itemStore = new ItemStore(new Random());
        int max = itemStore.getItemOptions().size * 2;
        for(int i = 0; i < max; i++) itemStore.generateItemRoomItem();
        assertTrue(itemStore.generateItemRoomItem() != null);

    }


    @Test
    public void testBossItemsNotGeneratedWhenCallingItemRoomItem() throws Exception {
        ItemStore itemStore = new ItemStore(new Random());
        int max = itemStore.getItemOptions().size * 2;
        for(int i = 0; i < max; i++) {
            itemStore.generateItemRoomItem();
        }
        assertTrue(itemStore.getItemOptions().size == 1);
        assertTrue(itemStore.getItemOptions().first().availables.contains(ItemStore.Available.BOSS, true));
        assertTrue(!itemStore.getItemOptions().first().availables.contains(ItemStore.Available.ITEM, true));

    }
    

}
