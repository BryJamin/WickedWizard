package com.byrjamin.wickedwizard.factories.arenas.presets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.factories.items.passives.damage.Anger;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemSwiftShot;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.passives.health.Medicine;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

public class ItemArenaFactory extends com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory {

    ItemFactory itemFactory;

    public ItemArenaFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
        this.itemFactory = new ItemFactory(assetManager);
    }

    //TODO actually make the rareItem rare.
    public Arena createItemRoom(MapCoords defaultCoords, Item item, Item rareItem) {

        Arena arena = new Arena(Arena.RoomType.ITEM, arenaSkin, defaultCoords);

        arena.setWidth(SECTION_WIDTH);
        arena.setHeight(SECTION_HEIGHT);

        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.DOOR)).buildArena(arena);

        arena.addEntity(decorFactory.wallBag(Measure.units(5), Measure.units(30f), Measure.units(25f), Measure.units(5f)));
        arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(35f), Measure.units(30f), Measure.units(30f), Measure.units(5f)));

        arena.addEntity(decorFactory.lockWall(arena.getWidth() - Measure.units(35f), Measure.units(35f), Measure.units(5f), Measure.units(20f)));


        for(ComponentBag b : new ItemFactory(assetManager).createItemAltarBag(Measure.units(7.5f),
                Measure.units(40), item)) {
            arena.addEntity(b);
        }

        for(ComponentBag b : new ItemFactory(assetManager).createItemAltarBag(Measure.units(75f),
                Measure.units(40), rareItem)) {
            arena.addEntity(b);
        }

        return arena;
    }


    public Arena createItemTestRoom(MapCoords defaultCoords) {

        Array<MapCoords> containingCorrds = new Array<MapCoords>();
        containingCorrds.add(defaultCoords);
        containingCorrds.add(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

        Arena arena = new Arena(Arena.RoomType.ITEM, arenaSkin, containingCorrds.toArray());


        arena.setWidth(SECTION_WIDTH * 2);
        arena.setHeight(SECTION_HEIGHT);


        arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena(arena);


        Item[] items = {new Medicine(), new Anger(), new ItemSwiftShot()};

        Random random = new Random();

        for(ComponentBag b : itemFactory.createItemAltarBag(arena.getWidth() / 4,
                arena.getHeight() / 2, items[random.nextInt(items.length)])) {
            arena.addEntity(b);
        }


        arena.addEntity(new BlobFactory(assetManager).dummyBlob(arena.getWidth() / 2, arena.getHeight() / 2));



        return arena;
    }






}
