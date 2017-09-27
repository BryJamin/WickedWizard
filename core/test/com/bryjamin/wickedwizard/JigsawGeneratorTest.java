package com.bryjamin.wickedwizard;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGenerator;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * Created by Home on 14/02/2017.
 */
public class JigsawGeneratorTest extends GameTest {

    private static final float repeats = 20000;


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

        for(int i = 0; i < repeats; i++) {

            Random random = new Random();

            int numberOfRooms =  random.nextInt(100);

            System.out.println("Generating for a room size of " + numberOfRooms);

            //LevelItemSystem lis = new LevelItemSystem(new Random());

            JigsawGenerator jg = new com.bryjamin.wickedwizard.factories.arenas.levels.PresetGenerators().level1Configuration(assetManager, new com.bryjamin.wickedwizard.factories.arenas.skins.LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.generate();
            jg.cleanArenas();


            Array<Arena> arenas = jg.getStartingMap().getRoomArray();

            assertSpecialRoomsExist(arenas);

            System.out.println(i);

            assertAllDoors(arenas);


        }


    }


    /**
     * Checks if number of doors for all arenas is greater than zero.
     * As a room with no doors generated with no exit, so it's doors were removed
     * @param arenas - Arenas that will be checked
     */
    public void assertAllDoors(Array<Arena> arenas){
        for(int i = 0; i < arenas.size; i++) {
            assertTrue(arenas.get(i).getDoors().size > 0);
        }

    }


    @Test
    public void testGenerateJigsawLevel2() throws Exception {

        AssetManager assetManager = new AssetManager();

        assetManager.load("sprite.atlas", TextureAtlas.class);

        assetManager.finishLoading();

        TextureAtlas atlas = assetManager.get("sprite.atlas", TextureAtlas.class);


        //TODO Check if numbers of rooms is equal or greater than the rooms inserted.

        for(int i = 0; i < repeats; i++) {

            Random random = new Random();

            int numberOfRooms = random.nextInt(100);

            System.out.println("Generating for a room size of " + numberOfRooms);

            JigsawGenerator jg = new com.bryjamin.wickedwizard.factories.arenas.levels.PresetGenerators().level2Configuration(assetManager, new com.bryjamin.wickedwizard.factories.arenas.skins.LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.generate();
            jg.cleanArenas();

            Array<Arena> arenas = jg.getStartingMap().getRoomArray();

            assertSpecialRoomsExist(arenas);

            assertAllDoors(arenas);
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

        for(int i = 0; i < repeats; i++) {

            Random random = new Random();

            int numberOfRooms = random.nextInt(100);

            System.out.println("Generating for a room size of " + numberOfRooms);


            JigsawGenerator jg = new com.bryjamin.wickedwizard.factories.arenas.levels.PresetGenerators().level3Configuration(assetManager, new com.bryjamin.wickedwizard.factories.arenas.skins.LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.generate();
            jg.cleanArenas();


            Array<Arena> arenas = jg.getStartingMap().getRoomArray();

            assertSpecialRoomsExist(arenas);

            assertAllDoors(arenas);
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

        for(int i = 0; i < repeats; i++) {

            Random random = new Random();

            int numberOfRooms = random.nextInt(95) + 5;

            JigsawGenerator jg = new com.bryjamin.wickedwizard.factories.arenas.levels.PresetGenerators().level4Configuration(assetManager, new com.bryjamin.wickedwizard.factories.arenas.skins.LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.setNoBattleRooms(numberOfRooms);
            jg.generate();
            jg.cleanArenas();


            Array<Arena> arenas = jg.getStartingMap().getRoomArray();


            System.out.println("Number of rooms is " + numberOfRooms);

            assertSpecialRoomsExist(arenas);

            assertAllDoors(arenas);
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

        for(int i = 0; i < repeats; i++) {

            Random random = new Random();

            //Level 5 rooms are not friendly with smaller generation sizes

            int numberOfRooms = random.nextInt(94) + 6;


            JigsawGenerator jg = new com.bryjamin.wickedwizard.factories.arenas.levels.PresetGenerators().level5Configuration(assetManager, new com.bryjamin.wickedwizard.factories.arenas.skins.LightGraySkin(), random)
                    .noBattleRooms(numberOfRooms).build();
            jg.setNoBattleRooms(numberOfRooms);
            jg.generate();
            jg.cleanArenas();

            System.out.println("Number of rooms is " + numberOfRooms);

            Array<Arena> arenas = jg.getStartingMap().getRoomArray();

            assertSpecialRoomsExist(arenas);

            assertAllDoors(arenas);
            System.out.println(i);

        }


    }



    public void assertSpecialRoomsExist(Array<Arena> arenas){

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





    }


}
