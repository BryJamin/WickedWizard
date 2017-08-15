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

    private AssetManager assetManager;
    private Random random;

    private JigsawGenerator jigsawGenerator;

    private GameCreator gameCreator;

    private Level level;

    public ChangeLevelSystem(GameCreator gameCreator, AssetManager assetManager, JigsawGenerator jigsawGenerator, Random random){
        this.gameCreator = gameCreator;
        this.assetManager = assetManager;
        this.random = random;
        this.jigsawGenerator = jigsawGenerator;
        level = Level.ONE;
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


        jigsawGenerator = gameCreator.gameLevels.removeFirst().jigsawGeneratorConfig
                .itemStore(jigsawGenerator.getItemStore())
                .build();

/*        switch (level) {
            case ONE:
            default:
                level = Level.TWO;
                jigsawGenerator = getJigsawGenerator(Level.TWO);
                world.getSystem(MessageBannerSystem.class).createLevelBanner("Chapter 2");
                break;
            case TWO: level = Level.THREE;
                jigsawGenerator = getJigsawGenerator(Level.THREE);
                world.getSystem(MessageBannerSystem.class).createLevelBanner("Chapter 3");
                break;
            case THREE: level = Level.FOUR;
                jigsawGenerator = getJigsawGenerator(Level.FOUR);
                world.getSystem(MessageBannerSystem.class).createLevelBanner("Chapter 4");
                break;
            case FOUR: level = Level.FIVE;
                jigsawGenerator = getJigsawGenerator(Level.FIVE);
                world.getSystem(MessageBannerSystem.class).createLevelBanner("Chapter 5");
                break;
            case FIVE:
                jigsawGenerator = getJigsawGenerator(Level.ONE);
                //TODO world.endGame
                break;
        }*/


        world.getSystem(MusicSystem.class).playLevelMusic(level);

        return jigsawGenerator;
    }

    public JigsawGenerator getJigsawGenerator(Level currentLevel){


        JigsawGenerator jg;
        ArenaSkin arenaSkin = currentLevel.getArenaSkin();

        switch(currentLevel){
            case ONE:
            default:

                jg = new PresetGenerators().level1Configuration(assetManager, arenaSkin, random)
                        .itemStore(jigsawGenerator.getItemStore())
                        .build();

                break;
            case TWO:

                jg = new PresetGenerators().level2Configuration(assetManager, arenaSkin, random)
                        .itemStore(jigsawGenerator.getItemStore())
                        .build();

                break;
            case THREE:

                jg = new PresetGenerators().level3Configuration(assetManager, arenaSkin, random)
                        .itemStore(jigsawGenerator.getItemStore())
                        .build();

                break;
            case FOUR:

                jg = new PresetGenerators().level4Configuration(assetManager, arenaSkin, random)
                        .itemStore(jigsawGenerator.getItemStore())
                        .build();

                break;
            case FIVE:

                jg = new PresetGenerators().level5Configuration(assetManager, arenaSkin, random)
                        .itemStore(jigsawGenerator.getItemStore())
                        .build();

                break;
        }


        return jg;
    }

    public Level getLevel() {
        return level;
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
