package com.bryjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomGiantKugelRoom;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;

import java.util.Random;

/**
 * Created by BB on 24/08/2017.
 */

public class Rank2ChallengeMaps extends AbstractFactory {


    private static final float ARENA_SPEEDRUN_TIMER = 50f;
    public static final float TIME_TRIAL_SPEEDRUN_TIMER = 33f;


    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private com.bryjamin.wickedwizard.factories.items.ItemFactory itemFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin = com.bryjamin.wickedwizard.utils.enums.Level.TWO.getArenaSkin();

    private Random random;


    public Rank2ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.itemFactory = new com.bryjamin.wickedwizard.factories.items.ItemFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);

        this.random = random;
    }


    public com.bryjamin.wickedwizard.factories.arenas.GameCreator perfectWanda(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.TWO.getMusic()).createArena(new com.bryjamin.wickedwizard.utils.MapCoords());

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).crit = 0;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new com.bryjamin.wickedwizard.utils.MapCoords(2,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWanda(assetManager, arenaSkin).wandaArena().createArena(new com.bryjamin.wickedwizard.utils.MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }




    public com.bryjamin.wickedwizard.factories.arenas.GameCreator perfectKugel(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.TWO.getMusic()).createArena(new com.bryjamin.wickedwizard.utils.MapCoords());

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).crit = 0;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new com.bryjamin.wickedwizard.utils.MapCoords(3,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomGiantKugelRoom(assetManager, arenaSkin).giantKugelArena().createArena(new com.bryjamin.wickedwizard.utils.MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }



    public com.bryjamin.wickedwizard.factories.arenas.GameCreator rank2ArenaRace(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.TWO.getMusic()).createArena(new com.bryjamin.wickedwizard.utils.MapCoords());

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).crit = 0;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.LuckSystem.class).turnOffEnemyDrops();
                e.deleteFromWorld();
            }
        }));

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new com.bryjamin.wickedwizard.utils.MapCoords(0,1), Arena.ArenaType.TRAP);
        arena.addEntity(new com.bryjamin.wickedwizard.factories.arenas.decor.OnLoadFactory().challengeTimer(ARENA_SPEEDRUN_TIMER));
        arena.addEntity(decorFactory.spikeWall(com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(50f), 270));
        arena.addEntity(decorFactory.spikeWall(com.bryjamin.wickedwizard.utils.Measure.units(90f), com.bryjamin.wickedwizard.utils.Measure.units(10f), com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(50f), 90));

        arena.addWave(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, arena.getHeight() / 2, true));
        arena.addWave(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, arena.getHeight() / 2, true, true));

        arena.addWave(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, arena.getHeight() / 2, true),
                arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, com.bryjamin.wickedwizard.utils.Measure.units(35f), true),
                arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4, com.bryjamin.wickedwizard.utils.Measure.units(35f), false));

        arena.addWave(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight()),
        arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth() / 2, arena.getHeight()),
        arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight()),
        arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight() / 2),
        arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight() / 2));

        arena.addWave(arenaEnemyPlacementFactory.spawnLaserKugel(arena.getWidth() / 2, arena.getHeight() / 2, false));

        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new com.bryjamin.wickedwizard.utils.MapCoords(0, 2));
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









    public com.bryjamin.wickedwizard.factories.arenas.GameCreator rank2TimeTrail(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.TWO.getMusic()).createArena(new com.bryjamin.wickedwizard.utils.MapCoords());

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).health = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.StatComponent.class).crit = 0;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.LuckSystem.class).turnOffEnemyDrops();
                e.deleteFromWorld();
            }
        }));

        Arena room1 = rank2TimeTrailRoom1(new com.bryjamin.wickedwizard.utils.MapCoords(1, 0));
        room1.addEntity(new com.bryjamin.wickedwizard.factories.arenas.decor.OnLoadFactory().challengeTimer(TIME_TRIAL_SPEEDRUN_TIMER));

        ArenaMap arenaMap = new ArenaMap(startingArena,
                room1,
                rank2TimeTrailRoom2GoatWizard(new com.bryjamin.wickedwizard.utils.MapCoords(1, 3)),
                rank2TimeTrailRoom3(new com.bryjamin.wickedwizard.utils.MapCoords(2, 3)),
                rank2TimeTrailRoom4(new com.bryjamin.wickedwizard.utils.MapCoords(6, 3)),
                new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new com.bryjamin.wickedwizard.utils.MapCoords(7, 3))
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }
    public Arena rank2TimeTrailRoom1(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){


        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 2),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.NONE)).buildArena();


        float oddPosx = arena.getWidth() / 4;
        float evenPosX = arena.getWidth() / 4 * 3;


        arena.addEntity(decorFactory.grapplePointBag(oddPosx, com.bryjamin.wickedwizard.utils.Measure.units(45f)));
        arena.addEntity(decorFactory.grapplePointBag(evenPosX, com.bryjamin.wickedwizard.utils.Measure.units(75)));
        arena.addEntity(decorFactory.grapplePointBag(oddPosx, com.bryjamin.wickedwizard.utils.Measure.units(105f)));
        arena.addEntity(decorFactory.grapplePointBag(evenPosX, com.bryjamin.wickedwizard.utils.Measure.units(135f)));

        return arena;
    }

    public Arena rank2TimeTrailRoom2GoatWizard(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
        com.bryjamin.wickedwizard.utils.ComponentBag bag = arenaEnemyPlacementFactory.goatWizardFactory.goatWizard(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(40f), false, false);
        com.bryjamin.wickedwizard.utils.BagSearch.removeObjectOfTypeClass(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class, bag);
        arena.addEntity(bag);
        return arena;

    }


    public Arena rank2TimeTrailRoom3(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){



        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 3, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .buildArena();


        float firstSpikeBlockX = com.bryjamin.wickedwizard.utils.Measure.units(40f);
        float firstSpikeWidth = com.bryjamin.wickedwizard.utils.Measure.units(20f);
        float firstSpikeBlockHeight = com.bryjamin.wickedwizard.utils.Measure.units(15f);

        arena.addEntity(decorFactory.wallBag(firstSpikeBlockX, com.bryjamin.wickedwizard.utils.Measure.units(5f), firstSpikeWidth, firstSpikeBlockHeight))
                .addEntity(decorFactory.spikeWall(firstSpikeBlockX, firstSpikeBlockHeight + com.bryjamin.wickedwizard.utils.Measure.units(5f), firstSpikeWidth, com.bryjamin.wickedwizard.utils.Measure.units(5f), 0));


        float secondSpikeBlockX = com.bryjamin.wickedwizard.utils.Measure.units(60f);
        float secondSpikeWidth = com.bryjamin.wickedwizard.utils.Measure.units(20f);
        float secondSpikeBlockHeight = com.bryjamin.wickedwizard.utils.Measure.units(25f);

        arena.addEntity(decorFactory.wallBag(secondSpikeBlockX, com.bryjamin.wickedwizard.utils.Measure.units(5f), secondSpikeWidth, secondSpikeBlockHeight))
                .addEntity(decorFactory.spikeWall(secondSpikeBlockX, secondSpikeBlockHeight + com.bryjamin.wickedwizard.utils.Measure.units(5f), secondSpikeWidth, com.bryjamin.wickedwizard.utils.Measure.units(5f), 0));


        float bigBlockX = com.bryjamin.wickedwizard.utils.Measure.units(80f);
        float bigBlockWidth = com.bryjamin.wickedwizard.utils.Measure.units(50f);
        float bigBlockHeight = com.bryjamin.wickedwizard.utils.Measure.units(35f);

        arena.addEntity(decorFactory.wallBag(bigBlockX, com.bryjamin.wickedwizard.utils.Measure.units(5f), bigBlockWidth, bigBlockHeight));


        float mediumBlockX = bigBlockX + bigBlockWidth;
        float mediumWidth = com.bryjamin.wickedwizard.utils.Measure.units(150f);
        float mediumHeight = com.bryjamin.wickedwizard.utils.Measure.units(20f);

        arena.addEntity(decorFactory.wallBag(mediumBlockX, com.bryjamin.wickedwizard.utils.Measure.units(5f), mediumWidth, mediumHeight))
                .addEntity(decorFactory.spikeWall(mediumBlockX, mediumHeight + com.bryjamin.wickedwizard.utils.Measure.units(5f), mediumWidth, com.bryjamin.wickedwizard.utils.Measure.units(5f), 0));



        float blockBlockX = mediumBlockX + mediumWidth + com.bryjamin.wickedwizard.utils.Measure.units(25f);
        float blockBlockY = com.bryjamin.wickedwizard.utils.Measure.units(25f);
        float blockBlockWidth = com.bryjamin.wickedwizard.utils.Measure.units(150f);
        float blockBlockHeight = com.bryjamin.wickedwizard.utils.Measure.units(30f);

        arena.addEntity(decorFactory.wallBag(blockBlockX, blockBlockY, blockBlockWidth, blockBlockHeight));

        arena.addEntity(decorFactory.spikeWall(blockBlockX - com.bryjamin.wickedwizard.utils.Measure.units(5f), blockBlockY, com.bryjamin.wickedwizard.utils.Measure.units(5f), blockBlockHeight, 90f));
        arena.addEntity(decorFactory.spikeWall(mediumBlockX + mediumWidth, com.bryjamin.wickedwizard.utils.Measure.units(5f), com.bryjamin.wickedwizard.utils.Measure.units(5f), mediumHeight, 270f));

        return arena;

    }


    public Arena rank2TimeTrailRoom4(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords){

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
        arena.addEntity(arenaEnemyPlacementFactory.kugelDuscheFactory.kugelDusche(arena.getWidth() / 2, com.bryjamin.wickedwizard.utils.Measure.units(27.5f), true));
        return arena;

    }















}
