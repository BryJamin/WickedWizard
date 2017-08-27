package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.byrjamin.wickedwizard.GameTest;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.utils.enums.ItemType;

import org.junit.Assert;
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
        int i = itemStore.getItemOptionsArray().size;
        itemStore.generateItemRoomItem();
        assertTrue("Size is " + itemStore.getItemOptionsArray().size + " \n Previous size was " + i, itemStore.getItemOptionsArray().size == i - 1);


    }



    @Test
    public void testNoNullPointerWhenAllItemsGenerated() throws Exception {

        ItemStore itemStore = new ItemStore(new Random());
        int max = itemStore.getItemOptionsArray().size * 2;
        for(int i = 0; i < max; i++) itemStore.generateItemRoomItem();
        assertTrue(itemStore.generateItemRoomItem() != null);

    }


    @Test
    public void testBossItemsNotGeneratedWhenCallingItemRoomItem() throws Exception {
        ItemStore itemStore = new ItemStore(new Random());
        int max = itemStore.getItemOptionsArray().size * 2;
        for(int i = 0; i < max; i++) {
            itemStore.generateItemRoomItem();
        }
       // assertTrue(itemStore.getItemOptionsArray().size == 1);
        for(ItemStore.ItemOptions io : itemStore.getItemOptionsArray()) {
            assertTrue(io.item.getValues().itemTypes.contains(ItemType.BOSS, false));
            assertTrue(!io.item.getValues().itemTypes.contains(ItemType.ITEM, false));
        }
    }

    @Test
    public void testItemRegions() throws Exception {


        AssetManager assetManager = new AssetManager();

        TextureAtlasLoader textureAtlasLoader = new TextureAtlasLoader(new LocalFileHandleResolver());

        assetManager.setLoader(TextureAtlas.class,textureAtlasLoader);
        assetManager.load("/android/assets/sprite.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get("/android/assets/sprite.atlas", TextureAtlas.class);


        ItemStore itemStore = new ItemStore(new Random());

        for(Item item : ItemResource.allItems) {
            Assert.assertTrue(item.getValues().region.getLeft()
                            + " index "
                            + item.getValues().region.getRight()
                            + " is not inside of the sprite atlas",
                    atlas.findRegion(item.getValues().region.getLeft(), item.getValues().region.getRight()) != null);
        }

    }



    @Test
    public void testItemTypes() throws Exception {


        ItemStore itemStore = new ItemStore(new Random());

        AssetManager assetManager = new AssetManager();

        assetManager.load(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        assetManager.finishLoading();
        //TextureAtlas atlas = assetManager.get("/android/assets/sprite.atlas", TextureAtlas.class);


        WorldConfiguration config = new WorldConfigurationBuilder()
                .with(WorldConfigurationBuilder.Priority.HIGHEST,
                        new MovementSystem(),
                        new FindPlayerSystem(new PlayerFactory(assetManager).playerBag(0,0)))
                .build();

        World world = new World(config);

        int total = 0;

        int health = 0;
        int maxHealth = 0;
        int dmg = 0;
        int armor = 0;
        int firerate = 0;
        int speed = 0;
        int luck = 0;
        int range = 0;
        int shotspeed = 0;
        int accuracy = 0;
        int companion = 0;






        for(Item i : ItemResource.allItems) {

            if(i instanceof Companion){
                companion += 1;
                total++;
                continue;
            }

            StatComponent pre = new StatComponent();

            CurrencyComponent currency = new CurrencyComponent();

            Entity e = world.createEntity();
            e.edit().add(new StatComponent());
            e.edit().add(currency);

            i.applyEffect(world, e);

            StatComponent after = e.getComponent(StatComponent.class);

            health += (after.health > pre.health) ? 1 : 0;
            maxHealth += (after.maxHealth > pre.maxHealth) ? 1 : 0;
            dmg += (after.damage > pre.damage) ? 1 : 0;
            armor += (after.armor > pre.armor) ? 1 : 0;
            firerate += (after.fireRate > pre.fireRate) ? 1 : 0;
            speed += (after.speed > pre.speed) ? 1 : 0;
            luck += (after.luck > pre.luck) ? 1 : 0;
            shotspeed += (after.shotSpeed > pre.shotSpeed) ? 1 : 0;
            accuracy += (after.accuracy > pre.accuracy) ? 1 : 0;
            range += (after.range > pre.range) ? 1 : 0;

            total++;


        }


        System.out.println("Total Items: " + total);
        System.out.println("Total Items increasing health: " + health);
        System.out.println("Total Items increasing maxHealth: " + maxHealth);
        System.out.println("Total Items increasing damage: " + dmg);
        System.out.println("Total Items increasing armor: " + armor);
        System.out.println("Total Items increasing firerate: " + firerate);
        System.out.println("Total Items increasing speed: " + speed);
        System.out.println("Total Items increasing luck: " + luck);
        System.out.println("Total Items increasing shotspeed: " + shotspeed);
        System.out.println("Total Items increasing accuracy: " + accuracy);
        System.out.println("Total Items increasing range: " + range);
        System.out.println("Total Companions: " + companion);

    }



}
