package com.byrjamin.wickedwizard.factories.arenas.presetmaps;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.BossMapCreate;
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
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 29/05/2017.
 */

public class BossMaps extends AbstractFactory {

    ArenaShellFactory arenaShellFactory;
    ChestFactory chestFactory;
    DecorFactory decorFactory;
    ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    TurretFactory turretFactory;

    ArenaSkin arenaSkin;

    public BossMaps(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaShellFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        //this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin);
        this.decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public Arena bossTeleportArena(MapCoords mapCoords, BossTeleporterComponent btc){

        Arena bossRoom = new ArenaShellFactory(assetManager, arenaSkin).createOmniArenaHiddenGrapple(new MapCoords());
        bossRoom.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(25f), Measure.units(5f)));
        bossRoom.roomType = Arena.RoomType.BOSS;
        bossRoom.addEntity(decorFactory.mapPortal(Measure.units(17.5f), Measure.units(45f), btc));

        return bossRoom;
    }


    public Array<ArenaMap> getBossMapsArray(){

        Array<ArenaMap> arenaMaps = new Array<ArenaMap>();
        arenaMaps.insert(0, blobbaMap(new BossTeleporterComponent()));
        arenaMaps.insert(1, adojMap(new BossTeleporterComponent()));
        arenaMaps.insert(2, giantKugelMap(new BossTeleporterComponent()));
        arenaMaps.insert(3, wandaMap(new BossTeleporterComponent()));
        arenaMaps.insert(4, boomyMap(new BossTeleporterComponent()));
        arenaMaps.insert(5, ajirMap(new BossTeleporterComponent()));
        arenaMaps.insert(6, wraithMap(new BossTeleporterComponent()));
        arenaMaps.insert(7, amalgamaMap(new BossTeleporterComponent()));
        arenaMaps.insert(8, endMap(new BossTeleporterComponent()));
        return  arenaMaps;
    }


    public BossMapCreate blobbaMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return blobbaMap(btc);
            }
        };
    }

    public BossMapCreate adojMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return adojMap(btc);
            }
        };
    }

    public BossMapCreate giantSpinnerMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return giantKugelMap(btc);
            }
        };
    }

    public BossMapCreate wandaMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return wandaMap(btc);
            }
        };
    }

    public BossMapCreate boomyMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return boomyMap(btc);
            }
        };
    }

    public BossMapCreate ajirMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return ajirMap(btc);
            }
        };
    }

    public BossMapCreate wraithMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return wraithMap(btc);
            }
        };
    }

    public BossMapCreate amalgamaMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return amalgamaMap(btc);
            }
        };
    }

    public BossMapCreate endMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return endMap(btc);
            }
        };
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


    public Arena startingArena(MapCoords mapCoords, BossTeleporterComponent btc){
        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(mapCoords);
        startingArena.addEntity(decorFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f),
                btc));
        return startingArena;
    }


  //  public Arena finalRoom



    public ArenaMap giantKugelMap(BossTeleporterComponent btc) {

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = startingArena(new MapCoords(0,0), btc);
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
