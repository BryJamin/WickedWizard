package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.archive.maps.MapCoords;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.factories.items.DamageUp;
import com.byrjamin.wickedwizard.factories.items.FireRateUp;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.PlusOne;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 11/04/2017.
 */

public class ItemRoomFactory extends RoomFactory {


    public static Arena createItemRoom(){
        return createItemRoom(new MapCoords(0,0));
    }

    public static Arena createItemRoom(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds, Arena.RoomType.ITEM);


        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena.addEntity(BackgroundFactory.backgroundBags(0,0,
                SECTION_WIDTH,
                SECTION_HEIGHT,
                Measure.units(15),
                PlayScreen.atlas.findRegions("backgrounds/wall")));

        //LEFT WALL
        arena.addEntity(EntityFactory.wallBag(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.left));

        //RIGHT WALL
        arena.addEntity(EntityFactory.wallBag(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag(WIDTH - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.right));

        //CEILING
        arena.addEntity(EntityFactory.wallBag(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH));

        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));


        Item[] items = {new PlusOne(), new DamageUp(), new FireRateUp()};

        Random random = new Random();


        for(ComponentBag b : ItemFactory.createItemAltarBag(arena.getWidth() / 2,
                arena.getHeight() / 2, items[random.nextInt(items.length)])) {
            arena.addEntity(b);
        }
        return arena;
    }


    public static Arena createItemTestRoom(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

        Arena arena = new Arena(containingCorrds, Arena.RoomType.ITEM);


        arena.setWidth(SECTION_WIDTH * 2);
        arena.setHeight(SECTION_HEIGHT);

        arena.addEntity(BackgroundFactory.backgroundBags(0,0,
                SECTION_WIDTH * 2,
                SECTION_HEIGHT,
                Measure.units(15),
                PlayScreen.atlas.findRegions("backgrounds/wall")));

        //LEFT WALL
        arena.addEntity(EntityFactory.wallBag(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(EntityFactory.doorBag(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.left));

        //RIGHT WALL
        arena.addEntity(EntityFactory.wallBag(arena.getWidth() - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, arena.getHeight()));
        arena.addDoor(EntityFactory.doorBag(arena.getWidth() - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                DoorComponent.DIRECTION.right));

        //CEILING
        arena.addEntity(EntityFactory.wallBag(0, arena.getHeight() - WALLWIDTH, arena.getWidth(), WALLWIDTH));

        //GROUND
        arena.addEntity(EntityFactory.wallBag(0,  -WALLWIDTH, arena.getWidth(), WALLWIDTH * 3));



        Item[] items = {new PlusOne(), new DamageUp(), new FireRateUp()};

        Random random = new Random();

        for(ComponentBag b : ItemFactory.createItemAltarBag(arena.getWidth() / 4,
                arena.getHeight() / 2, items[random.nextInt(items.length)])) {
            arena.addEntity(b);
        }


        arena.addEntity(BlobFactory.dummyBlob(arena.getWidth() / 2, arena.getHeight() / 2));



        return arena;
    }






}
