package com.byrjamin.wickedwizard.factories.arenas.presets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.factories.enemy.BlobFactory;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemAnger;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemSwiftShot;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.passives.health.Medicine;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

import static com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH;

public class ItemArenaFactory extends com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory {

    ItemFactory itemFactory;

    public ItemArenaFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaSkin = arenaSkin; //new BrightWhiteSkin(atlas);
        this.decorFactory = new DecorFactory(assetManager, this.arenaSkin);
    }

    //TODO actually make the rareItem rare.
    public Arena createItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addRoomType(Arena.RoomType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.DOOR)).buildArena();

        arena.addEntity(decorFactory.wallBag(Measure.units(5), Measure.units(30f), Measure.units(25f), Measure.units(5f)));
        arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(35f), Measure.units(30f), Measure.units(30f), Measure.units(5f)));

            arena.addEntity(itemFactory.createItemAltarBag(Measure.units(10f),
                    Measure.units(35f), arenaSkin.getWallTint()));

        return arena;
    }


    public Arena createLeftItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addRoomType(Arena.RoomType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(35f), Measure.units(10f), Measure.units(30f), Measure.units(25f)));
        arena.addEntity(itemFactory.createItemAltarBag(arena.getWidth() - Measure.units(27.5f),
                    Measure.units(35f), arenaSkin.getWallTint()));
        return arena;
    }


    public Arena createRightItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addRoomType(Arena.RoomType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(10f), Measure.units(30f), Measure.units(25f)));
        arena.addEntity(itemFactory.createItemAltarBag(Measure.units(12.5f), Measure.units(35f), arenaSkin.getWallTint()));

        return arena;
    }



    public Arena createUpItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addRoomType(Arena.RoomType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL))
                .buildArena();

        arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(32.5f), Measure.units(60f), Measure.units(5f)));

        arena.addEntity(itemFactory.createItemAltarBag(Measure.units(42.5f),
                Measure.units(10f), arenaSkin.getWallTint()));

        return arena;
    }


    public Arena createDownItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena();


        arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(32.5f), Measure.units(60f), Measure.units(5f)));

        arena.addEntity(decorFactory.decorativeBlock(Measure.units(25f), Measure.units(32.5f), DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
        arena.addEntity(decorFactory.decorativeBlock(Measure.units(75f) - DECORATIVE_BEAM_WIDTH, Measure.units(32.5f), DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));

        arena.addEntity(itemFactory.createItemAltarBag(Measure.units(42.5f),
                Measure.units(37.5f), arenaSkin.getWallTint()));

        return arena;
    }




    public Arena createBossRushItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addRoomType(Arena.RoomType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL))
                .buildArena();

        arena.addEntity(itemFactory.createItemAltarBag(Measure.units(42.5f),
                Measure.units(10f), arenaSkin.getWallTint()));
        return arena;
    }





}
