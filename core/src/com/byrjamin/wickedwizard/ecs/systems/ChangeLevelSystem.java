package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.CBlockSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.FoundarySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.FreedomSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.PrisonSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;

import static com.byrjamin.wickedwizard.ecs.systems.ChangeLevelSystem.Level.CBLOCK;
import static com.byrjamin.wickedwizard.ecs.systems.ChangeLevelSystem.Level.FOUNDARY;
import static com.byrjamin.wickedwizard.ecs.systems.ChangeLevelSystem.Level.FREEDOMRUN;
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


    private JigsawGenerator jigsawGenerator;

    public ChangeLevelSystem(JigsawGenerator jigsawGenerator, TextureAtlas atlas){
        SOLITARY.setArenaSkin(new SolitarySkin(atlas));
        FOUNDARY.setArenaSkin(new FoundarySkin(atlas));
        PRISON.setArenaSkin(new PrisonSkin(atlas));
        CBLOCK.setArenaSkin(new CBlockSkin(atlas));
        FREEDOMRUN.setArenaSkin(new FreedomSkin(atlas));
        this.jigsawGenerator = jigsawGenerator;

        level = SOLITARY;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public JigsawGenerator incrementLevel(){

        //Current level sequence. SOLITARY -> PRISON -> FOUNDARY -> CBLOCK -> FREEDOM

        switch (level) {
            case SOLITARY: level = PRISON;
                jigsawGenerator.setNoBattleRooms(13);
                break;
            case PRISON: level = FOUNDARY;
                jigsawGenerator.setNoBattleRooms(15);
                break;
            case FOUNDARY: level = CBLOCK;
                jigsawGenerator.setNoBattleRooms(15);
                break;
            case CBLOCK: level = FREEDOMRUN;
                jigsawGenerator.setNoBattleRooms(18);
                break;
            case FREEDOMRUN:
                //TODO world.endGame
                break;
        }

        jigsawGenerator.setSkin(level.getArenaSkin());

        System.out.println(level);

        return jigsawGenerator;
    }

    public Level getLevel() {
        return level;
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
