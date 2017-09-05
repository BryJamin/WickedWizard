package com.bryjamin.wickedwizard.factories.arenas.bossrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.MapCoords;

/**
 * Created by Home on 06/07/2017.
 */

public class BossRoomWraithCowl extends com.bryjamin.wickedwizard.factories.AbstractFactory {

    private ArenaSkin arenaSkin;

    public BossRoomWraithCowl(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate wraithcowlArena() {
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.FULL,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR)).buildArena();

                arena.addEntity(new com.bryjamin.wickedwizard.factories.enemy.bosses.BossWraithCowl(assetManager).wraithCowl(arena.getWidth() / 2, arena.getHeight() / 2));

                return arena;
            }
        };
    }
    


}
