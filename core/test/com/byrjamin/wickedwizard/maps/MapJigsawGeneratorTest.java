package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.maps.rooms.BossRoom;
import com.byrjamin.wickedwizard.maps.rooms.ItemRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Home on 14/02/2017.
 */
public class MapJigsawGeneratorTest extends GameTest{

    @Test
    public void testGenerateJigsaw() throws Exception {


        PlayScreen.atlas = new TextureAtlas();

        for(int i = 0; i < 500; i++) {

            Random random = new Random();

            MapJigsawGenerator mapJigsawGenerator = new MapJigsawGenerator(3, random);

            Array<Room> rooms = mapJigsawGenerator.generateJigsaw();

            System.out.println("Room size is " + rooms.size);
            //Assert.assertTrue(rooms.size == 15);

            boolean bossRoom = false;

            for (Room r : rooms) {
                if (r instanceof BossRoom) {
                    bossRoom = true;
                    break;
                }
            }

            Assert.assertTrue(bossRoom);


            boolean itemRoom = false;

            for (Room r : rooms) {
                if (r instanceof ItemRoom) {
                    itemRoom = true;
                    break;
                }
            }

            Assert.assertTrue(itemRoom);

            System.out.println(i);

        }


    }
}