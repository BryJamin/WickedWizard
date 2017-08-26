package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.BombFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBoomyMap;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAjir;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.ReuseableRooms;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by BB on 24/08/2017.
 */

public class Rank3ChallengeMaps extends AbstractFactory {


    private static final float ARENA_SPEEDRUN_TIMER = 80f;
    public static final float TIME_TRIAL_SPEEDRUN_TIMER = 27.5f;


    private ArenaShellFactory arenaShellFactory;
    private DecorFactory decorFactory;
    private ItemFactory itemFactory;
    private BombFactory bombFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin = Level.THREE.getArenaSkin();

    private Random random;


    public Rank3ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.bombFactory = new BombFactory(assetManager);

        this.random = random;
    }


    public GameCreator perfectBoomy(String id){

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.THREE.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(2,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomBoomyMap(assetManager, arenaSkin).boomyArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }




    public GameCreator perfectAjir(String id){

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.THREE.getMusic()).createArena(new MapCoords(0, 0));

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(2,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomAjir(assetManager, arenaSkin).ajirArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }



    public GameCreator rank3ArenaRace(String id){

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.TWO.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                world.getSystem(LuckSystem.class).turnOffEnemyDrops();
                e.deleteFromWorld();
            }
        }));

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0,1));
        arena.addEntity(new OnLoadFactory().challengeTimer(ARENA_SPEEDRUN_TIMER));


        arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, Measure.units(45f)),
                bombFactory.fadeInSeaMine(arena.getWidth() / 4 * 3, arena.getHeight() / 2, true, true),
                bombFactory.fadeInSeaMine(arena.getWidth() / 4, arena.getHeight() / 2, false, true));

        arena.addWave(arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 4, Measure.units(45f)),
                arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 4 * 3, Measure.units(45f)));

        arena.addWave(arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() / 4, Measure.units(35f)),
                arenaEnemyPlacementFactory.spawnSilverHead(arena.getWidth() / 4 * 3, Measure.units(35f)),
                arenaEnemyPlacementFactory.spawnFixedFlyByBombSentry(arena.getWidth() / 2, Measure.units(45f)),
                bombFactory.fadeInSeaMine(arena.getWidth() / 4 * 3, arena.getHeight() / 2, true, true),
                bombFactory.fadeInSeaMine(arena.getWidth() / 4, arena.getHeight() / 2, false, true));

        arena.addWave(arenaEnemyPlacementFactory.spawnMovingFlyByBombSentry(arena.getWidth() / 4, Measure.units(35f), true),
                arenaEnemyPlacementFactory.spawnMovingTriSentry(arena.getWidth() / 4 * 3, Measure.units(45f), false));

        arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4, Measure.units(45f)),
                arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4 * 3, Measure.units(45f)),
                bombFactory.fadeInSeaMine(arena.getWidth() / 4 * 3, arena.getHeight() / 2, true, true),
                bombFactory.fadeInSeaMine(arena.getWidth() / 4, arena.getHeight() / 2, false, true));



        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(0, 2));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                arena,
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }












    public GameCreator timeTrial(String id){

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.TWO.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                world.getSystem(LuckSystem.class).turnOffEnemyDrops();
                e.deleteFromWorld();
            }
        }));

        Arena room1 = trialRoom1(new MapCoords(1, 0));
        room1.addEntity(new OnLoadFactory().challengeTimer(TIME_TRIAL_SPEEDRUN_TIMER));

        ArenaMap arenaMap = new ArenaMap(startingArena,
                room1,
                timeTrailRoom2(new MapCoords(5, 0)),
                trailRoom3(new MapCoords(6,0)),
                trailRoom4(new MapCoords(6, 3)),
                new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(7, 3))
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }


    public Arena trialRoom1(MapCoords defaultCoords){



        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.MANDATORYDOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL, //upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE ,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE ,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,//!upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 3, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE ,
                        ArenaBuilder.wall.MANDATORYDOOR,
                        ArenaBuilder.wall.FULL,//!upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE))
                .buildArena();


        float bottomWallWidth = Measure.units(45f);

        arena.addEntity(decorFactory.wallBag(Measure.units(0), Measure.units(-5f), arena.getWidth(), Measure.units(5)));

        //left
        arena.addEntity(decorFactory.wallBag(Measure.units(5), Measure.units(0), bottomWallWidth, Measure.units(10f)));
        arena.addEntity(decorFactory.wallBag(arena.getWidth() - bottomWallWidth - Measure.units(5f), Measure.units(0), bottomWallWidth, Measure.units(10f)));

        arena.addEntity(decorFactory.spikeWall(0, 0, arena.getWidth(), Measure.units(5f), 0));

        for(int i = 0; i < 5; i++) {
            arena.addEntity(decorFactory.grapplePointBag(Measure.units(100 + (i * 50)), Measure.units(35f)));
            arena.addEntity(decorFactory.fixedWallTurret(Measure.units(100 + (i * 50)) - Measure.units(2.5f), Measure.units(50f), 180, 1.5f, 0));
        }

        //Used for start direction of mine
        int mod  = random.nextBoolean() ? 0 : 1;

        for(int i = 0; i <= 7; i++) {
            arena.addEntity(arenaEnemyPlacementFactory.bombFactory.verticalSeaMine(Measure.units(20) + Measure.units(i * 50f), Measure.units(25f), i % 2 == mod));
        }


        return arena;

    }


    public Arena timeTrailRoom2(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.MANDATORYDOOR,
                        ArenaBuilder.wall.MANDATORYDOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .buildArena();

        float leftPosX = Measure.units(25f);

        arena.addEntity(decorFactory.wallBag(leftPosX, Measure.units(20f), Measure.units(20f), Measure.units(35f)));
        arena.addEntity(decorFactory.wallBag(Measure.units(55f), Measure.units(10f), Measure.units(20f), Measure.units(35f)));

        //arena.addEntity(decorFactory.grapplePointBag(Measure.units(50f), Measure.units(50f)));
        //arena.addEntity(decorFactory.grapplePointBag(Measure.units(15f), Measure.units(50f)));

        BombFactory bf = new BombFactory(assetManager);

        for(int i = 0; i < 3; i++) arena.addEntity(bf.mine(Measure.units(55f + (i * 7.5f)), Measure.units(45f), 0));

        for(int i = 0; i < 5; i++) arena.addEntity(bf.mine(Measure.units(45f), Measure.units(20f + (i * 7.5f)), -90));
        for(int i = 0; i < 5; i++) arena.addEntity(bf.mine(Measure.units(50f), Measure.units(10f + (i * 7.5f)), 90));

        for(int i = 0; i < 4; i++) arena.addEntity(bf.mine(leftPosX + Measure.units(i * 7.5f), Measure.units(10f), 0));

        return arena;
    }

    public Arena trailRoom3(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.MANDATORYDOOR))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.MANDATORYGRAPPLE,
                        ArenaBuilder.wall.NONE))
                .buildArena();

        boolean bool = true;

        BombFactory bf = new BombFactory(assetManager);

        for(int i = 0; i < 4; i++) {
            arena.addEntity(decorFactory.grapplePointBag( i % 2 == (bool ? 0 : 1) ? arena.getWidth() / 4 * 3 : arena.getWidth() / 4, Measure.units(45f + (i * 30))));
        }

        int zeroOrOne = random.nextBoolean() ? 0 : 1;

        for(int i = 0; i < 5; i++) {
            arena.addEntity(bf.horizontalSeaMine(Measure.units(45f), Measure.units(25f + (i * 30)), i % 2 == zeroOrOne));
        }

        return arena;

    }


    public Arena trailRoom4(MapCoords defaultCoords){

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
        ComponentBag bag = arenaEnemyPlacementFactory.turretFactory.fixedFlyByDoubleBombSentry(arena.getWidth() / 2, Measure.units(40f));
        arena.addEntity(bag);
        return arena;

    }















}
