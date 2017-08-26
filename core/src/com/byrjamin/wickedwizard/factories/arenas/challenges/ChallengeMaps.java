package com.byrjamin.wickedwizard.factories.arenas.challenges;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.OrderedMap;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.PortalFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.LightGraySkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;

import java.util.Random;

/**
 * Created by BB on 20/08/2017.
 */

public class ChallengeMaps extends AbstractFactory {


    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private PortalFactory portalFactory;
    private ItemFactory itemFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private Rank1ChallengeMaps rank1ChallengeMaps;
    private Rank2ChallengeMaps rank2ChallengeMaps;
    private Rank3ChallengeMaps rank3ChallengeMaps;
    private Rank4ChallengeMaps rank4ChallengeMaps;
    private Rank5ChallengeMaps rank5ChallengeMaps;

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
        this.rank1ChallengeMaps = new Rank1ChallengeMaps(assetManager, random);
        this.rank2ChallengeMaps = new Rank2ChallengeMaps(assetManager, random);
        this.rank3ChallengeMaps = new Rank3ChallengeMaps(assetManager, random);
        this.rank4ChallengeMaps = new Rank4ChallengeMaps(assetManager, random);
        this.rank5ChallengeMaps = new Rank5ChallengeMaps(assetManager, random);

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
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.perfectBlobba, rank1ChallengeMaps.perfectBlobba(ChallengesResource.Rank1Challenges.perfectBlobba));
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.perfectAdoj, rank1ChallengeMaps.perfectAdoj(ChallengesResource.Rank1Challenges.perfectAdoj));
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.tutorialSpeedRun, rank1ChallengeMaps.tutorialSpeedRun(ChallengesResource.Rank1Challenges.tutorialSpeedRun));
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.arenaSpeedRun, rank1ChallengeMaps.arenaSpeedRun(ChallengesResource.Rank1Challenges.arenaSpeedRun));

        mapOfChallenges.put(ChallengesResource.Rank2Challenges.perfectWanda, rank2ChallengeMaps.perfectWanda(ChallengesResource.Rank2Challenges.perfectWanda));
        mapOfChallenges.put(ChallengesResource.Rank2Challenges.perfectKugel, rank2ChallengeMaps.perfectKugel(ChallengesResource.Rank2Challenges.perfectKugel));
        mapOfChallenges.put(ChallengesResource.Rank2Challenges.arenaTrail, rank2ChallengeMaps.rank2ArenaRace(ChallengesResource.Rank2Challenges.arenaTrail));
        mapOfChallenges.put(ChallengesResource.Rank2Challenges.rank2TimeTrail, rank2ChallengeMaps.rank2TimeTrail(ChallengesResource.Rank2Challenges.rank2TimeTrail));

        mapOfChallenges.put(ChallengesResource.Rank3Challenges.perfectBoomy, rank3ChallengeMaps.perfectBoomy(ChallengesResource.Rank3Challenges.perfectBoomy));
        mapOfChallenges.put(ChallengesResource.Rank3Challenges.perfectAjir, rank3ChallengeMaps.perfectAjir(ChallengesResource.Rank3Challenges.perfectAjir));
        mapOfChallenges.put(ChallengesResource.Rank3Challenges.rank3ArenaTrial, rank3ChallengeMaps.rank3ArenaRace(ChallengesResource.Rank3Challenges.rank3ArenaTrial));
        mapOfChallenges.put(ChallengesResource.Rank3Challenges.rank3TimeTrail, rank3ChallengeMaps.timeTrial(ChallengesResource.Rank3Challenges.rank3TimeTrail));

        mapOfChallenges.put(ChallengesResource.Rank4Challenges.perfectWraith, rank4ChallengeMaps.perfectWraith(ChallengesResource.Rank4Challenges.perfectWraith));
        mapOfChallenges.put(ChallengesResource.Rank4Challenges.perfectAmalgama, rank4ChallengeMaps.perfectAmalgama(ChallengesResource.Rank4Challenges.perfectAmalgama));
        mapOfChallenges.put(ChallengesResource.Rank4Challenges.rank4Arena, rank4ChallengeMaps.rank4ArenaRace(ChallengesResource.Rank4Challenges.rank4Arena));
        mapOfChallenges.put(ChallengesResource.Rank4Challenges.rank4TimeTrail, rank4ChallengeMaps.timeTrial(ChallengesResource.Rank4Challenges.rank4TimeTrail));

        mapOfChallenges.put(ChallengesResource.Rank5Challenges.rank5UltimateTimeTrail, rank5ChallengeMaps.ultimateTimeTrail(ChallengesResource.Rank5Challenges.rank5UltimateTimeTrail));
        mapOfChallenges.put(ChallengesResource.Rank5Challenges.bossRush, rank5ChallengeMaps.bossRush(ChallengesResource.Rank5Challenges.bossRush));
        mapOfChallenges.put(ChallengesResource.Rank5Challenges.perfectBossRush, rank5ChallengeMaps.perfectBossRush(ChallengesResource.Rank5Challenges.perfectBossRush));
        mapOfChallenges.put(ChallengesResource.Rank5Challenges.rank5NotUltimateArena, rank5ChallengeMaps.rank5ArenaRace(ChallengesResource.Rank5Challenges.rank5NotUltimateArena));

    }


    public GameCreator getChallenge(String id){
        return mapOfChallenges.get(id);
    }



























}
