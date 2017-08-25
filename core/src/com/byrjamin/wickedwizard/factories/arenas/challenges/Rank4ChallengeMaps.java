package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomAmalgama;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.BossRoomWraithCowl;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.ReuseableRooms;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by BB on 24/08/2017.
 */

public class Rank4ChallengeMaps extends AbstractFactory {


    private static final float ARENA_SPEEDRUN_TIMER = 35f;


    private ArenaShellFactory arenaShellFactory;
    private DecorFactory decorFactory;
    private ItemFactory itemFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private ArenaSkin arenaSkin = Level.FOUR.getArenaSkin();

    private Random random;


    public Rank4ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new ArenaShellFactory(assetManager, arenaSkin);
        this.decorFactory = new DecorFactory(assetManager, arenaSkin);
        this.itemFactory = new ItemFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);

        this.random = random;
    }


    public GameCreator perfectWraith(String id){

        Arena startingArena = new ReuseableRooms(assetManager, arenaSkin).challengeStartingArena(Level.FOUR.getMusic()).createArena(new MapCoords());

        ComponentBag bag = startingArena.createArenaBag();
        bag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).health = 1;
                world.getSystem(FindPlayerSystem.class).getPlayerComponent(StatComponent.class).maxHealth = 2;
                e.deleteFromWorld();
            }
        }));

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(2,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomWraithCowl(assetManager, arenaSkin).wraithcowlArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }




    public GameCreator perfectAmalgama(String id){

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

        Arena endArena = new ReuseableRooms(assetManager, arenaSkin).challengeEndArenaRightPortal(id).createArena(new MapCoords(7,0));
        ArenaMap arenaMap = new ArenaMap(startingArena,
                new BossRoomAmalgama(assetManager, arenaSkin).amalgamaArena().createArena(new MapCoords(1,0)),
                endArena
        );

        JigsawGeneratorConfig jigsawGeneratorConfig = new JigsawGeneratorConfig(assetManager, random)
                .startingMap(arenaMap);

        GameCreator gameCreator = new GameCreator();
        gameCreator.add(new GameCreator.LevelCreator(jigsawGeneratorConfig, false));

        return gameCreator;


    }



}
