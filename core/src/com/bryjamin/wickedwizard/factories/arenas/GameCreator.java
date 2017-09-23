package com.bryjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Queue;

/**
 * Created by BB on 15/08/2017.
 */

public class GameCreator {


    //public String id = "0";

    public enum GameType {
        ADVENTURE, CHALLENGE, TUTORIAL
    }

    private GameType gameType;

    public Queue<LevelCreator> gameLevels = new Queue<LevelCreator>();
    public int position = 0;


    public GameCreator(GameType gameType, LevelCreator... levelCreators){
        for(LevelCreator l : levelCreators){
            gameLevels.addLast(l);
        }
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public LevelCreator getNextLevel(){
        position++;
        if(position >= gameLevels.size) position = gameLevels.size - 1;
        return gameLevels.get(position);
    }

    public boolean isFinalLevel(){
        return position + 1 >= gameLevels.size;
    }

    public LevelCreator getCurrentLevel(){
        return gameLevels.get(position);
    }

    public void setCurrentLevel(int position) {
        if(position <= 0) position = 0;
        if(position >= gameLevels.size) position = gameLevels.size - 1;
        this.position = position;
    }


    public int getPosition() {
        return position;
    }

    public LevelCreator getAndSetLevel(int position){
        if(position <= 0) position = 0;
        if(position >= gameLevels.size) position = gameLevels.size - 1;
        this.position = position;
        return gameLevels.get(position);
    }

    public void add(LevelCreator levelCreator){
        gameLevels.addLast(levelCreator);
    }


    public static class LevelCreator {

        public String id = "unidentified";
        public JigsawGeneratorConfig jigsawGeneratorConfig;
        public boolean isGenerated;

        public LevelCreator(JigsawGeneratorConfig jigsawGeneratorConfig){
            this(jigsawGeneratorConfig, true);
        }


        public LevelCreator(JigsawGeneratorConfig jigsawGeneratorConfig, String id){
            this(jigsawGeneratorConfig, id, true);
        }

        public LevelCreator(JigsawGeneratorConfig jigsawGeneratorConfig, boolean isGenerated){
            this.jigsawGeneratorConfig = jigsawGeneratorConfig;
            this.isGenerated = isGenerated;
        }

        public LevelCreator(JigsawGeneratorConfig jigsawGeneratorConfig, String id, boolean isGenerated){
            this.jigsawGeneratorConfig = jigsawGeneratorConfig;
            this.id = id;
            this.isGenerated = isGenerated;
        }

    }




}
