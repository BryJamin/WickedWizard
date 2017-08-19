package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.factories.arenas.BossMapCreate;
import com.byrjamin.wickedwizard.factories.arenas.GameCreator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level1Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level2Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level3Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level4Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level5Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.PresetGenerators;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.Bourbon;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkPurpleAndBrown;
import com.byrjamin.wickedwizard.factories.arenas.skins.FoundarySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkGraySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.LightGraySkin;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

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
            world.getSystem(EndGameSystem.class).backToMenu();
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
