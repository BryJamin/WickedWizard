package com.byrjamin.wickedwizard.factories.arenas.bossrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.bosses.MrBoomyBoss;
import com.byrjamin.wickedwizard.utils.MapCoords;

import static com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT;
import static com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH;

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

                arena.addEntity(new MrBoomyBoss(assetManager).mrBoomy(arena.getWidth() / 2, arena.getHeight() / 2));

                return arena;
            }
        };
    }


}
