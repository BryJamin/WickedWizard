package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.factories.arenas.levels.PresetGenerators;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by BB on 15/08/2017.
 */

public class PresetGames {


    public static GameCreator DEFAULT_GAME(AssetManager assetManager, Random random){

        PresetGenerators presetGenerators = new PresetGenerators();

        return new GameCreator(
                new GameCreator.LevelCreator(presetGenerators.level1Configuration(assetManager, random)),
                new GameCreator.LevelCreator(presetGenerators.level2Configuration(assetManager, random)),
                new GameCreator.LevelCreator(presetGenerators.level3Configuration(assetManager, random)),
                new GameCreator.LevelCreator(presetGenerators.level4Configuration(assetManager, random)),
                new GameCreator.LevelCreator(presetGenerators.level5Configuration(assetManager, random)));

    }







}
