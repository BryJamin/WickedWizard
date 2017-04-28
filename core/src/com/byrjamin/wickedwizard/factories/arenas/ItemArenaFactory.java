package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.factories.items.passives.DamageUp;
import com.byrjamin.wickedwizard.factories.items.passives.FireRateUp;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.passives.PlusOne;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

public class ItemArenaFactory extends ArenaShellFactory {

    ItemFactory itemFactory;

    public ItemArenaFactory(AssetManager assetManager) {
        super(assetManager);
        this.itemFactory = new ItemFactory(assetManager);
    }

    public Arena createItemRoom(){
        return createItemRoom(new MapCoords(0,0));
    }

    public Arena createItemRoom(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);

        Arena arena = new Arena(containingCorrds, Arena.RoomType.ITEM);


        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena.addEntity(BackgroundFactory.backgroundBags(0,0,
                SECTION_WIDTH,
                SECTION_HEIGHT,
                Measure.units(15),
                atlas.findRegions("backgrounds/wall")));

        //LEFT WALL
        arena.addEntity(entityfactory.wallBag(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(entityfactory.doorBag(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.left));

        //RIGHT WALL
        arena.addEntity(entityfactory.wallBag(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(entityfactory.doorBag(WIDTH - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.right));

        //CEILING
        arena.addEntity(entityfactory.wallBag(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH));

        //GROUND
        arena.addEntity(entityfactory.wallBag(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3));


        Item[] items = {new PlusOne(), new DamageUp(), new FireRateUp()};

        Random random = new Random();


        for(ComponentBag b : new ItemFactory(assetManager).createItemAltarBag(arena.getWidth() / 2,
                Measure.units(12), items[random.nextInt(items.length)])) {
            arena.addEntity(b);
        }
        return arena;
    }


    public Arena createItemTestRoom(MapCoords defaultCoords) {

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
                atlas.findRegions("backgrounds/wall")));

        //LEFT WALL
        arena.addEntity(entityfactory.wallBag(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT));
        arena.addDoor(entityfactory.doorBag(0, Measure.units(10),
                new MapCoords(defaultCoords.getX(), defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() - 1, defaultCoords.getY()),
                DoorComponent.DIRECTION.left));

        //RIGHT WALL
        arena.addEntity(entityfactory.wallBag(arena.getWidth() - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, arena.getHeight()));
        arena.addDoor(entityfactory.doorBag(arena.getWidth() - WALLWIDTH, Measure.units(10),
                new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                DoorComponent.DIRECTION.right));

        //CEILING
        arena.addEntity(entityfactory.wallBag(0, arena.getHeight() - WALLWIDTH, arena.getWidth(), WALLWIDTH));

        //GROUND
        arena.addEntity(entityfactory.wallBag(0,  -WALLWIDTH, arena.getWidth(), WALLWIDTH * 3));



        Item[] items = {new PlusOne(), new DamageUp(), new FireRateUp()};

        Random random = new Random();

        for(ComponentBag b : itemFactory.createItemAltarBag(arena.getWidth() / 4,
                arena.getHeight() / 2, items[random.nextInt(items.length)])) {
            arena.addEntity(b);
        }


        arena.addEntity(new BlobFactory(assetManager).dummyBlob(arena.getWidth() / 2, arena.getHeight() / 2));



        return arena;
    }






}
