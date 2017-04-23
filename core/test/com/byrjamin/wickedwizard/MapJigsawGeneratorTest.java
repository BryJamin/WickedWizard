package com.byrjamin.wickedwizard;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.archive.maps.MapJigsawGenerator;
import com.byrjamin.wickedwizard.archive.maps.rooms.BossRoom;
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

            Array<com.byrjamin.wickedwizard.archive.maps.rooms.Room> rooms = mapJigsawGenerator.generateJigsaw();

            System.out.println("Room size is " + rooms.size);
            //Assert.assertTrue(rooms.size == 15);

            boolean bossRoom = false;

            for (com.byrjamin.wickedwizard.archive.maps.rooms.Room r : rooms) {
                if (r instanceof BossRoom) {
                    bossRoom = true;
                    break;
                }
            }

            assertTrue(bossRoom);


            boolean itemRoom = false;

            for (com.byrjamin.wickedwizard.archive.maps.rooms.Room r : rooms) {
                if (r instanceof com.byrjamin.wickedwizard.archive.maps.rooms.ItemRoom) {
                    itemRoom = true;
                    break;
                }
            }

            assertTrue(itemRoom);

            //System.out.println(i);

        }


    }
}