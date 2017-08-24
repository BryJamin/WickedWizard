package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.OrderedMap;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChallengeTimerComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BiggaBlobbaMap;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAdoj;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.ReuseableRooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.TutorialFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.LightGraySkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Locale;
import java.util.Random;

/**
 * Created by BB on 20/08/2017.
 */

public class ChallengeMaps extends AbstractFactory {


    private static final float ARENA_SPEEDRUN_TIMER = 35f;


    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private PortalFactory portalFactory;
    private ItemFactory itemFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private Rank2ChallengeMaps rank2ChallengeMaps;
    private Rank3ChallengeMaps rank3ChallengeMaps;
    private Rank4ChallengeMaps rank4ChallengeMaps;

    ArenaSkin arenaSkin = new LightGraySkin();

    private Random random;


    private OrderedMap<String, GameCreator> mapOfChallenges = new OrderedMap<String, GameCreator>();





    public ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.portalFactory = new PortalFactory(assetManager);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.rank2ChallengeMaps = new Rank2ChallengeMaps(assetManager, random);
        this.rank3ChallengeMaps = new Rank3ChallengeMaps(assetManager, random);
        this.rank4ChallengeMaps = new Rank4ChallengeMaps(assetManager, random);

        this.random = random;
        setUpMap();
    }


    public void updateArenaSkin(ArenaSkin arenaSkin){
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.arenaSkin = arenaSkin;
    }



    public void setUpMap(){
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.perfectBlobba, perfectBlobba(ChallengesResource.Rank1Challenges.perfectBlobba));
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.perfectAdoj, perfectAdoj(ChallengesResource.Rank1Challenges.perfectAdoj));
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.tutorialSpeedRun, tutorialSpeedRun(ChallengesResource.Rank1Challenges.tutorialSpeedRun));
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.arenaSpeedRun, arenaSpeedRun(ChallengesResource.Rank1Challenges.arenaSpeedRun));

        mapOfChallenges.put(ChallengesResource.Rank2Challenges.perfectWanda, rank2ChallengeMaps.perfectWanda(ChallengesResource.Rank2Challenges.perfectWanda));
        mapOfChallenges.put(ChallengesResource.Rank2Challenges.perfectKugel, rank2ChallengeMaps.perfectKugel(ChallengesResource.Rank2Challenges.perfectKugel));

        mapOfChallenges.put(ChallengesResource.Rank3Challenges.perfectBoomy, rank3ChallengeMaps.perfectBoomy(ChallengesResource.Rank3Challenges.perfectBoomy));
        mapOfChallenges.put(ChallengesResource.Rank3Challenges.perfectAjir, rank3ChallengeMaps.perfectAjir(ChallengesResource.Rank3Challenges.perfectAjir));

        mapOfChallenges.put(ChallengesResource.Rank4Challenges.perfectWraith, rank4ChallengeMaps.perfectWraith(ChallengesResource.Rank4Challenges.perfectWraith));
        mapOfChallenges.put(ChallengesResource.Rank4Challenges.perfectAmalgama, rank4ChallengeMaps.perfectAmalgama(ChallengesResource.Rank4Challenges.perfectAmalgama));

    }


    public GameCreator getChallenge(String id){
        return mapOfChallenges.get(id);
    }


    public GameCreator perfectBlobba(String id){

        updateArenaSkin(Level.ONE.getArenaSkin());

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.ONE.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArena(id).createArena(new MapCoords(2,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BiggaBlobbaMap(assetManager, arenaSkin).biggaBlobbaArena().createArena(new MapCoords(1,0)),
                endArena
                );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));


        return gameCreator;


    }




    public GameCreator perfectAdoj(String id){

        updateArenaSkin(Level.ONE.getArenaSkin());

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.ONE.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArena(id).createArena(new MapCoords(2,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomAdoj(assetManager, arenaSkin).adojArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));


        return gameCreator;

    }





    public GameCreator tutorialSpeedRun(String id){

        updateArenaSkin(Level.ONE.getArenaSkin());

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.ONE.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 2;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                e.deleteFromWorld();
            }
        }));

        TutorialFactory tutorialFactory = new TutorialFactory(assetManager, arenaSkin);
        startingArena.addEntity(new OnLoadFactory().challengeTimer(20));

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArena(id).createArena(new MapCoords(7,3));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                tutorialFactory.jumpTutorial(new MapCoords(1, 0)),
                tutorialFactory.platformTutorial(new MapCoords(5,0)),
                tutorialFactory.grappleTutorial(new MapCoords(6,0)),
                tutorialFactory.enemyTurtorial(new MapCoords(6,3)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));


        return gameCreator;

    }




    public GameCreator arenaSpeedRun(String id){

        updateArenaSkin(Level.ONE.getArenaSkin());

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.ONE.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 2;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).crit = 0;
                e.deleteFromWorld();
            }
        }));


        Arena arena = arenaShellFactory.createSmallArenaNoGrapple(new MapCoords(1,0));
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


        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArena(id).createArena(new MapCoords(2, 0));

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




























}
