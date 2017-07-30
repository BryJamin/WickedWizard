package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.arenas.BossMapCreate;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;

import java.util.Random;

/**
 * Created by Home on 30/07/2017.
 */

public class PresetGenerators {

    private static final int numberOfLevel1Rooms = 5;
    private static final int numberOfLevel2Rooms = 8;
    private static final int numberOfLevel3Rooms = 10;
    private static final int numberOfLevel4Rooms = 12;
    private static final int numberOfLevel5Rooms = 15;



    public PresetGenerators(){

    }


    public JigsawGeneratorConfig level1Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).blobbaMapCreate());
        bossMapGens.add(new BossMaps(assetManager, arenaSkin).adojMapCreate());

        return new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                .arenaCreates(new Level1Rooms(assetManager, arenaSkin, random).getAllArenas())
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel1Rooms);

    }

    public JigsawGeneratorConfig level2Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).wandaMapCreate());
        bossMapGens.add(new BossMaps(assetManager, arenaSkin).giantSpinnerMapCreate());

        return new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                .arenaCreates(new Level2Rooms(assetManager, arenaSkin, random).getAllArenas())
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel2Rooms);

    }


    public JigsawGeneratorConfig level3Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).boomyMapCreate());
        bossMapGens.add(new BossMaps(assetManager, arenaSkin).ajirMapCreate());

        return new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                .arenaCreates(new Level3Rooms(assetManager, arenaSkin, random).getAllArenas())
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel3Rooms);
    }



    public JigsawGeneratorConfig level4Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).amalgamaMapCreate());
        bossMapGens.add(new BossMaps(assetManager, arenaSkin).wraithMapCreate());

        return new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                .arenaCreates(new Level4Rooms(assetManager, arenaSkin, random).getAllArenas())
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel4Rooms);

    }


    public JigsawGeneratorConfig level5Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).endMapCreate());

        return new JigsawGeneratorConfig(assetManager, arenaSkin, random)
                .arenaCreates(new Level5Rooms(assetManager, arenaSkin, random).getAllArenas())
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel5Rooms);

    }












}
