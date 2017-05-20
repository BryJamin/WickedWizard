package com.byrjamin.wickedwizard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.systems.level.LevelItemSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.skins.FoundarySkin;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Home on 14/02/2017.
 */
public class JigsawGeneratorTest extends GameTest {


    @Test
    public void testGenerateJigsaw() throws Exception {

        AssetManager assetManager = new AssetManager();

        System.out.println(Gdx.files.getLocalStoragePath());
        System.out.println(Gdx.files.getExternalStoragePath());

        TextureAtlasLoader textureAtlasLoader = new TextureAtlasLoader(new LocalFileHandleResolver());

        assetManager.setLoader(TextureAtlas.class,textureAtlasLoader);
        assetManager.load(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);



        //TODO rooms generate and a boss/item always seems to be there however,
        //TODO and in situation where a room can't be placed there is no endless loop
        //TODO needs to look into using omni rooms if number of rooms is not the same as entered into /
        //TODO the generator.

        for(int i = 0; i < 5000; i++) {

            Random random = new Random();

            int numberOfRooms = random.nextInt(100);

            LevelItemSystem lis = new LevelItemSystem(new Random());

            JigsawGenerator jg = new JigsawGenerator(assetManager, new FoundarySkin(atlas), numberOfRooms,lis.getItemPool(), random);
            jg.generateTutorial = false;

            Array<Arena> arenas = jg.generate();

           // Array<com.byrjamin.wickedwizard.archive.maps.rooms.Room> rooms = mapJigsawGenerator.generateJigsaw();

           // System.out.println("Room size is " + rooms.size);

            //Assert.assertEquals(numberOfRooms + 4, arenas.size);

            boolean bossRoom = false;

            for (Arena a : arenas) {
                if (a.roomType == Arena.RoomType.BOSS) {
                    bossRoom = true;
                    break;
                }
            }
            System.out.println("Arena size is: " +arenas.size);
            System.out.println(i);
            assertTrue(bossRoom);


            boolean itemRoom = false;

            for (Arena a : arenas) {
                if (a.roomType == Arena.RoomType.ITEM) {
                    itemRoom = true;
                    break;
                }
            }

            assertTrue(itemRoom);

            //System.out.println(i);

        }


    }

}
