package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGen;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 20/06/2017.
 */

public class Level4Rooms extends AbstractFactory {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private TurretFactory turretFactory;
    private Random random;
    private ArenaSkin arenaSkin;

    public Level4Rooms(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.random = random;
        this.arenaSkin = arenaSkin;
    }


    public Array<ArenaGen> getLevel4RoomArray() {
        Array<ArenaGen> ag = new Array<ArenaGen>();
        ag.add(room1LaserBouncers());
        return ag;
    }



    public ArenaGen room1LaserBouncers() {
        return new ArenaGen() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 4, Measure.units(45f)));
                arena.addEntity(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 4 * 3, Measure.units(45f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


}
