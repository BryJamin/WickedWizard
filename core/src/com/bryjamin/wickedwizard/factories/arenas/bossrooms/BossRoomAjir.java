package com.bryjamin.wickedwizard.factories.arenas.bossrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.bosses.BossAjir;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 07/07/2017.
 */

public class BossRoomAjir extends AbstractFactory {

    private ArenaSkin arenaSkin;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;

    public BossRoomAjir(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
    }

    public ArenaCreate ajirArena() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL)).buildArena();


                arena.addEntity(decorFactory.wallBag(0, arena.getHeight(), arena.getWidth(), Measure.units(5f)));
                arena.addEntity(new BossAjir(assetManager).ajir(arena.getWidth() / 2, Measure.units(40f)));




                return arena;
            }
        };
    }


}
