package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.LuckSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.GiantKugelRoom;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.WandaRoom;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.OnLoadFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.ReuseableRooms;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by BB on 24/08/2017.
 */

public class Rank2ChallengeMaps extends AbstractFactory {


    private static final float ARENA_SPEEDRUN_TIMER = 50f;
    private static final float TIME_TRIAL_SPEEDRUN_TIMER = 50f;


    private ArenaShellFactory arenaShellFactory;
    private DecorFactory decorFactory;
    private ItemFactory itemFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin = Level.TWO.getArenaSkin();

    private Random random;


    public Rank2ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);

        this.random = random;
    }


    public GameCreator perfectWanda(String id){

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.TWO.getMusic()).createArena(new MapCoords());

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
                new WandaRoom(assetManager, arenaSkin).wandaArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }




    public GameCreator perfectKugel(String id){

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.TWO.getMusic()).createArena(new MapCoords());

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

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(3,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new GiantKugelRoom(assetManager, arenaSkin).giantKugelArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }



    public GameCreator rank2ArenaRace(String id){

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
        arena.addEntity(decorFactory.spikeWall(Measure.units(5f), Measure.units(10f), Measure.units(5f), Measure.units(50f), 270));
        arena.addEntity(decorFactory.spikeWall(Measure.units(90f), Measure.units(10f), Measure.units(5f), Measure.units(50f), 90));
/*

        arena.addWave(arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() / 4, Measure.units(45f), false),
                arenaEnemyPlacementFactory.spawnMovingSentry(arena.getWidth() / 4 * 3, Measure.units(45f), true));
*/

        arena.addWave(arenaEnemyPlacementFactory.spawnkugelDusche(arena.getWidth() / 2, arena.getHeight() / 2, true));
        arena.addWave(arenaEnemyPlacementFactory.spawnGoatWizard(arena.getWidth() / 2, arena.getHeight() / 2, true, true));

        arena.addWave(arenaEnemyPlacementFactory.spawnMovingJig(arena.getWidth() / 2, arena.getHeight() / 2, true),
                arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4 * 3, Measure.units(35f), true),
                arenaEnemyPlacementFactory.spawnBouncer(arena.getWidth() / 4, Measure.units(35f), false));

        arena.addWave(arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight()),
        arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth() / 2, arena.getHeight()),
        arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight()),
        arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(0, arena.getHeight() / 2),
        arenaEnemyPlacementFactory.amoebaFactory.fastamoeba(arena.getWidth(), arena.getHeight() / 2));

        arena.addWave(arenaEnemyPlacementFactory.spawnLaserKugel(arena.getWidth() / 2, arena.getHeight() / 2, false));

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















}
