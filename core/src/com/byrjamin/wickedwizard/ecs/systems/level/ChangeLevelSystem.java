package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.ecs.systems.audio.MusicSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.factories.arenas.BossMapCreate;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level1Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level2Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level3Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level4Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level5Rooms;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.Bourbon;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkPurpleAndBrown;
import com.byrjamin.wickedwizard.factories.arenas.skins.FoundarySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.DarkGraySkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.LightGraySkin;

import java.util.Random;

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

    private static final int numberOfLevel1Rooms = 1;
    private static final int numberOfLevel2Rooms = 8;
    private static final int numberOfLevel3Rooms = 10;
    private static final int numberOfLevel4Rooms = 12;
    private static final int numberOfLevel5Rooms = 15;



    private AssetManager assetManager;
    private TextureAtlas atlas;
    private Random random;

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

    public ChangeLevelSystem(AssetManager assetManager, Random random){
        this.assetManager = assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
        ONE.setArenaSkin(new LightGraySkin(atlas));
        TWO.setArenaSkin(new FoundarySkin(atlas));
        THREE.setArenaSkin(new DarkPurpleAndBrown(atlas));
        FOUR.setArenaSkin(new Bourbon(atlas));
        FIVE.setArenaSkin(new DarkGraySkin(atlas));
        this.random = random;
        level = ONE;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public JigsawGenerator incrementLevel(){

        JigsawGenerator jigsawGenerator;

        switch (level) {
            case ONE:
            default:
                level = TWO;
                jigsawGenerator = getJigsawGenerator(TWO);
                world.getSystem(MessageBannerSystem.class).createItemBanner("Chapter 2", "");
                break;
            case TWO: level = THREE;
                jigsawGenerator = getJigsawGenerator(THREE);
                world.getSystem(MessageBannerSystem.class).createItemBanner("Chapter 3", "");
                break;
            case THREE: level = FOUR;
                jigsawGenerator = getJigsawGenerator(FOUR);
                world.getSystem(MessageBannerSystem.class).createItemBanner("Chapter 4", "");
                break;
            case FOUR: level = FIVE;
                jigsawGenerator = getJigsawGenerator(FIVE);
                world.getSystem(MessageBannerSystem.class).createItemBanner("Chapter 5", "");
                break;
            case FIVE:
                jigsawGenerator = getJigsawGenerator(ONE);
                //TODO world.endGame
                break;
        }


        world.getSystem(MusicSystem.class).playLevelMusic(level);

        return jigsawGenerator;
    }

    public JigsawGenerator getJigsawGenerator(ChangeLevelSystem.Level currentLevel){


        JigsawGenerator jg;
        ArenaSkin arenaSkin = currentLevel.arenaSkin;

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        switch(currentLevel){
            case ONE:
            default:

                bossMapGens.add(new BossMaps(assetManager, arenaSkin).blobbaMapCreate());
                bossMapGens.add(new BossMaps(assetManager, arenaSkin).adojMapCreate());

                jg = new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                        .arenaCreates(new Level1Rooms(assetManager, arenaSkin, random).getAllArenas())
                        .bossMapCreates(bossMapGens)
                        .noBattleRooms(numberOfLevel1Rooms)
                        .build(); //level3Rooms.getLevel3RoomArray(); //arenaGens = level2Rooms.getLevel2RoomArray();
                break;
            case TWO:

                bossMapGens.add(new BossMaps(assetManager, arenaSkin).wandaMapCreate());
                bossMapGens.add(new BossMaps(assetManager, arenaSkin).giantSpinnerMapCreate());

                jg = new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                        .arenaCreates(new Level2Rooms(assetManager, arenaSkin, random).getAllArenas())
                        .bossMapCreates(bossMapGens)
                        .noBattleRooms(numberOfLevel2Rooms)
                        .build();

                break;
            case THREE:

                bossMapGens.add(new BossMaps(assetManager, arenaSkin).boomyMapCreate());
                bossMapGens.add(new BossMaps(assetManager, arenaSkin).ajirMapCreate());

                jg = new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                        .arenaCreates(new Level3Rooms(assetManager, arenaSkin, random).getAllArenas())
                        .bossMapCreates(bossMapGens)
                        .noBattleRooms(numberOfLevel3Rooms)
                        .build();

                break;
            case FOUR:

                bossMapGens.add(new BossMaps(assetManager, arenaSkin).amalgamaMapCreate());
                bossMapGens.add(new BossMaps(assetManager, arenaSkin).wraithMapCreate());

                jg = new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                        .arenaCreates(new Level4Rooms(assetManager, arenaSkin, random).getAllArenas())
                        .bossMapCreates(bossMapGens)
                        .noBattleRooms(numberOfLevel4Rooms)
                        .build();

                break;
            case FIVE:

                bossMapGens.add(new BossMaps(assetManager, arenaSkin).endMapCreate());

                jg = new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                        .arenaCreates(new Level5Rooms(assetManager, arenaSkin, random).getAllArenas())
                        .bossMapCreates(bossMapGens)
                        .noBattleRooms(numberOfLevel5Rooms)
                        .build();

                break;
        }


        return jg;
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
