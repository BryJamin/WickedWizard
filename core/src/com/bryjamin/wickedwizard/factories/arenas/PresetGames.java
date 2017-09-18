package com.bryjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;

import java.util.Random;

/**
 * Created by BB on 15/08/2017.
 */

public class PresetGames {


    public static String DEFAULT_GAME_ID = "8d74501f-91ac-4964-82f0-34700da3686f";


    public static final String LEVEL_ONE_ID = "6f339192-fc1b-43a8-bba0-6edc70860857";
    public static final String LEVEL_TWO_ID = "6e352a19-2bb3-4ded-9f1e-42aa7d3dfe40";
    public static final String LEVEL_THREE_ID = "14f52096-1b15-4ac0-9109-4c1e83f545a3";
    public static final String LEVEL_FOUR_ID = "5c746955-8c04-4f02-a54f-dec9aa79b7c6";
    public static final String LEVEL_FIVE_ID = "983c9e2d-52ce-4525-8a62-c6279212325d";





    public static GameCreator DEFAULT_GAME(AssetManager assetManager, Random random){

        com.bryjamin.wickedwizard.factories.arenas.levels.PresetGenerators presetGenerators = new com.bryjamin.wickedwizard.factories.arenas.levels.PresetGenerators();

        return new GameCreator(DEFAULT_GAME_ID,
                new GameCreator.LevelCreator(presetGenerators.level1Configuration(assetManager, random), ChallengesResource.LEVEL_1_COMPLETE),
                new GameCreator.LevelCreator(presetGenerators.level2Configuration(assetManager, random), ChallengesResource.LEVEL_2_COMPLETE),
                new GameCreator.LevelCreator(presetGenerators.level3Configuration(assetManager, random), ChallengesResource.LEVEL_3_COMPLETE),
                new GameCreator.LevelCreator(presetGenerators.level4Configuration(assetManager, random), ChallengesResource.LEVEL_4_COMPLETE),
                new GameCreator.LevelCreator(presetGenerators.level5Configuration(assetManager, random), ChallengesResource.LEVEL_5_COMPLETE));

    }







}
