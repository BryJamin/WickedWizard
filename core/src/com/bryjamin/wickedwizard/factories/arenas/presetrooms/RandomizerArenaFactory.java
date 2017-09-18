package com.bryjamin.wickedwizard.factories.arenas.presetrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.items.ItemFactory;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 02/09/2017.
 */

public class RandomizerArenaFactory extends AbstractFactory {


    private ArenaSkin arenaSkin;
    private ItemFactory itemFactory;

    public RandomizerArenaFactory(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
        this.itemFactory = new ItemFactory(assetManager);
    }



    public Arena createRandomizerRoom(MapCoords defaultCoords) {

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.RANDOMIZER)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.FULL))
                .buildArena();


        ComponentBag bag = itemFactory.randomizerShopOption(arena.getWidth() / 2, Measure.units(30f), 5, arenaSkin);
        arena.addEntity(bag);

        return arena;
    }






}
