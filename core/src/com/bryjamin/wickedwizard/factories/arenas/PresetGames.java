package com.bryjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;

import java.util.Random;

/**
 * Created by BB on 15/08/2017.
 */

public class PresetGames {


    public static String DEFAULT_GAME_ID = "8d74501f-91ac-4964-82f0-34700da3686f";


    public static GameCreator DEFAULT_GAME(AssetManager assetManager, Random random){

        com.bryjamin.wickedwizard.factories.arenas.levels.PresetGenerators presetGenerators = new com.bryjamin.wickedwizard.factories.arenas.levels.PresetGenerators();

        return new GameCreator(DEFAULT_GAME_ID,
                new GameCreator.LevelCreator(presetGenerators.level1Configuration(assetManager, random)),
                new GameCreator.LevelCreator(presetGenerators.level2Configuration(assetManager, random)),
                new GameCreator.LevelCreator(presetGenerators.level3Configuration(assetManager, random)),
                new GameCreator.LevelCreator(presetGenerators.level4Configuration(assetManager, random)),
                new GameCreator.LevelCreator(presetGenerators.level5Configuration(assetManager, random)));

    }







}
