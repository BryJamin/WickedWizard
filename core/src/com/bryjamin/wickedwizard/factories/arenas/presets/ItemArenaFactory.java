package com.bryjamin.wickedwizard.factories.arenas.presets;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.items.ItemFactory;

public class ItemArenaFactory extends ArenaShellFactory {

    ItemFactory itemFactory;

    public ItemArenaFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager, arenaSkin);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaSkin = arenaSkin; //new BrightWhiteSkin(atlas);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, this.arenaSkin);
    }

    //TODO actually make the rareItem rare.
    public Arena createItemRoom(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.DOOR)).buildArena();

        arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(5), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));
        arena.addEntity(decorFactory.wallBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));

            arena.addEntity(itemFactory.createItemAltarBag(com.bryjamin.wickedwizard.utils.Measure.units(10f),
                    com.bryjamin.wickedwizard.utils.Measure.units(35f), arenaSkin.getWallTint()));

        return arena;
    }


    public Arena createLeftItemRoom(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(35f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(25f)));
        arena.addEntity(itemFactory.createItemAltarBag(arena.getWidth() - com.bryjamin.wickedwizard.utils.Measure.units(27.5f),
                    com.bryjamin.wickedwizard.utils.Measure.units(35f), arenaSkin.getWallTint()));
        return arena;
    }


    public Arena createRightItemRoom(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(30f), com.bryjamin.wickedwizard.utils.Measure.units(25f)));
        arena.addEntity(itemFactory.createItemAltarBag(com.bryjamin.wickedwizard.utils.Measure.units(12.5f), com.bryjamin.wickedwizard.utils.Measure.units(35f), arenaSkin.getWallTint()));

        return arena;
    }



    public Arena createUpItemRoom(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL))
                .buildArena();

        arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(32.5f), com.bryjamin.wickedwizard.utils.Measure.units(60f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));

        arena.addEntity(itemFactory.createItemAltarBag(com.bryjamin.wickedwizard.utils.Measure.units(42.5f),
                com.bryjamin.wickedwizard.utils.Measure.units(10f), arenaSkin.getWallTint()));

        return arena;
    }


    public Arena createDownItemRoom(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.ITEM)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena();


        arena.addEntity(decorFactory.wallBag(com.bryjamin.wickedwizard.utils.Measure.units(20f), com.bryjamin.wickedwizard.utils.Measure.units(32.5f), com.bryjamin.wickedwizard.utils.Measure.units(60f), com.bryjamin.wickedwizard.utils.Measure.units(5f)));

        arena.addEntity(decorFactory.decorativeBlock(com.bryjamin.wickedwizard.utils.Measure.units(25f), com.bryjamin.wickedwizard.utils.Measure.units(32.5f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, com.bryjamin.wickedwizard.utils.Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
        arena.addEntity(decorFactory.decorativeBlock(com.bryjamin.wickedwizard.utils.Measure.units(75f) - com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, com.bryjamin.wickedwizard.utils.Measure.units(32.5f), com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory.DECORATIVE_BEAM_WIDTH, com.bryjamin.wickedwizard.utils.Measure.units(30f), TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));

        arena.addEntity(itemFactory.createItemAltarBag(com.bryjamin.wickedwizard.utils.Measure.units(42.5f),
                com.bryjamin.wickedwizard.utils.Measure.units(37.5f), arenaSkin.getWallTint()));

        return arena;
    }




    public Arena createBossRushItemRoom(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL))
                .buildArena();

        arena.addEntity(itemFactory.createItemAltarBag(com.bryjamin.wickedwizard.utils.Measure.units(42.5f),
                com.bryjamin.wickedwizard.utils.Measure.units(10f), arenaSkin.getWallTint()));
        return arena;
    }





}
