package com.bryjamin.wickedwizard.factories.items;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bryjamin.wickedwizard.GameTest;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.PlayerIDs;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.ecs.systems.physics.MovementSystem;
import com.bryjamin.wickedwizard.factories.PlayerFactory;

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
            assertTrue(io.item.getValues().getItemTypes().contains(com.bryjamin.wickedwizard.utils.enums.ItemType.BOSS, false));
            assertTrue(!io.item.getValues().getItemTypes().contains(com.bryjamin.wickedwizard.utils.enums.ItemType.ITEM, false));
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

        for(com.bryjamin.wickedwizard.factories.items.Item item : ItemResource.allItems) {
            Assert.assertTrue(item.getValues().getRegion().getLeft()
                            + " index "
                            + item.getValues().getRegion().getRight()
                            + " is not inside of the sprite atlas",
                    atlas.findRegion(item.getValues().getRegion().getLeft(), item.getValues().getRegion().getRight()) != null);
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
                        new FindPlayerSystem(new PlayerFactory(assetManager).playerBag(PlayerIDs.LEAH_ID,0,0)))
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
        int shotSize = 0;
        int accuracy = 0;
        int companion = 0;



        int nhealth = 0;
        int nmaxHealth = 0;
        int ndmg = 0;
        int narmor = 0;
        int nfirerate = 0;
        int nspeed = 0;
        int nluck = 0;
        int nrange = 0;
        int nshotspeed = 0;
        int nshotSize = 0;
        int naccuracy = 0;
        int ncompanion = 0;






        for(com.bryjamin.wickedwizard.factories.items.Item i : ItemResource.allItems) {

            if(i instanceof com.bryjamin.wickedwizard.factories.items.Companion){
                companion += 1;
                total++;
                continue;
            }

            com.bryjamin.wickedwizard.ecs.components.StatComponent pre = new com.bryjamin.wickedwizard.ecs.components.StatComponent();

            CurrencyComponent currency = new CurrencyComponent();

            Entity e = world.createEntity();
            e.edit().add(new com.bryjamin.wickedwizard.ecs.components.StatComponent());
            e.edit().add(currency);

            i.applyEffect(world, e);

            com.bryjamin.wickedwizard.ecs.components.StatComponent after = e.getComponent(StatComponent.class);

            health += (after.getHealth() > pre.getHealth()) ? 1 : 0;
            maxHealth += (after.maxHealth > pre.maxHealth) ? 1 : 0;
            dmg += (after.damage > pre.damage) ? 1 : 0;
            armor += (after.armor > pre.armor) ? 1 : 0;
            firerate += (after.fireRate > pre.fireRate) ? 1 : 0;
            speed += (after.speed > pre.speed) ? 1 : 0;
            luck += (after.luck > pre.luck) ? 1 : 0;
            shotspeed += (after.shotSpeed > pre.shotSpeed) ? 1 : 0;
            shotSize += (after.shotSize > pre.shotSize) ? 1 : 0;
            accuracy += (after.accuracy > pre.accuracy) ? 1 : 0;
            range += (after.range > pre.range) ? 1 : 0;




            nhealth += (after.getHealth() < pre.getHealth()) ? 1 : 0;
            nmaxHealth += (after.maxHealth < pre.maxHealth) ? 1 : 0;
            ndmg += (after.damage < pre.damage) ? 1 : 0;
            narmor += (after.armor < pre.armor) ? 1 : 0;
            nfirerate += (after.fireRate < pre.fireRate) ? 1 : 0;
            nspeed += (after.speed < pre.speed) ? 1 : 0;
            nluck += (after.luck < pre.luck) ? 1 : 0;
            nshotspeed += (after.shotSpeed < pre.shotSpeed) ? 1 : 0;
            nshotSize += (after.shotSize < pre.shotSize) ? 1 : 0;
            naccuracy += (after.accuracy < pre.accuracy) ? 1 : 0;
            nrange += (after.range < pre.range) ? 1 : 0;



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
        System.out.println("Total Items increasing shotSize: " + shotSize);
        System.out.println("Total Items increasing accuracy: " + accuracy);
        System.out.println("Total Items increasing range: " + range);
        System.out.println("Total Companions: " + companion);




        System.out.println("Total Items decreasing health: " + nhealth);
        System.out.println("Total Items decreasing maxHealth: " + nmaxHealth);
        System.out.println("Total Items decreasing damage: " + ndmg);
        System.out.println("Total Items decreasing armor: " + narmor);
        System.out.println("Total Items decreasing firerate: " + nfirerate);
        System.out.println("Total Items decreasing speed: " + nspeed);
        System.out.println("Total Items decreasing luck: " + nluck);
        System.out.println("Total Items decreasing shotspeed: " + nshotspeed);
        System.out.println("Total Items decreasing shotSize: " + nshotSize);
        System.out.println("Total Items decreasing accuracy: " + naccuracy);
        System.out.println("Total Items decreasing range: " + nrange);

    }



}
