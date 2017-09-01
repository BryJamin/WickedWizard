package com.bryjamin.wickedwizard.factories.arenas.bossrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;

/**
 * Created by Home on 03/07/2017.
 */

public class BossRoomAdoj extends AbstractFactory {


    private ArenaSkin arenaSkin;


    public BossRoomAdoj(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate adojArena() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL)).buildArena();

                arena.addEntity(new com.bryjamin.wickedwizard.factories.enemy.bosses.BossAdoj(assetManager).bossAdoj(com.bryjamin.wickedwizard.utils.Measure.units(70f), com.bryjamin.wickedwizard.utils.Measure.units(10f)));

                return arena;
            }
        };
    }


}

