package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

/**
 * Created by BB on 15/08/2017.
 */

public class GameCreator {


    public Queue<LevelCreator> gameLevels = new Queue<LevelCreator>();

    public GameCreator(){

    }


    public GameCreator(LevelCreator... levelCreators){

        for(LevelCreator l : levelCreators){
            gameLevels.addLast(l);
        }

    }
/*

    public void add(LevelCreator levelCreator){
        gameLevels.add(levelCreator);
    }
*/


    public static class LevelCreator {

        public JigsawGeneratorConfig jigsawGeneratorConfig;
        public boolean isGenerated;

        public LevelCreator(JigsawGeneratorConfig jigsawGeneratorConfig){
            this(jigsawGeneratorConfig, false);
        }

        public LevelCreator(JigsawGeneratorConfig jigsawGeneratorConfig, boolean isGenerated){
            this.jigsawGeneratorConfig = jigsawGeneratorConfig;
            this.isGenerated = isGenerated;
        }

    }




}
