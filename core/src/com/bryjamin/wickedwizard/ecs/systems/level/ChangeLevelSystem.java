package com.bryjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.bryjamin.wickedwizard.factories.arenas.GameCreator;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.bryjamin.wickedwizard.utils.enums.Level;

/**
 * Created by Home on 29/04/2017.
 */

public class ChangeLevelSystem extends BaseSystem {

    private JigsawGenerator jigsawGenerator;

    private GameCreator gameCreator;

    private Level level;

    public ChangeLevelSystem(GameCreator gameCreator, JigsawGenerator jigsawGenerator){
        this.gameCreator = gameCreator;
        this.jigsawGenerator = jigsawGenerator;
        this.level = jigsawGenerator.getLevel();
    }


    public void setLevel(Level level) {
        this.level = level;
    }

    public void setLevel(String level) {
        for(Level l : Level.values()){
            if(l.name().equals(level)) this.level = l;
        }
    }

    public JigsawGenerator incrementLevel(){


        if(gameCreator.isFinalLevel()) {
            world.getSystem(GameSystem.class).backToMenu();
            return null;
        }

        GameCreator.LevelCreator levelCreator = gameCreator.getNextLevel();
        JigsawGeneratorConfig jigsawGeneratorConfig = levelCreator.jigsawGeneratorConfig;
        level = jigsawGeneratorConfig.level;

        jigsawGenerator = jigsawGeneratorConfig.itemStore(jigsawGenerator.getItemStore())
                .build();

        if(levelCreator.isGenerated){
            jigsawGenerator.generate();
        } else {
            jigsawGenerator.cleanArenas();
        }

        return jigsawGenerator;
    }

    public Level getLevel() {
        return level;
    }

    public GameCreator getGameCreator() {
        return gameCreator;
    }

    public JigsawGenerator getJigsawGenerator() {
        return jigsawGenerator;
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }
}
