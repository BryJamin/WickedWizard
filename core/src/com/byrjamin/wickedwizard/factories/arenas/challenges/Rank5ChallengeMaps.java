package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAjir;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBoomyMap;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBiggaBlobba;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAdoj;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAmalgama;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossArenaEndBoss;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomGiantKugelRoom;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWanda;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWraithCowl;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.ReuseableRooms;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.EndGameMap;
import com.byrjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemIWishYouWell;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Direction;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by BB on 26/08/2017.
 */

public class Rank5ChallengeMaps extends AbstractFactory {


    private static final float ARENA_SPEEDRUN_TIMER = 99f;
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

    private ArenaSkin arenaSkin = Level.FIVE.getArenaSkin();

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

        Arena startingArena = new ReuseableRooms(assetManager, Level.ONE.getArenaSkin()).challengeStartingArena(Level.FIVE.getMusic()).createArena(new MapCoords());

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




        ArenaMap secondMap = new ArenaMap(new ArenaShellFactory(assetManager, Level.TWO.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0), Arena.ArenaType.NORMAL),
                rank2ChallengeMaps.rank2TimeTrailRoom1(new MapCoords(1, 0)),
                rank2ChallengeMaps.rank2TimeTrailRoom2GoatWizard(new MapCoords(1,3)),
                rank2ChallengeMaps.rank2TimeTrailRoom3(new MapCoords(2,3)),
                rank2ChallengeMaps.rank2TimeTrailRoom4(new MapCoords(6,3)),
                nextLevelRoom(new MapCoords(7, 3), Level.TWO.getArenaSkin())
        );


        JigsawGeneratorConfig secondConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(secondMap);


        ArenaMap thirdMap = new ArenaMap(new ArenaShellFactory(assetManager, Level.THREE.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0), Arena.ArenaType.NORMAL),
                rank3ChallengeMaps.trialRoom1(new MapCoords(1, 0)),
                rank3ChallengeMaps.timeTrailRoom2(new MapCoords(5,0)),
                rank3ChallengeMaps.trailRoom3(new MapCoords(6,0)),
                rank3ChallengeMaps.trailRoom4(new MapCoords(6,3)),
                nextLevelRoom(new MapCoords(7, 3), Level.THREE.getArenaSkin())
        );


        JigsawGeneratorConfig thirdConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(thirdMap);


        ArenaMap fourthMap = new ArenaMap(new ArenaShellFactory(assetManager, Level.FOUR.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0), Arena.ArenaType.NORMAL),
                rank4ChallengeMaps.timeTrailSwitchLaserRoom(new MapCoords(1, 0)),
                rank4ChallengeMaps.trailRoomLargeRoomAndEnemies(new MapCoords(2,0)),
                rank4ChallengeMaps.trialRoomLaserGauntlet(new MapCoords(3, -1)),
                rank4ChallengeMaps.trailRoomPentaSentry(new MapCoords(7, 0)),
                nextLevelRoom(new MapCoords(6, 0), Level.FOUR.getArenaSkin())
        );


        JigsawGeneratorConfig fourthConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(fourthMap);


        ArenaMap fifthMap = new ArenaMap(new ArenaShellFactory(assetManager, Level.FIVE.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0), Arena.ArenaType.NORMAL),
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

        Arena exitArena = new ArenaShellFactory(assetManager, arenaSkin).createOmniArenaHiddenGrapple(mapCoords, Arena.ArenaType.NORMAL);

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


    public GameCreator bossRush(String id) {

        Arena startingArena = new ReuseableRooms(assetManager, Level.ONE.getArenaSkin()).challengeStartingArena(Level.FIVE.getMusic()).createArena(new MapCoords(0, 0));

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 4;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 4;
                e.deleteFromWorld();
            }
        }));


        ArenaMap arenaMap = new ArenaMap(startingArena,

                new BossRoomBiggaBlobba(assetManager, Level.ONE.getArenaSkin()).biggaBlobbaArena().createArena(new MapCoords(1, 0)),
                new ItemArenaFactory(assetManager, Level.ONE.getArenaSkin()).createBossRushItemRoom(new MapCoords(2, 0)),

                new BossRoomAdoj(assetManager, Level.ONE.getArenaSkin()).adojArena().createArena(new MapCoords(3, 0)),
                new ItemArenaFactory(assetManager, Level.TWO.getArenaSkin()).createBossRushItemRoom(new MapCoords(4, 0)),

                new BossRoomGiantKugelRoom(assetManager, Level.TWO.getArenaSkin()).giantKugelArena().createArena(new MapCoords(5, 0)),
                new ItemArenaFactory(assetManager, Level.TWO.getArenaSkin()).createBossRushItemRoom(new MapCoords(7, 0)),

                new BossRoomWanda(assetManager, Level.TWO.getArenaSkin()).wandaArena().createArena(new MapCoords(8, 0)),
                new ItemArenaFactory(assetManager, Level.THREE.getArenaSkin()).createBossRushItemRoom(new MapCoords(9, 0)),

                new BossRoomBoomyMap(assetManager, Level.THREE.getArenaSkin()).boomyArena().createArena(new MapCoords(10, 0)),
                new ItemArenaFactory(assetManager, Level.THREE.getArenaSkin()).createBossRushItemRoom(new MapCoords(11, 0)),

                new BossRoomAjir(assetManager, Level.THREE.getArenaSkin()).ajirArena().createArena(new MapCoords(12, 0)),
                new ItemArenaFactory(assetManager, Level.FOUR.getArenaSkin()).createBossRushItemRoom(new MapCoords(13, 0)),

                new BossRoomWraithCowl(assetManager, Level.FOUR.getArenaSkin()).wraithcowlArena().createArena(new MapCoords(14, 0)),
                new ItemArenaFactory(assetManager, Level.FOUR.getArenaSkin()).createBossRushItemRoom(new MapCoords(15, 0)),

                new ArenaShellFactory(assetManager, Level.FOUR.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(16, 0), Arena.ArenaType.NORMAL),
                new BossRoomAmalgama(assetManager, Level.FOUR.getArenaSkin()).amalgamaArena().createArena(new MapCoords(16, -1)),
                new ItemArenaFactory(assetManager, Level.FIVE.getArenaSkin()).createBossRushItemRoom(new MapCoords(22, -1)),


                new BossArenaEndBoss(assetManager, Level.FIVE.getArenaSkin()).endStartingRoom(new EndGameMap(assetManager).endBossRushMap(id)).createArena(new MapCoords(23, -1))

        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }



    public GameCreator perfectBossRush(String id) {

        Arena startingArena = new ReuseableRooms(assetManager, Level.FIVE.getArenaSkin()).challengeStartingArena(Level.FIVE.getMusic()).createArena(new MapCoords(-1, 0));

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(LuckSystem.class).turnOffEnemyDrops();
                e.deleteFromWorld();
            }
        }));


        Arena iWishYouWell = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0), Arena.ArenaType.NORMAL);
        iWishYouWell.addEntity(itemFactory.createItemAltarBag(Measure.units(42.5f),
                Measure.units(10f), arenaSkin.getWallTint(), new ItemIWishYouWell()));

        ArenaMap arenaMap = new ArenaMap(startingArena,

                iWishYouWell,


                new BossRoomBiggaBlobba(assetManager, Level.FIVE.getArenaSkin()).biggaBlobbaArena().createArena(new MapCoords(1, 0)),

                new BossRoomAdoj(assetManager, Level.FIVE.getArenaSkin()).adojArena().createArena(new MapCoords(2, 0)),

                new BossRoomGiantKugelRoom(assetManager, Level.FIVE.getArenaSkin()).giantKugelArena().createArena(new MapCoords(3, 0)),

                new BossRoomWanda(assetManager, Level.FIVE.getArenaSkin()).wandaArena().createArena(new MapCoords(5, 0)),

                new BossRoomBoomyMap(assetManager, Level.FIVE.getArenaSkin()).boomyArena().createArena(new MapCoords(6, 0)),

                new BossRoomAjir(assetManager, Level.FIVE.getArenaSkin()).ajirArena().createArena(new MapCoords(7, 0)),

                new BossRoomWraithCowl(assetManager, Level.FIVE.getArenaSkin()).wraithcowlArena().createArena(new MapCoords(8, 0)),

                new BossRoomAmalgama(assetManager, Level.FIVE.getArenaSkin()).amalgamaArena().createArena(new MapCoords(8, -1)),

                new BossArenaEndBoss(assetManager, Level.FIVE.getArenaSkin()).endStartingRoom(new EndGameMap(assetManager).endBossRushMap(id)).createArena(new MapCoords(14, -1))

        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }


    public GameCreator rank5ArenaRace(String id) {

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.FIVE.getMusic()).createArena(new MapCoords(0, 0));

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

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(-1, 0), Arena.ArenaType.TRAP);
        arena.addEntity(new OnLoadFactory().challengeTimer(ARENA_SPEEDRUN_TIMER));

        arena.addWave(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, Measure.units(45f)));


        arena.addWave(arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, arena.getHeight() / 2, Direction.RIGHT, 5),
                arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, arena.getHeight() / 2, Direction.LEFT, 5));

        arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 4, Measure.units(40f), true, true),
                arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 4 * 3, Measure.units(40f), false, true));


        arena.addWave(arenaEnemyPlacementFactory.spawnJumpingJack(arena.getWidth() / 2, Measure.units(45f), false));


        arena.addWave(arenaEnemyPlacementFactory.spawnFixedPentaSentry(arena.getWidth() / 4, Measure.units(45f)),
                arenaEnemyPlacementFactory.spawnFixedFlyByDoubleBombSentry(arena.getWidth() / 4 * 3, Measure.units(45f)));





        ArenaMap arenaMap = new ArenaMap(startingArena,
                arena,
                new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(-2, 0))
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }

}

