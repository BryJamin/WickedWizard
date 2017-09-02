package com.bryjamin.wickedwizard.factories.arenas.bossrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 08/07/2017.
 */

public class BossRoomAmalgama extends com.bryjamin.wickedwizard.factories.AbstractFactory {

    private com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin arenaSkin;


    public BossRoomAmalgama(AssetManager assetManager, com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate amalgamaArena() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 3, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 4, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 5, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL)).buildArena();

                arena.addEntity(new com.bryjamin.wickedwizard.factories.enemy.bosses.BossAmalgama(assetManager).amalgama(-Measure.units(80f), 0));


                return arena;
            }
        };
    }


}
