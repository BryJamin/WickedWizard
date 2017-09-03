package com.bryjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAmalgama;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWraithCowl;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.weapons.enemy.LaserBeam;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by BB on 24/08/2017.
 */

public class Rank4ChallengeMaps extends AbstractFactory {


    private static final float ARENA_SPEEDRUN_TIMER = 45f;
    public static final float TIME_TRIAL_SPEEDRUN_TIMER = 52.5f;


    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory beamTurretFactory;
    private com.bryjamin.wickedwizard.factories.items.ItemFactory itemFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin = com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getArenaSkin();

    private Random random;


    public Rank4ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.itemFactory = new com.bryjamin.wickedwizard.factories.items.ItemFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.beamTurretFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory(assetManager, arenaSkin);
        this.random = random;
    }


    public com.bryjamin.wickedwizard.factories.arenas.GameCreator perfectWraith(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getMusic()).createArena(new MapCoords());

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(2,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomWraithCowl(assetManager, arenaSkin).wraithcowlArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }




    public com.bryjamin.wickedwizard.factories.arenas.GameCreator perfectAmalgama(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getMusic()).createArena(new MapCoords(0, 0));

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(7,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomAmalgama(assetManager, arenaSkin).amalgamaArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }




    public com.bryjamin.wickedwizard.factories.arenas.GameCreator rank4ArenaRace(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getMusic()).createArena(new MapCoords(0,0));

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.LuckSystem.class).turnOffEnemyDrops();
                e.deleteFromWorld();
            }
        }));

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0,-1), Arena.ArenaType.TRAP);
        arena.addEntity(new OnLoadFactory().challengeTimer(ARENA_SPEEDRUN_TIMER));


        arena.addWave(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(40f), true, true),
                arenaEnemyPlacementFactory.spawnAngryBlob(arena.getWidth() / 2, Measure.units(40f), false));


        arena.addWave(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, Measure.units(30f), false),
                arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, Measure.units(30f), true));


        arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4, Measure.units(35f)),
                arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 4 * 3, Measure.units(45f)));

        arena.addWave(arenaEnemyPlacementFactory.spawnLaserKugel(arena.getWidth() / 4, Measure.units(25f), true));


        arena.addWave(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, Measure.units(45f)));

        Arena endArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(-1, -1));
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









    public com.bryjamin.wickedwizard.factories.arenas.GameCreator timeTrial(String id){

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getMusic()).createArena(new MapCoords());

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.LuckSystem.class).turnOffEnemyDrops();
                e.deleteFromWorld();
            }
        }));

        Arena room1 = timeTrailSwitchLaserRoom(new MapCoords(1, 0));
        room1.addEntity(new OnLoadFactory().challengeTimer(TIME_TRIAL_SPEEDRUN_TIMER));

        ArenaMap arenaMap = new ArenaMap(startingArena,
                room1,
                trailRoomLargeRoomAndEnemies(new MapCoords(2,0)),
                trialRoomLaserGauntlet(new MapCoords(3, -1)),
                trailRoomPentaSentry(new MapCoords(7, 0)),
                new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(6, 0))
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }


    public Arena trialRoomLaserGauntlet(MapCoords defaultCoords){



        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.NORMAL)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.DOOR))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE ,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE ,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 3, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE ,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 4, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE ,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.GRAPPLE,
                        ArenaBuilder.wall.FULL))
                .buildArena();



        LaserBeam laserBeam = new LaserBeam.LaserBeamBuilder(assetManager)
                .chargingLaserHeight(Measure.units(15f))
                .chargingLaserWidth(Measure.units(1000f))
                .chargingLaserTime(1.5f)
                .activeLaserHeight(Measure.units(17.5f))
                .activeLaserWidth(Measure.units(1000f))
                .activeLaserTime(0.5f)
                .activeLaserDisperseTime(1f)
                .centerLaserUsingWidth(false)
                .layer(TextureRegionComponent.ENEMY_LAYER_FAR)
                .build();


        arena.addEntity(beamTurretFactory.timedLaserBeam(-Measure.units(20f), Measure.units(5), 6, 0, 3f, false,
                laserBeam));


        return arena;

    }


    public Arena trailRoomPentaSentry(MapCoords defaultCoords){

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords, Arena.ArenaType.TRAP);
        com.bryjamin.wickedwizard.utils.ComponentBag bag = arenaEnemyPlacementFactory.turretFactory.movingPentaSentry(arena.getWidth() / 2, Measure.units(40f), true, true);
        arena.addEntity(bag);
        return arena;

    }


    public Arena timeTrailSwitchLaserRoom(MapCoords defaultCoords){

        Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords, Arena.ArenaType.TRAP);
        arena.arenaType = Arena.ArenaType.TRAP;


        arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(45f), Measure.units(25f),2,
                new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(4f))
                        .speedInDegrees(2f)
                        .numberOfOrbitals(15)
                        .chargeTime(0.5f)
                        .angles(0, 45, 90)
                        .build()));


        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(5f), Measure.units(40f), -90));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(10f), Measure.units(40f), 90));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(5f), Measure.units(20f), -90));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(20f), Measure.units(50f), 180));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(25f), Measure.units(50f), 180));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(10f), Measure.units(20f), 90));

        return arena;
    }

    public Arena trailRoomLargeRoomAndEnemies(MapCoords defaultCoords){

        Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR,
                        ArenaBuilder.wall.NONE)).buildArena();


        arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(50f)));
        arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(95f)));

        arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(50f)));
        arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(95f)));

        arena.addEntity(decorFactory.platform(0, Measure.units(65f), arena.getWidth()));

        arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(90f), Measure.units(50f),4,
                new com.bryjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(15f))
                        .speedInDegrees(-0.225f)
                        .numberOfOrbitals(10)
                        .chargeTime(0.5f)
                        .disperseTime(1f)
                        .angles(90, 270)
                        .build()));


        arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedMultiSentry(arena.getWidth() / 2, Measure.units(35f)));
        arena.addEntity(arenaEnemyPlacementFactory.turretFactory.fixedFlyByBombSentry(arena.getWidth() / 2, Measure.units(100f)));

        return arena;

    }





}
