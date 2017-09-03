package com.bryjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.systems.LuckSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAdoj;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAmalgama;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomGiantKugelRoom;
import com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWraithCowl;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.bryjamin.wickedwizard.factories.arenas.presetmaps.GalleryAtTheEndMap;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by BB on 26/08/2017.
 */

public class Rank5ChallengeMaps extends AbstractFactory {


    private static final float ARENA_SPEEDRUN_TIMER = 99f;
    private static final float TIME_TRIAL_SPEEDRUN_TIMER =
            com.bryjamin.wickedwizard.factories.arenas.challenges.Rank4ChallengeMaps.TIME_TRIAL_SPEEDRUN_TIMER +
                    Rank3ChallengeMaps.TIME_TRIAL_SPEEDRUN_TIMER +
                    Rank2ChallengeMaps.TIME_TRIAL_SPEEDRUN_TIMER +
                    Rank1ChallengeMaps.TUTORIAL_SPEEDRUN_TIMER + 5f;


    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory beamTurretFactory;
    private com.bryjamin.wickedwizard.factories.items.ItemFactory itemFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin = com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin();

    private Random random;

    private Rank1ChallengeMaps rank1ChallengeMaps;
    private Rank2ChallengeMaps rank2ChallengeMaps;
    private Rank3ChallengeMaps rank3ChallengeMaps;
    private com.bryjamin.wickedwizard.factories.arenas.challenges.Rank4ChallengeMaps rank4ChallengeMaps;


    public Rank5ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.itemFactory = new com.bryjamin.wickedwizard.factories.items.ItemFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.beamTurretFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.BeamTurretFactory(assetManager, arenaSkin);
        this.random = random;
        this.rank1ChallengeMaps = new Rank1ChallengeMaps(assetManager, random);
        this.rank2ChallengeMaps = new Rank2ChallengeMaps(assetManager, random);
        this.rank3ChallengeMaps = new Rank3ChallengeMaps(assetManager, random);
        this.rank4ChallengeMaps = new com.bryjamin.wickedwizard.factories.arenas.challenges.Rank4ChallengeMaps(assetManager, random);

    }


    public com.bryjamin.wickedwizard.factories.arenas.GameCreator ultimateTimeTrail(String id) {

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.ONE.getArenaSkin()).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getMusic()).createArena(new MapCoords());

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
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
                nextLevelRoom(new MapCoords(7,3), com.bryjamin.wickedwizard.utils.enums.Level.ONE.getArenaSkin())
        );

        JigsawGeneratorConfig firstConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(firstMap);




        ArenaMap secondMap = new ArenaMap(new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.TWO.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0), Arena.ArenaType.NORMAL),
                rank2ChallengeMaps.rank2TimeTrailRoom1(new MapCoords(1, 0)),
                rank2ChallengeMaps.rank2TimeTrailRoom2GoatWizard(new MapCoords(1,3)),
                rank2ChallengeMaps.rank2TimeTrailRoom3(new MapCoords(2,3)),
                rank2ChallengeMaps.rank2TimeTrailRoom4(new MapCoords(6,3)),
                nextLevelRoom(new MapCoords(7, 3), com.bryjamin.wickedwizard.utils.enums.Level.TWO.getArenaSkin())
        );


        JigsawGeneratorConfig secondConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(secondMap);


        ArenaMap thirdMap = new ArenaMap(new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.THREE.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0), Arena.ArenaType.NORMAL),
                rank3ChallengeMaps.trialRoom1(new MapCoords(1, 0)),
                rank3ChallengeMaps.timeTrailRoom2(new MapCoords(5,0)),
                rank3ChallengeMaps.trailRoom3(new MapCoords(6,0)),
                rank3ChallengeMaps.trailRoom4(new MapCoords(6,3)),
                nextLevelRoom(new MapCoords(7, 3), com.bryjamin.wickedwizard.utils.enums.Level.THREE.getArenaSkin())
        );


        JigsawGeneratorConfig thirdConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(thirdMap);


        ArenaMap fourthMap = new ArenaMap(new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0), Arena.ArenaType.NORMAL),
                rank4ChallengeMaps.timeTrailSwitchLaserRoom(new MapCoords(1, 0)),
                rank4ChallengeMaps.trailRoomLargeRoomAndEnemies(new MapCoords(2,0)),
                rank4ChallengeMaps.trialRoomLaserGauntlet(new MapCoords(3, -1)),
                rank4ChallengeMaps.trailRoomPentaSentry(new MapCoords(7, 0)),
                nextLevelRoom(new MapCoords(6, 0), com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getArenaSkin())
        );


        JigsawGeneratorConfig fourthConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(fourthMap);


        ArenaMap fifthMap = new ArenaMap(new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(0,0), Arena.ArenaType.NORMAL),
                new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(1, 0))
        );


        JigsawGeneratorConfig fifthConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(fifthMap);



        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(firstConfig, false));
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(secondConfig, false));
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(thirdConfig, false));
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(fourthConfig, false));
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(fifthConfig, false));

        return gameCreator;


    }


    private Arena nextLevelRoom(MapCoords mapCoords, ArenaSkin arenaSkin){

        Arena exitArena = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin).createOmniArenaHiddenGrapple(mapCoords, Arena.ArenaType.NORMAL);

        exitArena.addEntity(new com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory(assetManager).portal(exitArena.getWidth() / 2, Measure.units(32.5f), com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory.levelPortalSize, com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory.levelPortalSize, new com.bryjamin.wickedwizard.ecs.components.ai.Task() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem.class).createNewLevel();
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


    public com.bryjamin.wickedwizard.factories.arenas.GameCreator bossRush(String id) {

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.ONE.getArenaSkin()).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getMusic()).createArena(new MapCoords(0, 0));

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 4;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 4;
                e.deleteFromWorld();
            }
        }));


        ArenaMap arenaMap = new ArenaMap(startingArena,

                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBiggaBlobba(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.ONE.getArenaSkin()).biggaBlobbaArena().createArena(new MapCoords(1, 0)),
                new com.bryjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.ONE.getArenaSkin()).createBossRushItemRoom(new MapCoords(2, 0)),

                new BossRoomAdoj(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.ONE.getArenaSkin()).adojArena().createArena(new MapCoords(3, 0)),
                new com.bryjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.TWO.getArenaSkin()).createBossRushItemRoom(new MapCoords(4, 0)),

                new BossRoomGiantKugelRoom(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.TWO.getArenaSkin()).giantKugelArena().createArena(new MapCoords(5, 0)),
                new com.bryjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.TWO.getArenaSkin()).createBossRushItemRoom(new MapCoords(7, 0)),

                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWanda(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.TWO.getArenaSkin()).wandaArena().createArena(new MapCoords(8, 0)),
                new com.bryjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.THREE.getArenaSkin()).createBossRushItemRoom(new MapCoords(9, 0)),

                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBoomyMap(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.THREE.getArenaSkin()).boomyArena().createArena(new MapCoords(10, 0)),
                new com.bryjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.THREE.getArenaSkin()).createBossRushItemRoom(new MapCoords(11, 0)),

                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAjir(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.THREE.getArenaSkin()).ajirArena().createArena(new MapCoords(12, 0)),
                new com.bryjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getArenaSkin()).createBossRushItemRoom(new MapCoords(13, 0)),

                new BossRoomWraithCowl(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getArenaSkin()).wraithcowlArena().createArena(new MapCoords(14, 0)),
                new com.bryjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getArenaSkin()).createBossRushItemRoom(new MapCoords(15, 0)),

                new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(16, 0), Arena.ArenaType.NORMAL),
                new BossRoomAmalgama(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getArenaSkin()).amalgamaArena().createArena(new MapCoords(16, -1)),
                new com.bryjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).createBossRushItemRoom(new MapCoords(22, -1)),


                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossArenaEndBoss(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).endStartingRoom(new GalleryAtTheEndMap(assetManager).endBossRushMap(id)).createArena(new MapCoords(23, -1))

        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }



    public com.bryjamin.wickedwizard.factories.arenas.GameCreator perfectBossRush(String id) {

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getMusic()).createArena(new MapCoords(-1, 0));

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(LuckSystem.class).turnOffEnemyDrops();
                e.deleteFromWorld();
            }
        }));


        Arena iWishYouWell = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(0, 0), Arena.ArenaType.NORMAL);
        iWishYouWell.addEntity(itemFactory.createItemAltarBag(Measure.units(42.5f),
                Measure.units(10f), arenaSkin.getWallTint(), new com.bryjamin.wickedwizard.factories.items.passives.luck.ItemIWishYouWell()));

        ArenaMap arenaMap = new ArenaMap(startingArena,

                iWishYouWell,


                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBiggaBlobba(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).biggaBlobbaArena().createArena(new MapCoords(1, 0)),

                new BossRoomAdoj(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).adojArena().createArena(new MapCoords(2, 0)),

                new BossRoomGiantKugelRoom(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).giantKugelArena().createArena(new MapCoords(3, 0)),

                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWanda(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).wandaArena().createArena(new MapCoords(5, 0)),

                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomBoomyMap(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).boomyArena().createArena(new MapCoords(6, 0)),

                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAjir(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).ajirArena().createArena(new MapCoords(7, 0)),

                new BossRoomWraithCowl(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).wraithcowlArena().createArena(new MapCoords(8, 0)),

                new BossRoomAmalgama(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).amalgamaArena().createArena(new MapCoords(8, -1)),

                new com.bryjamin.wickedwizard.factories.arenas.bossrooms.BossArenaEndBoss(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin()).endStartingRoom(new GalleryAtTheEndMap(assetManager).endBossRushMap(id)).createArena(new MapCoords(14, -1))

        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }


    public com.bryjamin.wickedwizard.factories.arenas.GameCreator rank5ArenaRace(String id) {

        Arena startingArena = new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getMusic()).createArena(new MapCoords(0, 0));

        com.bryjamin.wickedwizard.utils.ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                world.getSystem(LuckSystem.class).turnOffEnemyDrops();
                e.deleteFromWorld();
            }
        }));

        Arena arena = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords(-1, 0), Arena.ArenaType.TRAP);
        arena.addEntity(new OnLoadFactory().challengeTimer(ARENA_SPEEDRUN_TIMER));

        arena.addWave(arenaEnemyPlacementFactory.spawnHeavyModon(arena.getWidth() / 2, Measure.units(45f)));


        arena.addWave(arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, arena.getHeight() / 2, com.bryjamin.wickedwizard.utils.enums.Direction.RIGHT, 5),
                arenaEnemyPlacementFactory.spawnLeftSnake(arena.getWidth() / 2, arena.getHeight() / 2, com.bryjamin.wickedwizard.utils.enums.Direction.LEFT, 5));

        arena.addWave(arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 4, Measure.units(40f), true, true),
                arenaEnemyPlacementFactory.spawnLaserus(arena.getWidth() / 4 * 3, Measure.units(40f), false, true));


        arena.addWave(arenaEnemyPlacementFactory.spawnJumpingJack(arena.getWidth() / 2, Measure.units(45f), false));


        arena.addWave(arenaEnemyPlacementFactory.spawnFixedPentaSentry(arena.getWidth() / 4, Measure.units(45f)),
                arenaEnemyPlacementFactory.spawnFixedFlyByDoubleBombSentry(arena.getWidth() / 4 * 3, Measure.units(45f)));





        ArenaMap arenaMap = new ArenaMap(startingArena,
                arena,
                new com.bryjamin.wickedwizard.factories.arenas.levels.ReuseableRooms(assetManager, arenaSkin).challengeEndArenaMiddlePortal(id).createArena(new MapCoords(-2, 0))
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator = new com.bryjamin.wickedwizard.factories.arenas.GameCreator();
        gameCreator.add(new com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }

}

