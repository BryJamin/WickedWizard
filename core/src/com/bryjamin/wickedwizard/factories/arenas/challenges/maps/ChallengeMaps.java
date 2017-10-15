package com.bryjamin.wickedwizard.factories.arenas.challenges.maps;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.OrderedMap;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.GameCreator;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;

import java.util.Random;

;

/**
 * Created by BB on 20/08/2017.
 */

public class ChallengeMaps extends AbstractFactory {


    private com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory arenaShellFactory;
    private com.bryjamin.wickedwizard.factories.chests.ChestFactory chestFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory portalFactory;
    private com.bryjamin.wickedwizard.factories.items.ItemFactory itemFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;

    private com.bryjamin.wickedwizard.factories.arenas.challenges.maps.Rank1ChallengeMaps rank1ChallengeMaps;
    private com.bryjamin.wickedwizard.factories.arenas.challenges.maps.Rank2ChallengeMaps rank2ChallengeMaps;
    private com.bryjamin.wickedwizard.factories.arenas.challenges.maps.Rank3ChallengeMaps rank3ChallengeMaps;
    private com.bryjamin.wickedwizard.factories.arenas.challenges.maps.Rank4ChallengeMaps rank4ChallengeMaps;
    private com.bryjamin.wickedwizard.factories.arenas.challenges.maps.Rank5ChallengeMaps rank5ChallengeMaps;

    ArenaSkin arenaSkin = new com.bryjamin.wickedwizard.factories.arenas.skins.LightGraySkin();

    private Random random;

    private OrderedMap<String, GameCreator> mapOfChallenges = new OrderedMap<String, GameCreator>();

    public ChallengeMaps(AssetManager assetManager, Random random) {
        super(assetManager);
        this.arenaShellFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new com.bryjamin.wickedwizard.factories.chests.ChestFactory(assetManager);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.portalFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.PortalFactory(assetManager);
        this.itemFactory = new com.bryjamin.wickedwizard.factories.items.ItemFactory(assetManager);
        this.arenaEnemyPlacementFactory = new ArenaEnemyPlacementFactory(assetManager, arenaSkin, random);
        this.rank1ChallengeMaps = new Rank1ChallengeMaps(assetManager, random);
        this.rank2ChallengeMaps = new Rank2ChallengeMaps(assetManager, random);
        this.rank3ChallengeMaps = new Rank3ChallengeMaps(assetManager, random);
        this.rank4ChallengeMaps = new Rank4ChallengeMaps(assetManager, random);
        this.rank5ChallengeMaps = new Rank5ChallengeMaps(assetManager, random);

        this.random = random;
        setUpMap();
    }

    public void setUpMap(){

        mapOfChallenges.put(ChallengesResource.Rank1Challenges.tutorialSpeedRun, rank1ChallengeMaps.tutorialSpeedRun(ChallengesResource.Rank1Challenges.TUTORIAL_SPEEDRUN));
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.arenaSpeedRun, rank1ChallengeMaps.arenaSpeedRun(ChallengesResource.Rank1Challenges.ARENA_SPEEDRUN));
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.perfectBlobba, rank1ChallengeMaps.perfectBlobba(ChallengesResource.Rank1Challenges.PERFECT_BLOBBA));
        mapOfChallenges.put(ChallengesResource.Rank1Challenges.perfectAdoj, rank1ChallengeMaps.perfectAdoj(ChallengesResource.Rank1Challenges.PERFECT_ADOJ));

        mapOfChallenges.put(ChallengesResource.Rank2Challenges.rank2TimeTrail, rank2ChallengeMaps.rank2TimeTrail(ChallengesResource.Rank2Challenges.RANK_TWO_TIME_TRAIL));
        mapOfChallenges.put(ChallengesResource.Rank2Challenges.arenaTrail, rank2ChallengeMaps.rank2ArenaRace(ChallengesResource.Rank2Challenges.RANK_TWO_ARENA));
        mapOfChallenges.put(ChallengesResource.Rank2Challenges.perfectWanda, rank2ChallengeMaps.perfectWanda(ChallengesResource.Rank2Challenges.PERFECT_WANDA));
        mapOfChallenges.put(ChallengesResource.Rank2Challenges.perfectKugel, rank2ChallengeMaps.perfectKugel(ChallengesResource.Rank2Challenges.PERFECT_KUGEL));

        mapOfChallenges.put(ChallengesResource.Rank3Challenges.rank3TimeTrail, rank3ChallengeMaps.timeTrial(ChallengesResource.Rank3Challenges.RANK_THREE_TIME_TRAIL));
        mapOfChallenges.put(ChallengesResource.Rank3Challenges.rank3ArenaTrial, rank3ChallengeMaps.rank3ArenaRace(ChallengesResource.Rank3Challenges.RANK_THREE_ARENA));
        mapOfChallenges.put(ChallengesResource.Rank3Challenges.perfectBoomy, rank3ChallengeMaps.perfectBoomy(ChallengesResource.Rank3Challenges.PERFECT_BOOMY));
        mapOfChallenges.put(ChallengesResource.Rank3Challenges.perfectAjir, rank3ChallengeMaps.perfectAjir(ChallengesResource.Rank3Challenges.PERFECT_AJIR));

        mapOfChallenges.put(ChallengesResource.Rank4Challenges.rank4TimeTrail, rank4ChallengeMaps.timeTrial(ChallengesResource.Rank4Challenges.RANK_FOUR_TIME_TRAIL));
        mapOfChallenges.put(ChallengesResource.Rank4Challenges.rank4Arena, rank4ChallengeMaps.rank4ArenaRace(ChallengesResource.Rank4Challenges.RANK_FOUR_ARENA));
        mapOfChallenges.put(ChallengesResource.Rank4Challenges.perfectWraith, rank4ChallengeMaps.perfectWraith(ChallengesResource.Rank4Challenges.PERFECT_WRAITH));
        mapOfChallenges.put(ChallengesResource.Rank4Challenges.perfectAmalgama, rank4ChallengeMaps.perfectAmalgama(ChallengesResource.Rank4Challenges.PERFECT_AMALGAMA));

        mapOfChallenges.put(ChallengesResource.Rank5Challenges.rank5UltimateTimeTrail, rank5ChallengeMaps.ultimateTimeTrail(ChallengesResource.Rank5Challenges.RANK_FIVE_TIME_TRAIL));
        mapOfChallenges.put(ChallengesResource.Rank5Challenges.rank5NotUltimateArena, rank5ChallengeMaps.rank5ArenaRace(ChallengesResource.Rank5Challenges.RANK_FIVE_ARENA));
        mapOfChallenges.put(ChallengesResource.Rank5Challenges.bossRush, rank5ChallengeMaps.bossRush(ChallengesResource.Rank5Challenges.BOSS_RUSH));
        mapOfChallenges.put(ChallengesResource.Rank5Challenges.perfectBossRush, rank5ChallengeMaps.perfectBossRush(ChallengesResource.Rank5Challenges.PERFECT_BOSS_RUSH));

    }


    public GameCreator getChallenge(String id){
        return mapOfChallenges.get(id);
    }



























}
