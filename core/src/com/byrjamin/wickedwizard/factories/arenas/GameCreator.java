package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.byrjamin.wickedwizard.utils.enums.Level;

/**
 * Created by BB on 15/08/2017.
 */

public class GameCreator {


    public Queue<LevelCreator> gameLevels = new Queue<LevelCreator>();
    public int position = 0;

    public GameCreator(LevelCreator... levelCreators){
        for(LevelCreator l : levelCreators){
            gameLevels.addLast(l);
        }
    }


    public LevelCreator getNextLevel(){
        position++;
        if(position > gameLevels.size) position = gameLevels.size - 1;
        return gameLevels.get(position);
    }

    public LevelCreator getCurrentLevel(){
        return gameLevels.get(position);
    }

    public LevelCreator getAndSetLevel(int position){
        if(position < 0) position = 0;
        if(position > gameLevels.size) position = gameLevels.size - 1;
        this.position = position;
        return gameLevels.get(position);
    }

    public void add(LevelCreator levelCreator){
        gameLevels.addLast(levelCreator);
    }


    public static class LevelCreator {

        public JigsawGeneratorConfig jigsawGeneratorConfig;
        public boolean isGenerated;

        public LevelCreator(JigsawGeneratorConfig jigsawGeneratorConfig){
            this(jigsawGeneratorConfig, true);
        }

        public LevelCreator(JigsawGeneratorConfig jigsawGeneratorConfig, boolean isGenerated){
            this.jigsawGeneratorConfig = jigsawGeneratorConfig;
            this.isGenerated = isGenerated;
        }

    }




}
