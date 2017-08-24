package com.byrjamin.wickedwizard.factories.arenas.presetmaps;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.assets.MenuStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.BossMapCreate;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.PresetGames;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BiggaBlobbaMap;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BoomyMap;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAjir;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAmalgama;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomEnd;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWraithCowl;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.GiantKugelRoom;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAdoj;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.WandaRoom;
import com.byrjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.items.Item;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemVitaminC;
import com.byrjamin.wickedwizard.screens.DataSave;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 29/05/2017.
 */

public class BossMaps extends AbstractFactory {

    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private PortalFactory portalFactory;
    private ItemFactory itemFactory;

    ArenaSkin arenaSkin;

    public BossMaps(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        //this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.portalFactory = new PortalFactory(assetManager);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public Array<ArenaMap> getBossMapsArray(){

        Array<ArenaMap> arenaMaps = new Array<ArenaMap>();
        arenaMaps.insert(0, blobbaMapCreate().createBossMap(new BossTeleporterComponent(), new ItemVitaminC()));
        arenaMaps.insert(1, adojMapCreate().createBossMap(new BossTeleporterComponent(), new ItemVitaminC()));
        arenaMaps.insert(2, giantSpinnerMapCreate().createBossMap(new BossTeleporterComponent(), new ItemVitaminC()));
        arenaMaps.insert(3, wandaMapCreate().createBossMap(new BossTeleporterComponent(), new ItemVitaminC()));
        arenaMaps.insert(4, boomyMapCreate().createBossMap(new BossTeleporterComponent(), new ItemVitaminC()));
        arenaMaps.insert(5, ajirMapCreate().createBossMap(new BossTeleporterComponent(), new ItemVitaminC()));
        arenaMaps.insert(6, wraithMapCreate().createBossMap(new BossTeleporterComponent(), new ItemVitaminC()));
        arenaMaps.insert(7, amalgamaMapCreate().createBossMap(new BossTeleporterComponent(), new ItemVitaminC()));
        arenaMaps.insert(8, endMapCreate().createBossMap(new BossTeleporterComponent(), new ItemVitaminC()));
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

        Arena bossRoom = new ArenaShellFactory(assetManager, arenaSkin).createOmniArenaHiddenGrapple(mapCoords);
        bossRoom.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(25f), Measure.units(5f)));
        bossRoom.roomType = Arena.RoomType.BOSS;
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

        Arena startingArena = arenaShellFactory.createSmallArena(mapCoords, false, true, false, false);
        startingArena.addEntity(portalFactory.mapPortal(startingArena.getWidth() / 2, startingArena.getHeight() / 2 + Measure.units(5f), btc));
        return startingArena;
    }


    /**
     * Creates the Exit Arena that leads to the next level of the game
     * This arena also holds an item
     * @param mapCoords - Map co-ordinates of the Arena
     * @return - Returns the arena
     */
    public Arena bossMapExitArena(MapCoords mapCoords, Item item){
        Arena exitArena = arenaShellFactory.createOmniArenaHiddenGrapple(mapCoords);
        exitArena.addEntity(decorFactory.wallBag(Measure.units(5f), Measure.units(30f), Measure.units(25f), Measure.units(5f)));


        exitArena.addEntity(portalFactory.levelPortal(Measure.units(80f), Measure.units(32.5f)));
        exitArena.addEntity(itemFactory.createItemAltarBag(Measure.units(10f), Measure.units(35f), item, arenaSkin.getWallTint()));
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


                    if (!DataSave.isDataAvailable(id)) {
                        DataSave.saveChallengeData(id);
                        world.getSystem(MessageBannerSystem.class).createLevelBanner(MenuStrings.NEW_TRAILS);
                    }
                }
                e.deleteFromWorld();
            }
        }));


        return exitArena;
    }





    public BossMapCreate blobbaMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        item,
                        new BiggaBlobbaMap(assetManager, arenaSkin).biggaBlobbaArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate adojMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        item,
                        new BossRoomAdoj(assetManager, arenaSkin).adojArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate giantSpinnerMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(3, -1),
                        btc,
                        item,
                        new GiantKugelRoom(assetManager, arenaSkin).giantKugelArena().createArena(new MapCoords(1, -1)));
            }
        };
    }

    public BossMapCreate wandaMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        item,
                        new WandaRoom(assetManager, arenaSkin).wandaArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate boomyMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        item,
                        new BoomyMap(assetManager, arenaSkin).boomyArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate ajirMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        item,
                        new BossRoomAjir(assetManager, arenaSkin).adojArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate wraithMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        item,
                        new BossRoomWraithCowl(assetManager, arenaSkin).wraithcowlArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate amalgamaMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(7, 0),
                        btc,
                        item,
                        new BossRoomAmalgama(assetManager, arenaSkin).amalgamaArena().createArena(new MapCoords(1, 0)));
            }
        };
    }

    public BossMapCreate endMapCreate(){
        return new BossMapCreate() {
            @Override
            public ArenaMap createBossMap(BossTeleporterComponent btc, Item item) {
                return createABossMap(
                        new MapCoords(0, 0),
                        new MapCoords(2, 0),
                        btc,
                        item,
                        new BossRoomEnd(assetManager, arenaSkin).endArena().createArena(new MapCoords(1, -20)));
            }
        };
    }


    public ArenaMap createABossMap(MapCoords entryArenaCoords, MapCoords exitArenaCoords, BossTeleporterComponent btc, Item item, Arena bossArena){

        Array<Arena> placedArenas = new Array<Arena>();

        Arena startingArena = bossMapStartingArena(entryArenaCoords, btc);

        placedArenas.add(startingArena);
        placedArenas.add(bossArena);
        placedArenas.add(bossMapExitArena(exitArenaCoords, item));

        return new ArenaMap(startingArena, placedArenas, new OrderedSet<Arena>(), new OrderedSet<Arena>());

    }




}
