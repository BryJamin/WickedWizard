package com.byrjamin.wickedwizard;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.levels.PresetGenerators;
import com.byrjamin.wickedwizard.factories.arenas.skins.LightGraySkin;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Home on 14/02/2017.
 */
public class JigsawGeneratorTest extends GameTest {


    @Test
    public void testGenerateJigsawLevel1() throws Exception {

        AssetManager assetManager = new AssetManager();

        assetManager.load("sprite.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("sprite.atlas", TextureAtlas.class);

        assertTrue(atlas != null);

        //TODO rooms generate and a boss/item always seems to be there however,
        //TODO and in situation where a room can't be placed there is no endless loop
        //TODO needs to look into using omni rooms if number of rooms is not the same as entered into /
        //TODO the generator.

        for(int i = 0; i < 20000; i++) {

            Random random = new Random();

            int numberOfRooms =  random.nextInt(100);

            System.out.println("Generating for a room size of " + numberOfRooms);

            //LevelItemSystem lis = new LevelItemSystem(new Random());

            JigsawGenerator jg = new PresetGenerators().level1Configuration(assetManager, new LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.generate();
            jg.cleanArenas();


            Array<Arena> arenas = jg.getStartingMap().getRoomArray();


/*            OrderedSet<DoorComponent> dcs = jg.createAvaliableDoorSet(arenas);

            System.out.println("Avaliable doors are + " + dcs.size);

            for(DoorComponent dc : dcs){
                System.out.println("Exit is " + dc.exit);
                System.out.println("Does it link to something? " + jg.findDoorWithinFoundRoom(dc, arenas));
            }*/

            boolean bossRoom = false;

            for (Arena a : arenas) {
/*                System.out.println(a.arenaType);
                for(MapCoords mc : a.cotainingCoords){
                    System.out.println(mc);
                }*/
                if (a.arenaType == Arena.ArenaType.BOSS) {
                    bossRoom = true;
                    break;
                }
            }

            assertTrue(bossRoom);


            boolean itemRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.ITEM) {
                    itemRoom = true;
                    break;
                }
            }

            assertTrue(itemRoom);

            boolean shopRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.SHOP) {
                    shopRoom = true;
                    break;
                }
            }

            assertTrue(shopRoom);

            System.out.println(i);

        }


    }


    @Test
    public void testGenerateJigsawLevel2() throws Exception {

        AssetManager assetManager = new AssetManager();

        assetManager.load("sprite.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("sprite.atlas", TextureAtlas.class);


        //TODO Check if numbers of rooms is equal or greater than the rooms inserted.

        for(int i = 0; i < 20000; i++) {

            Random random = new Random();

            int numberOfRooms = random.nextInt(100);

            System.out.println("Generating for a room size of " + numberOfRooms);

            JigsawGenerator jg = new PresetGenerators().level2Configuration(assetManager, new LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.generate();
            jg.cleanArenas();

            Array<Arena> arenas = jg.getStartingMap().getRoomArray();

            boolean bossRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.BOSS) {
                    bossRoom = true;
                    break;
                }
            }

            assertTrue(bossRoom);


            boolean itemRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.ITEM) {
                    itemRoom = true;
                    break;
                }
            }

            assertTrue(itemRoom);

            boolean shopRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.SHOP) {
                    shopRoom = true;
                    break;
                }
            }

            assertTrue(shopRoom);

            System.out.println(i);

        }


    }


    @Test
    public void testGenerateJigsawLevel3() throws Exception {

        AssetManager assetManager = new AssetManager();

        assetManager.load("sprite.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("sprite.atlas", TextureAtlas.class);


        //TODO Check if numbers of rooms is equal or greater than the rooms inserted.

        for(int i = 0; i < 20000; i++) {

            Random random = new Random();

            int numberOfRooms = random.nextInt(100);

            System.out.println("Generating for a room size of " + numberOfRooms);


            JigsawGenerator jg = new PresetGenerators().level3Configuration(assetManager, new LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.generate();
            jg.cleanArenas();


            Array<Arena> arenas = jg.getStartingMap().getRoomArray();

            boolean bossRoom = false;

            for (Arena a : arenas) {
/*                System.out.println(a.arenaType);
                for(MapCoords mc : a.cotainingCoords){
                    System.out.println(mc);
                }*/
                if (a.arenaType == Arena.ArenaType.BOSS) {
                    bossRoom = true;
                    break;
                }
            }

            assertTrue(bossRoom);


            boolean itemRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.ITEM) {
                    itemRoom = true;
                    break;
                }
            }

            assertTrue(itemRoom);

            boolean shopRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.SHOP) {
                    shopRoom = true;
                    break;
                }
            }

            assertTrue(shopRoom);

            System.out.println(i);

        }


    }


    @Test
    public void testGenerateJigsawLevel4() throws Exception {

        AssetManager assetManager = new AssetManager();

        assetManager.load("sprite.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("sprite.atlas", TextureAtlas.class);




        //TODO Check if numbers of rooms is equal or greater than the rooms inserted.

        for(int i = 0; i < 20000; i++) {

            Random random = new Random();

            int numberOfRooms = random.nextInt(95) + 5;

            JigsawGenerator jg = new PresetGenerators().level4Configuration(assetManager, new LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.setNoBattleRooms(numberOfRooms);
            jg.generate();
            jg.cleanArenas();


            Array<Arena> arenas = jg.getStartingMap().getRoomArray();


            System.out.println("Number of rooms is " + numberOfRooms);


            boolean bossRoom = false;

            for (Arena a : arenas) {
/*                System.out.println(a.arenaType);
                for(MapCoords mc : a.cotainingCoords){
                    System.out.println(mc);
                }*/
                if (a.arenaType == Arena.ArenaType.BOSS) {
                    bossRoom = true;
                    break;
                }
            }

            assertTrue(bossRoom);


            boolean itemRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.ITEM) {
                    itemRoom = true;
                    break;
                }
            }

            assertTrue(itemRoom);

            boolean shopRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.SHOP) {
                    shopRoom = true;
                    break;
                }
            }

            assertTrue(shopRoom);

            System.out.println(i);

        }


    }



    @Test
    public void testGenerateJigsawLevel5() throws Exception {

        AssetManager assetManager = new AssetManager();

        assetManager.load("sprite.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("sprite.atlas", TextureAtlas.class);




        //TODO Check if numbers of rooms is equal or greater than the rooms inserted.

        for(int i = 0; i < 20000; i++) {

            Random random = new Random();

            int numberOfRooms = random.nextInt(95) + 5;




            JigsawGenerator jg = new PresetGenerators().level5Configuration(assetManager, new LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.setNoBattleRooms(numberOfRooms);
            jg.generate();
            jg.cleanArenas();

            System.out.println("Number of rooms is " + numberOfRooms);

            Array<Arena> arenas = jg.getStartingMap().getRoomArray();

            boolean bossRoom = false;

            for (Arena a : arenas) {
/*                System.out.println(a.arenaType);
                for(MapCoords mc : a.cotainingCoords){
                    System.out.println(mc);
                }*/
                if (a.arenaType == Arena.ArenaType.BOSS) {
                    bossRoom = true;
                    break;
                }
            }

            assertTrue(bossRoom);


            boolean itemRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.ITEM) {
                    itemRoom = true;
                    break;
                }
            }

            assertTrue(itemRoom);

            boolean shopRoom = false;

            for (Arena a : arenas) {
                if (a.arenaType == Arena.ArenaType.SHOP) {
                    shopRoom = true;
                    break;
                }
            }

            assertTrue(shopRoom);

            System.out.println(i);

        }


    }


}
