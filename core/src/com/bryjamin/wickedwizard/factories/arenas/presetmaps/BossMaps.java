package com.bryjamin.wickedwizard.factories.arenas.presetmaps;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.UnlockMessageSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.BossMapCreate;
import com.bryjamin.wickedwizard.factories.arenas.GameCreator;
import com.bryjamin.wickedwizard.factories.arenas.PresetGames;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossArenaEndBoss;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAdoj;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAjir;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAmalgama;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBiggaBlobba;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBoomyMap;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomGiantKugelRoom;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWanda;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWraithCowl;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.chests.ChestFactory;
import com.bryjamin.wickedwizard.factories.items.ItemFactory;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.enums.ItemType;

;

/**
 * Created by Home on 29/05/2017.
 */

public class BossMaps extends AbstractFactory {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private PortalFactory portalFactory;
    private ItemFactory itemFactory;

    ArenaSkin arenaSkin;

    public BossMaps(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        //this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.portalFactory = new PortalFactory(assetManager);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public Array<ArenaMap> getBossMapsArray(){

        Array<ArenaMap> arenaMaps = new Array<ArenaMap>();
        arenaMaps.insert(0, blobbaMapCreate().createBossMap(new BossTeleporterComponent()));
        arenaMaps.insert(1, adojMapCreate().createBossMap(new BossTeleporterComponent()));
        arenaMaps.insert(2, giantSpinnerMapCreate().createBossMap(new BossTeleporterComponent()));
        arenaMaps.insert(3, wandaMapCreate().createBossMap(new BossTeleporterComponent()));
        arenaMaps.insert(4, boomyMapCreate().createBossMap(new BossTeleporterComponent()));
        arenaMaps.insert(5, ajirMapCreate().createBossMap(new BossTeleporterComponent()));
        arenaMaps.insert(6, wraithMapCreate().createBossMap(new BossTeleporterComponent()));
        arenaMaps.insert(7, amalgamaMapCreate().createBossMap(new BossTeleporterComponent()));
        arenaMaps.insert(8, endMapCreate().createBossMap(new BossTeleporterComponent()));
        return  arenaMaps;
    }


    /**
     * This is the arena placed inside of the main level map, that is used to teleport to a boss map.
     * @param mapCoords - Mapcoords of the arena
     * @param btc - The boss teleporter component that holds the link to another map.
     * @return - Returns the arena
     */
    public final Arena bossTeleportArena(MapCoords mapCoords, BossTeleporterComponent btc){

        btc.offsetX = Measure.units(25f);

        Arena bossRoom = new ArenaShellFactory(assetManager, arenaSkin).createOmniArenaHiddenGrapple(mapCoords, Arena.ArenaType.BOSS);
        bossRoom.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(25f), Measure.units(5f)));
        bossRoom.addEntity(portalFactory.mapPortal(Measure.units(17.5f), Measure.units(45f), btc));

        return bossRoom;
    }


    /**
     * The starting room you are teleported to when you use a boss room teleporter
     * @param mapCoords - Map co-ordinates of the Arena
     * @param btc - The boss teleport component that holds the link to another map
     * @return - Returns the arena
     */
    public Arena bossMapStartingArena(MapCoords mapCoords, BossTeleporterComponent btc){

        btc.offsetY = -Measure.units(15f);

        Arena startingArena = arenaShellFactory.createOmniArenaHiddenGrapple(mapCoords, Arena.ArenaType.NORMAL);
        startingArena.addEntity(portalFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f), btc));
        return startingArena;
    }


    /**
     * Creates the Exit Arena that leads to the next level of the game
     * This arena also holds an item
     * @param mapCoords - Map co-ordinates of the Arena
     * @return - Returns the arena
     */
    public Arena bossMapExitArena(MapCoords mapCoords){
        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(mapCoords, Arena.ArenaType.NORMAL);
        exitArena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(25f), Measure.units(5f)));


        exitArena.addEntity(portalFactory.levelPortal(Measure.units(80f), Measure.units(32.5f)));
        exitArena.addEntity(itemFactory.createItemAltarBag(Measure.units(10f), Measure.units(35f), arenaSkin.getWallTint(), ItemType.BOSS));
        exitArena.addEntity(chestFactory.chestBag(Measure.units(45f), Measure.units(10f)));


        ComponentBag bag = exitArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {

                GameCreator gameCreator = world.getSystem(ChangeLevelSystem.class).getGameCreator();

                if(gameCreator.id.equals(PresetGames.DEFAULT_GAME_ID)) {

                    String id;

                    switch (gameCreator.position){
                        case 0:
                        default: id = ChallengesResource.LEVEL_1_COMPLETE; break;
                        case 1: id = ChallengesResource.LEVEL_2_COMPLETE; break;
                        case 2: id = ChallengesResource.LEVEL_3_COMPLETE; break;
                        case 3: id = ChallengesResource.LEVEL_4_COMPLETE; break;
                        case 4: id = ChallengesResource.LEVEL_5_COMPLETE; break;
                    }

                    world.getSystem(UnlockMessageSystem.class).createUnlockMessage(id);
                }
                e.deleteFromWorld();
            }
        }));


        return exitArena;
    }





    public BossMapCreate blobbaMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        new BossRoomBiggaBlobba(assetManager, arenaSkin).biggaBlobbaArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate adojMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        new BossRoomAdoj(assetManager, arenaSkin).adojArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate giantSpinnerMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(3, -1),
                        btc,
                        new BossRoomGiantKugelRoom(assetManager, arenaSkin).giantKugelArena().createArena(new MapCoords(1, -1)));
            }
        };
    }

    public BossMapCreate wandaMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        new BossRoomWanda(assetManager, arenaSkin).wandaArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate boomyMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        new BossRoomBoomyMap(assetManager, arenaSkin).boomyArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate ajirMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        new BossRoomAjir(assetManager, arenaSkin).ajirArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate wraithMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        new BossRoomWraithCowl(assetManager, arenaSkin).wraithcowlArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate amalgamaMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(6, -1),
                        btc,
                        new BossRoomAmalgama(assetManager, arenaSkin).amalgamaArena().createArena(new MapCoords(0, -1)));
            }
        };
    }

    public BossMapCreate endMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        new BossArenaEndBoss(assetManager, arenaSkin).endStartingRoom().createArena(new MapCoords(1, 0)));
            }
        };
    }


    public ArenaMap createABossMap(MapCoords entryArenaCoords, MapCoords exitArenaCoords, BossTeleporterComponent btc, Arena bossArena){

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = bossMapStartingArena(entryArenaCoords, btc);

        placedArenas.add(startingArena);
        placedArenas.add(bossArena);
        placedArenas.add(bossMapExitArena(exitArenaCoords));

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }




}
