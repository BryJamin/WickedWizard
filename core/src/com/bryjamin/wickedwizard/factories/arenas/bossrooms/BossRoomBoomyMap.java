package com.bryjamin.wickedwizard.factories.arenas.bossrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.MapCoords;

/**
 * Created by Home on 02/07/2017.
 */

public class BossRoomBoomyMap extends AbstractFactory{

    private ArenaSkin arenaSkin;

    public BossRoomBoomyMap(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public ArenaCreate boomyArena() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL)).buildArena();

                arena.addEntity(new com.bryjamin.wickedwizard.factories.enemy.bosses.MrBoomyBoss(assetManager).mrBoomy(arena.getWidth() / 2, arena.getHeight() / 2));

                return arena;
            }
        };
    }


}
