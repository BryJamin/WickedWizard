package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.CBlockSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.FoundarySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.FreedomSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.PrisonSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;

import static com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.Level.FOUR;
import static com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.Level.TWO;
import static com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.Level.FIVE;
import static com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.Level.THREE;
import static com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem.Level.ONE;

/**
 * Created by Home on 29/04/2017.
 */

public class ChangeLevelSystem extends BaseSystem {


    private int currentLevel = 1;


    public enum Level {
        ONE, TWO, THREE, FOUR, FIVE;

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
        ONE.setArenaSkin(new SolitarySkin(atlas));
        TWO.setArenaSkin(new FoundarySkin(atlas));
        THREE.setArenaSkin(new PrisonSkin(atlas));
        FOUR.setArenaSkin(new CBlockSkin(atlas));
        FIVE.setArenaSkin(new FreedomSkin(atlas));
        this.jigsawGenerator = jigsawGenerator;

        level = ONE;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public JigsawGenerator incrementLevel(){

        switch (level) {
            case ONE: level = TWO;
                jigsawGenerator.setNoBattleRooms(8);
                world.getSystem(MessageBannerSystem.class).createBanner("Chapter 2", "");
                break;
            case TWO: level = THREE;
                jigsawGenerator.setNoBattleRooms(10);
                world.getSystem(MessageBannerSystem.class).createBanner("Chapter 3", "");
                break;
            case THREE: level = FOUR;
                jigsawGenerator.setNoBattleRooms(12);
                world.getSystem(MessageBannerSystem.class).createBanner("Chapter 4", "");
                break;
            case FOUR: level = FIVE;
                jigsawGenerator.setNoBattleRooms(14);
                world.getSystem(MessageBannerSystem.class).createBanner("Chapter 5", "");
                break;
            case FIVE:
                //TODO world.endGame
                break;
        }

        jigsawGenerator.setSkin(level.getArenaSkin());
        jigsawGenerator.setCurrentLevel(level);

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
