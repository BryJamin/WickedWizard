package com.bryjamin.wickedwizard.factories.arenas.bossrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.bosses.BiggaBlobbaBoss;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 30/06/2017.
 */

public class BossRoomBiggaBlobba extends AbstractFactory {

    private ArenaSkin arenaSkin;

    public BossRoomBiggaBlobba(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public ArenaCreate biggaBlobbaArena() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL)).buildArena();

                arena.addEntity(new BiggaBlobbaBoss(assetManager).biggaBlobbaBag(Measure.units(65f), Measure.units(10f)));

                return arena;
            }
        };
    }




}
