package com.bryjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;

/**
 * Created by Home on 29/04/2017.
 */

public class ChangeLevelSystem extends BaseSystem {

    private JigsawGenerator jigsawGenerator;

    private com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator;

    private com.bryjamin.wickedwizard.utils.enums.Level level;

    public ChangeLevelSystem(com.bryjamin.wickedwizard.factories.arenas.GameCreator gameCreator, JigsawGenerator jigsawGenerator){
        this.gameCreator = gameCreator;
        this.jigsawGenerator = jigsawGenerator;
        this.level = jigsawGenerator.getLevel();
    }


    public void setLevel(com.bryjamin.wickedwizard.utils.enums.Level level) {
        this.level = level;
    }

    public void setLevel(String level) {
        for(com.bryjamin.wickedwizard.utils.enums.Level l : com.bryjamin.wickedwizard.utils.enums.Level.values()){
            if(l.name().equals(level)) this.level = l;
        }
    }

    public JigsawGenerator incrementLevel(){


        if(gameCreator.isFinalLevel()) {
            world.getSystem(EndGameSystem.class).backToMenu();
            return null;
        }

        com.bryjamin.wickedwizard.factories.arenas.GameCreator.LevelCreator levelCreator = gameCreator.getNextLevel();
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

    public com.bryjamin.wickedwizard.utils.enums.Level getLevel() {
        return level;
    }

    public com.bryjamin.wickedwizard.factories.arenas.GameCreator getGameCreator() {
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
