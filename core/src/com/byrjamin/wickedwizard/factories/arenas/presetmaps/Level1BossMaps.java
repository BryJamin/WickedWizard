package com.byrjamin.wickedwizard.factories.arenas.presetmaps;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BiggaBlobbaMap;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BoomyMap;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAjir;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAmalgama;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomEnd;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWraithCowl;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.GiantKugelRoom;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAdoj;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.WandaRoom;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 29/05/2017.
 */

public class Level1BossMaps extends AbstractFactory {

    ArenaShellFactory arenaShellFactory;
    ChestFactory chestFactory;
    DecorFactory decorFactory;
    ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    TurretFactory turretFactory;

    ArenaSkin arenaSkin;

    public Level1BossMaps(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaShellFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        //this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin);
        this.decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public ArenaMap wandaMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0));
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));

        placedArenas.add(startingArena);
        placedArenas.add(new WandaRoom(assetManager, arenaSkin).wandaArena().createArena(new MapCoords(1, 0)));

        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(2, 0));
        exitArena.addEntity(decorFactory.levelPortal(exitArena.getWidth() / 2, exitArena.getHeight() / 2 + Measure.units(5f)));

        placedArenas.add(exitArena);

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }


    public ArenaMap giantKugelMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0));
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));

        placedArenas.add(startingArena);
        placedArenas.add(new GiantKugelRoom(assetManager, arenaSkin).giantKugelArena().createArena(new MapCoords(1, -1)));

        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(3, -1));
        exitArena.addEntity(decorFactory.levelPortal(exitArena.getWidth() / 2, exitArena.getHeight() / 2 + Measure.units(5f)));

        placedArenas.add(exitArena);

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }



    public ArenaMap blobbaMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0));
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));

        placedArenas.add(startingArena);
        placedArenas.add(new BiggaBlobbaMap(assetManager, arenaSkin).biggaBlobbaArena().createArena(new MapCoords(1, 0)));

        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(2, 0));
        exitArena.addEntity(decorFactory.levelPortal(exitArena.getWidth() / 2, exitArena.getHeight() / 2 + Measure.units(5f)));

        placedArenas.add(exitArena);

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }


    public ArenaMap boomyMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0));
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));

        placedArenas.add(startingArena);
        placedArenas.add(new BoomyMap(assetManager, arenaSkin).boomyArena().createArena(new MapCoords(1, 0)));

        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(2, 0));
        exitArena.addEntity(decorFactory.levelPortal(exitArena.getWidth() / 2, exitArena.getHeight() / 2 + Measure.units(5f)));

        placedArenas.add(exitArena);

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }

    public ArenaMap adojMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0));
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));

        placedArenas.add(startingArena);
        placedArenas.add(new BossRoomAdoj(assetManager, arenaSkin).adojArena().createArena(new MapCoords(1, 0)));

        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(2, 0));
        exitArena.addEntity(decorFactory.levelPortal(exitArena.getWidth() / 2, exitArena.getHeight() / 2 + Measure.units(5f)));

        placedArenas.add(exitArena);

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }



    public ArenaMap wraithMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0));
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));

        placedArenas.add(startingArena);
        placedArenas.add(new BossRoomWraithCowl(assetManager, arenaSkin).wraithcowlArena().createArena(new MapCoords(1, 0)));

        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(2, 0));
        exitArena.addEntity(decorFactory.levelPortal(exitArena.getWidth() / 2, exitArena.getHeight() / 2 + Measure.units(5f)));

        placedArenas.add(exitArena);

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }


    public ArenaMap ajirMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0));
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));

        placedArenas.add(startingArena);
        placedArenas.add(new BossRoomAjir(assetManager, arenaSkin).adojArena().createArena(new MapCoords(1, 0)));

        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(2, 0));
        exitArena.addEntity(decorFactory.levelPortal(exitArena.getWidth() / 2, exitArena.getHeight() / 2 + Measure.units(5f)));

        placedArenas.add(exitArena);

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }



    public ArenaMap amalgamaMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0));
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));

        placedArenas.add(startingArena);
        placedArenas.add(new BossRoomAmalgama(assetManager, arenaSkin).amalgamaArena().createArena(new MapCoords(1, 0)));

        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(7, 0));
        exitArena.addEntity(decorFactory.levelPortal(exitArena.getWidth() / 2, exitArena.getHeight() / 2 + Measure.units(5f)));

        placedArenas.add(exitArena);

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }




    public ArenaMap endMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0));
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));

        placedArenas.add(startingArena);


        placedArenas.add(new BossRoomEnd(assetManager, arenaSkin).endArena().createArena(new MapCoords(1, -20)));

        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(7, 0));
        exitArena.addEntity(decorFactory.levelPortal(exitArena.getWidth() / 2, exitArena.getHeight() / 2 + Measure.units(5f)));

        placedArenas.add(exitArena);

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }




}
