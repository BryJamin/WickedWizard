package com.bryjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAdoj;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.BlobFactory;
import com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by BB on 26/08/2017.
 */

public class Rank1ChallengeMaps extends AbstractFactory{

    private static final float ARENA_SPEEDRUN_TIMER = 35f;
    public static final float TUTORIAL_SPEEDRUN_TIMER = 20f;


    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin = com.bryjamin.wickedwizard.utils.enums.Level.ONE.getArenaSkin();

    private Random random;


    public Rank1ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.random = random;
    }






    public com.bryjamin.wickedwizard.factories.arenas.GameCreator perfectBlobba(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.ONE.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(2,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBiggaBlobba(assetManager, arenaSkin).biggaBlobbaArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));


        return gameCreator;


    }




    public com.bryjamin.wickedwizard.factories.arenas.GameCreator perfectAdoj(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.ONE.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(2,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomAdoj(assetManager, arenaSkin).adojArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));


        return gameCreator;

    }





    public com.bryjamin.wickedwizard.factories.arenas.GameCreator tutorialSpeedRun(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.ONE.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                e.deleteFromWorld();
            }
        }));
        startingArena.addEntity(new OnLoadFactory().challengeTimer(TUTORIAL_SPEEDRUN_TIMER));


        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(7,3));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                timeTrailRoomOneJumpTutorial(new MapCoords(1, 0)),
                timeTrailRoomTwoPlatformTutorial(new MapCoords(5,0)),
                timeTrailRoomThreeGrappleTutorial(new MapCoords(6,0)),
                timeTrialRoomFourEnemyTutorial(new MapCoords(6,3)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));


        return gameCreator;

    }


    public Arena timeTrailRoomOneJumpTutorial(MapCoords defaultCoords){

        Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL).addSection(
                new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 3, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL)).buildArena();

        arena.addEntity(decorFactory.wallBag(Measure.units(30f),  Measure.units(10f), Measure.units(40f), ArenaBuilder.WALLWIDTH * 2, arenaSkin));
        arena.addEntity(decorFactory.wallBag(Measure.units(100f),  Measure.units(10f), Measure.units(40f), ArenaBuilder.WALLWIDTH * 2, arenaSkin));
        float wall2PosX = Measure.units(290f);
        arena.addEntity(decorFactory.wallBag(wall2PosX,  Measure.units(10f), Measure.units(60f), ArenaBuilder.WALLWIDTH * 6, arenaSkin));
        return arena;

    }


    public Arena timeTrailRoomTwoPlatformTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE)).buildArena();

        //Blocker
        arena.addEntity(decorFactory.wallBag(Measure.units(40f), Measure.units(10f), Measure.units(20f), Measure.units(50f), arenaSkin));

        //Left platforms
        arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(30f), Measure.units(35f)));
        arena.addEntity(decorFactory.platform(Measure.units(5), Measure.units(55f), Measure.units(35f)));



        //Right platforms
        arena.addEntity(decorFactory.platform(Measure.units(60f), Measure.units(30f), Measure.units(35f)));
        arena.addEntity(decorFactory.platform(Measure.units(60f), Measure.units(55f), Measure.units(35f)));

        //Arrows
        for(int i = 0; i < 2; i ++)  arena.addEntity(decorFactory.chevronBag(arena.getWidth() - Measure.units(27.5f), Measure.units(17.5f + (i * 25f)), 180));

        //arena.addEntity(decorFactory.chevronBag(Measure.units(85f), Measure.units(22.5f), 0));
        //arena.addEntity(decorFactory.chevronBag(Measure.units(55f), Measure.units(22.5f), -90));
        for(int i = 0; i < 2; i ++)  arena.addEntity(decorFactory.chevronBag(Measure.units(17.5f), Measure.units(17.5f + (i * 25f)), 0));

        return arena;

    }



    public Arena timeTrailRoomThreeGrappleTutorial(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() +  2),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE)).buildArena();

        arena.addEntity(decorFactory.platform(0, arena.getHeight() - Measure.units(35), arena.getWidth()));

        for(int i = 0; i < 5; i ++) {
            arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(50 + (i * 30))));
        }

        return arena;

    }


    public Arena timeTrialRoomFourEnemyTutorial(MapCoords defaultCoords){



        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                .addSection(
                new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR)).buildArena();

        arena.addEntity(new BlobFactory(assetManager).blob(arena.getWidth() - Measure.units(12), Measure.units(30f),1, 0, 8, true, ColorResource.BLOB_GREEN));

        return arena;


    }





    public com.bryjamin.wickedwizard.factories.arenas.GameCreator arenaSpeedRun(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.ONE.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                e.deleteFromWorld();
            }
        }));


        Arena arena = arenaShellFactory.createSmallArenaNoGrapple(new MapCoords(1,0), Arena.ArenaType.TRAP);
        arena.addEntity(new OnLoadFactory().challengeTimer(ARENA_SPEEDRUN_TIMER));




        SpawnerFactory.SpawnerBuilder spawnerBuilder = new SpawnerFactory.SpawnerBuilder(assetManager, arenaSkin)
                .resetTime(0.5f)
                .life(5)
                .spawners(new SpawnerFactory.Spawner() {
                    @Override
                    public Bag<Component> spawnBag(float x, float y) {
                        return arenaEnemyPlacementFactory.bouncerFactory.smallBouncer(x,y, true);
                    }
                });

        arena.addWave(spawnerBuilder.build().spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2 + Measure.units(2.5f)));


        spawnerBuilder = new SpawnerFactory.SpawnerBuilder(assetManager, arenaSkin)
                .life(5)
                .spawners(new SpawnerFactory.Spawner() {
                    @Override
                    public Bag<Component> spawnBag(float x, float y) {
                        return arenaEnemyPlacementFactory.bouncerFactory.smallBouncer(x,y, false);
                    }
                });

        arena.addWave(spawnerBuilder.build().spawnerBag(arena.getWidth() / 2, arena.getHeight() / 2 + Measure.units(2.5f)));


        arena.addWave(arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() / 4, Measure.units(45f), false),
                arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() / 4 * 3, Measure.units(45f), true));

        arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 2, arena.getHeight() / 2));


        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(2, 0));

        ArenaMap arenaMap = new ArenaMap(startingArena,
                arena,
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));


        return gameCreator;

    }























}
