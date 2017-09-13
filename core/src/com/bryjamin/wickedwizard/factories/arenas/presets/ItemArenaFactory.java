package com.bryjamin.wickedwizard.factories.arenas.presets;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.items.ItemFactory;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.enums.ItemType;

public class ItemArenaFactory extends ArenaShellFactory {

    ItemFactory itemFactory;

    public ItemArenaFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaSkin = arenaSkin; //new BrightWhiteSkin(atlas);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, this.arenaSkin);
    }


    private ComponentBag altarPosition(float x, float y){
        return itemFactory.createItemAltarBag(x, y, arenaSkin.getWallTint(), ItemType.ITEM);
    }


    public Arena createLeftItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(arena.getWidth() - Measure.units(35f), Measure.units(10f), Measure.units(30f), Measure.units(25f)));
        arena.addEntity(altarPosition(arena.getWidth() - Measure.units(27.5f), Measure.units(35f)));
        return arena;
    }


    public Arena createRightItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(10f), Measure.units(30f), Measure.units(25f)));

        arena.addEntity(altarPosition(Measure.units(12.5f), Measure.units(35f)));

        return arena;
    }



    public Arena createUpItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL))
                .buildArena();

        arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(32.5f), Measure.units(60f), Measure.units(5f)));


        arena.addEntity(altarPosition(Measure.units(42.5f), Measure.units(10f)));

        return arena;
    }


    public Arena createDownItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena();


        arena.addEntity(decorFactory.wallBag(Measure.units(20f), Measure.units(32.5f), Measure.units(60f), Measure.units(5f)));

        arena.addEntity(decorFactory.decorativeBlock(Measure.units(25f), Measure.units(32.5f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
        arena.addEntity(decorFactory.decorativeBlock(Measure.units(75f) - com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(32.5f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));


        arena.addEntity(altarPosition(Measure.units(42.5f), Measure.units(37.5f)));

        return arena;
    }




    public Arena createBossRushItemRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL))
                .buildArena();

        arena.addEntity(altarPosition(Measure.units(42.5f), Measure.units(10f)));

        return arena;
    }





}
