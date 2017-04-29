package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.FoundarySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;

import static com.byrjamin.wickedwizard.ecs.systems.ChangeLevelSystem.Level.FOUNDARY;
import static com.byrjamin.wickedwizard.ecs.systems.ChangeLevelSystem.Level.PRISON;
import static com.byrjamin.wickedwizard.ecs.systems.ChangeLevelSystem.Level.SOLITARY;

/**
 * Created by Home on 29/04/2017.
 */

public class ChangeLevelSystem extends BaseSystem {


    private int currentLevel = 1;


    public enum Level {
        SOLITARY, FOUNDARY, PRISON, CBLOCK, FREEDOMRUN;

        private ArenaSkin arenaSkin;

        public void setArenaSkin(ArenaSkin arenaSkin){
            this.arenaSkin = arenaSkin;
        }

        public ArenaSkin getArenaSkin(){
            return  arenaSkin;
        }

    }

    private Level level;


    private FoundarySkin foundarySkin;
    private SolitarySkin solitarySkin;
    private JigsawGenerator jigsawGenerator;

    public ChangeLevelSystem(JigsawGenerator jigsawGenerator, TextureAtlas atlas){
        this.foundarySkin = new FoundarySkin(atlas);
        this.solitarySkin = new SolitarySkin(atlas);

        SOLITARY.setArenaSkin(solitarySkin);
        FOUNDARY.setArenaSkin(foundarySkin);
        this.jigsawGenerator = jigsawGenerator;

        level = SOLITARY;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public JigsawGenerator incrementLevel(){

        System.out.println(level);

        switch (level) {
            case SOLITARY: level = FOUNDARY;
                jigsawGenerator.setNoBattleRooms(13);
                jigsawGenerator.setSkin(foundarySkin);
                break;
            case FOUNDARY: level = PRISON;
                jigsawGenerator.setNoBattleRooms(15);
                jigsawGenerator.setSkin(solitarySkin);
                break;
        }

        System.out.println(level);

        return jigsawGenerator;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public ArenaSkin getCurrentLevelSkin(){
        return level.getArenaSkin();
    }



    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }
}
