package com.byrjamin.wickedwizard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
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

        PlayScreen.atlas = new TextureAtlas();

        System.out.println(Gdx.files.absolute("C:\\Users\\Home\\Documents\\University\\WickedWizard\\android\\assets\\sprite.atlas").exists());


        System.out.println(Gdx.files.getLocalStoragePath());


        AssetManager assetManager = new AssetManager();

        TextureAtlasLoader textureAtlasLoader = new TextureAtlasLoader(new LocalFileHandleResolver());

        assetManager.setLoader(TextureAtlas.class, textureAtlasLoader);
        assetManager.load("/assets/sprite.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        PlayScreen.atlas = assetManager.get("/assets/sprite.atlas", TextureAtlas.class);



        //TODO rooms generate and a boss/item always seems to be there however,
        //TODO and in situation where a room can't be placed there is no endless loop
        //TODO needs to look into using omni rooms if number of rooms is not the same as entered into /
        //TODO the generator. 

        for(int i = 0; i < 5000; i++) {

            Random random = new Random();

            int numberOfRooms = random.nextInt(100);

            JigsawGenerator jg = new JigsawGenerator(numberOfRooms, random);
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
