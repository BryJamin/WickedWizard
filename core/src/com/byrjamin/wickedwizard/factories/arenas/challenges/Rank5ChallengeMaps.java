package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAmalgama;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWraithCowl;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.ReuseableRooms;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.weapons.enemy.LaserBeam;
import com.byrjamin.wickedwizard.factories.weapons.enemy.LaserOrbitalTask;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by BB on 26/08/2017.
 */

public class Rank5ChallengeMaps extends AbstractFactory {


    private static final float ARENA_SPEEDRUN_TIMER = 45f;
    private static final float TIME_TRIAL_SPEEDRUN_TIMER =
            Rank4ChallengeMaps.TIME_TRIAL_SPEEDRUN_TIMER +
                    Rank3ChallengeMaps.TIME_TRIAL_SPEEDRUN_TIMER +
                    Rank2ChallengeMaps.TIME_TRIAL_SPEEDRUN_TIMER +
                    Rank1ChallengeMaps.TUTORIAL_SPEEDRUN_TIMER + 5f;


    private ArenaShellFactory arenaShellFactory;
    private DecorFactory decorFactory;
    private BeamTurretFactory beamTurretFactory;
    private ItemFactory itemFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin = Level.FOUR.getArenaSkin();

    private Random random;

    private Rank1ChallengeMaps rank1ChallengeMaps;
    private Rank2ChallengeMaps rank2ChallengeMaps;
    private Rank3ChallengeMaps rank3ChallengeMaps;
    private Rank4ChallengeMaps rank4ChallengeMaps;


    public Rank5ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.beamTurretFactory = new BeamTurretFactory(assetManager, arenaSkin);
        this.random = random;
        this.rank1ChallengeMaps = new Rank1ChallengeMaps(assetManager, random);
        this.rank2ChallengeMaps = new Rank2ChallengeMaps(assetManager, random);
        this.rank3ChallengeMaps = new Rank3ChallengeMaps(assetManager, random);
        this.rank4ChallengeMaps = new Rank4ChallengeMaps(assetManager, random);

    }


    public GameCreator ultimateTimeTrail(String id) {

        Arena startingArena = new ReuseableRooms(assetManager, Level.ONE.getArenaSkin()).challengeStartingArena(Level.FOUR.getMusic()).createArena(new MapCoords());

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

        Arena arena = rank1ChallengeMaps.timeTrailRoomOneJumpTutorial(new MapCoords(1, 0));
        arena.addEntity(new OnLoadFactory().challengeTimer(TIME_TRIAL_SPEEDRUN_TIMER));

        ArenaMap firstMap = new ArenaMap(startingArena,
                arena,
                rank1ChallengeMaps.timeTrailRoomTwoPlatformTutorial(new MapCoords(5,0)),
                rank1ChallengeMaps.timeTrailRoomThreeGrappleTutorial(new MapCoords(6,0)),
                rank1ChallengeMaps.timeTrialRoomFourEnemyTutorial(new MapCoords(6,3)),
                nextLevelRoom(new MapCoords(7,3), Level.ONE.getArenaSkin())
        );

        JigsawGeneratorConfig firstConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(firstMap);




        ArenaMap secondMap = new ArenaMap(new ArenaShellFactory(assetManager, Level.TWO.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0)),
                rank2ChallengeMaps.rank2TimeTrailRoom1(new MapCoords(1, 0)),
                rank2ChallengeMaps.rank2TimeTrailRoom2(new MapCoords(1,3)),
                rank2ChallengeMaps.rank2TimeTrailRoom3(new MapCoords(2,3)),
                rank2ChallengeMaps.rank2TimeTrailRoom4(new MapCoords(6,3)),
                nextLevelRoom(new MapCoords(7, 3), Level.TWO.getArenaSkin())
        );


        JigsawGeneratorConfig secondConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(secondMap);


        ArenaMap thirdMap = new ArenaMap(new ArenaShellFactory(assetManager, Level.THREE.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0)),
                rank3ChallengeMaps.trialRoom1(new MapCoords(1, 0)),
                rank3ChallengeMaps.timeTrailRoom2(new MapCoords(5,0)),
                rank3ChallengeMaps.trailRoom3(new MapCoords(6,0)),
                rank3ChallengeMaps.trailRoom4(new MapCoords(6,3)),
                nextLevelRoom(new MapCoords(7, 3), Level.THREE.getArenaSkin())
        );


        JigsawGeneratorConfig thirdConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(thirdMap);


        ArenaMap fourthMap = new ArenaMap(new ArenaShellFactory(assetManager, Level.FOUR.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0)),
                rank4ChallengeMaps.timeTrailSwitchLaserRoom(new MapCoords(1, 0)),
                rank4ChallengeMaps.trailRoomLargeRoomAndEnemies(new MapCoords(2,0)),
                rank4ChallengeMaps.trialRoomLaserGauntlet(new MapCoords(3, -1)),
                rank4ChallengeMaps.trailRoomPentaSentry(new MapCoords(7, 0)),
                nextLevelRoom(new MapCoords(6, 0), Level.FOUR.getArenaSkin())
        );


        JigsawGeneratorConfig fourthConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(fourthMap);


        ArenaMap fifthMap = new ArenaMap(new ArenaShellFactory(assetManager, Level.FIVE.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0)),
                new ReuseableRooms(assetManager, Level.FIVE.getArenaSkin()).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(1, 0))
        );


        JigsawGeneratorConfig fifthConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(fifthMap);



        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(firstConfig, false));
        gameCreator.add(new GameCreator.LevelCreator(secondConfig, false));
        gameCreator.add(new GameCreator.LevelCreator(thirdConfig, false));
        gameCreator.add(new GameCreator.LevelCreator(fourthConfig, false));
        gameCreator.add(new GameCreator.LevelCreator(fifthConfig, false));

        return gameCreator;


    }


    private Arena nextLevelRoom(MapCoords mapCoords, ArenaSkin arenaSkin){

        Arena exitArena = new ArenaShellFactory(assetManager, arenaSkin).createOmniArenaHiddenGrapple(mapCoords);

        exitArena.addEntity(new PortalFactory(assetManager).portal(exitArena.getWidth() / 2, Measure.units(32.5f), PortalFactory.levelPortalSize, PortalFactory.levelPortalSize, new Task() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(MapTeleportationSystem.class).createNewLevel();
                        for(BaseSystem s: world.getSystems()){
                            s.setEnabled(true);
                        }
                    }
                });
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        }));

        return exitArena;

    }


    public GameCreator perfectAmalgama(String id) {

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.FOUR.getMusic()).createArena(new MapCoords(0, 0));

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

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(7, 0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomAmalgama(assetManager, arenaSkin).amalgamaArena().createArena(new MapCoords(1, 0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }


    public GameCreator rank4ArenaRace(String id) {

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.FOUR.getMusic()).createArena(new MapCoords(0, 0));

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

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, -1));
        arena.addEntity(new OnLoadFactory().challengeTimer(ARENA_SPEEDRUN_TIMER));


        arena.addWave(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, Measure.units(40f), true, true),
                arenaEnemyPlacementFactory.spawnAngryBlob(arena.getWidth() / 2, Measure.units(40f), false));


        arena.addWave(arenaEnemyPlacementFactory.spawnLaserBouncer(arena.getWidth() / 2, Measure.units(30f), false),
                arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 2, Measure.units(30f), true));


        arena.addWave(arenaEnemyPlacementFactory.spawnModon(arena.getWidth() / 4, Measure.units(35f)),
                arenaEnemyPlacementFactory.spawnFixedSentry(arena.getWidth() / 4 * 3, Measure.units(45f)));

        arena.addWave(arenaEnemyPlacementFactory.spawnLaserKugel(arena.getWidth() / 4, Measure.units(25f), true));


        arena.addWave(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, Measure.units(45f)));

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(-1, -1));
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


    public GameCreator timeTrial(String id) {

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.FOUR.getMusic()).createArena(new MapCoords());

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

        Arena room1 = timeTrailSwitchLaserRoom(new MapCoords(1, 0));
        room1.addEntity(new OnLoadFactory().challengeTimer(TIME_TRIAL_SPEEDRUN_TIMER));

        ArenaMap arenaMap = new ArenaMap(startingArena,
                room1,
                trailRoomLargeRoomAndEnemies(new MapCoords(2, 0)),
                trialRoomLaserGauntlet(new MapCoords(3, -1)),
                trailRoomPentaSentry(new MapCoords(7, 0)),
                new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(6, 0))
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }


    private Arena trialRoomLaserGauntlet(MapCoords defaultCoords) {


        Arena arena = new ArenaBuilder(assetManager, arenaSkin)
                .addSection(new ArenaBuilder.Section(defaultCoords,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.DOOR, //upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.DOOR))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 2, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,//!upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 3, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,//!upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.FULL))
                .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 4, defaultCoords.getY()),
                        ArenaBuilder.wall.NONE,
                        ArenaBuilder.wall.FULL,
                        ArenaBuilder.wall.GRAPPLE,//!upperDoorIsLeft ? ArenaBuilder.wall.DOOR : ArenaBuilder.wall.FULL,
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


    private Arena trailRoomPentaSentry(MapCoords defaultCoords) {

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(defaultCoords);
        ComponentBag bag = arenaEnemyPlacementFactory.turretFactory.movingPentaSentry(arena.getWidth() / 2, Measure.units(40f), true, true);
        arena.addEntity(bag);
        return arena;

    }


    private Arena timeTrailSwitchLaserRoom(MapCoords defaultCoords) {

        Arena arena = arenaShellFactory.createSmallArenaNoGrapple(defaultCoords);
        arena.roomType = Arena.RoomType.TRAP;


        arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(45f), Measure.units(25f), 2,
                new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(4f))
                        .speedInDegrees(2f)
                        .numberOfOrbitals(20)
                        .chargeTime(0.5f)
                        .angles(70, 115, 160)
                        .build()));


        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(5f), Measure.units(40f), -90));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(10f), Measure.units(40f), 90));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(5f), Measure.units(20f), -90));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(Measure.units(20f), Measure.units(50f), 180));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(25f), Measure.units(50f), 180));
        arena.addWave(arenaEnemyPlacementFactory.switchFactory.fadeInswitchBag(arena.getWidth() - Measure.units(10f), Measure.units(20f), 90));

        return arena;
    }

    private Arena trailRoomLargeRoomAndEnemies(MapCoords defaultCoords) {

        Arena arena = new ArenaBuilder(assetManager, arenaSkin)
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
        // arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(110f)));
        arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(50f)));
        arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(95f)));
        //arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(110f)));
        arena.addEntity(decorFactory.platform(0, Measure.units(65f), arena.getWidth()));

        arena.addEntity(beamTurretFactory.inCombatLaserChain(Measure.units(90f), Measure.units(50f), 4,
                new LaserOrbitalTask.LaserBuilder(assetManager)
                        .orbitalAndIntervalSize(Measure.units(15f))
                        .speedInDegrees(-0.225f)
                        .numberOfOrbitals(10)
                        .chargeTime(0.5f)
                        .disperseTime(1f)
                        .angles(90, 270)
                        .build()));


        arena.addEntity(arenaEnemyPlacementFactory.spawnFixedTriSentry(arena.getWidth() / 2, Measure.units(35f)));
        arena.addEntity(arenaEnemyPlacementFactory.spawnFixedFlyByBombSentry(arena.getWidth() / 2, Measure.units(100f)));

        return arena;

    }


}

