package com.bryjamin.wickedwizard.factories.arenas.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;

import java.util.Random;

/**
 * Created by Home on 30/07/2017.
 */

public class PresetGenerators {

    private static final int numberOfLevel1Rooms = 5;
    //private static final int numberOfLevel1Rooms = 5;
    private static final int numberOfLevel2Rooms = 8;
    private static final int numberOfLevel3Rooms = 10;
    private static final int numberOfLevel4Rooms = 12;
    private static final int numberOfLevel5Rooms = 15;



    public PresetGenerators(){

    }


    public JigsawGeneratorConfig level1Configuration(AssetManager assetManager, Random random) {
        return level1Configuration(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.ONE.getArenaSkin(), random);
    }

    public JigsawGeneratorConfig level1Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate> bossMapGens = new Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate>();

        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, arenaSkin).blobbaMapCreate());
        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, arenaSkin).adojMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new Level1Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new ReuseableRooms(assetManager, arenaSkin).startingArena(com.bryjamin.wickedwizard.utils.enums.Level.ONE).createArena(new com.bryjamin.wickedwizard.utils.MapCoords())))
                .level(com.bryjamin.wickedwizard.utils.enums.Level.ONE)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel1Rooms);

    }

    public JigsawGeneratorConfig level2Configuration(AssetManager assetManager, Random random) {
        return level2Configuration(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.TWO.getArenaSkin(), random);
    }


    public JigsawGeneratorConfig level2Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate> bossMapGens = new Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate>();

        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, arenaSkin).wandaMapCreate());
        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, arenaSkin).giantSpinnerMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new Level2Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new ReuseableRooms(assetManager, arenaSkin).startingArena(com.bryjamin.wickedwizard.utils.enums.Level.TWO).createArena(new com.bryjamin.wickedwizard.utils.MapCoords())))
                .level(com.bryjamin.wickedwizard.utils.enums.Level.TWO)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel2Rooms);

    }

    public JigsawGeneratorConfig level3Configuration(AssetManager assetManager, Random random) {
        return level3Configuration(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.THREE.getArenaSkin(), random);
    }


    public JigsawGeneratorConfig level3Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate> bossMapGens = new Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate>();

        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, arenaSkin).boomyMapCreate());
        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, arenaSkin).ajirMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new Level3Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new ReuseableRooms(assetManager, arenaSkin).startingArena(com.bryjamin.wickedwizard.utils.enums.Level.THREE).createArena(new com.bryjamin.wickedwizard.utils.MapCoords())))
                .level(com.bryjamin.wickedwizard.utils.enums.Level.THREE)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel3Rooms);
    }

    public JigsawGeneratorConfig level4Configuration(AssetManager assetManager, Random random) {
        return level4Configuration(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FOUR.getArenaSkin(), random);
    }


    public JigsawGeneratorConfig level4Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate> bossMapGens = new Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate>();

        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, arenaSkin).amalgamaMapCreate());
        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, arenaSkin).wraithMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new Level4Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new ReuseableRooms(assetManager, arenaSkin).startingArena(com.bryjamin.wickedwizard.utils.enums.Level.FOUR).createArena(new com.bryjamin.wickedwizard.utils.MapCoords())))
                .level(com.bryjamin.wickedwizard.utils.enums.Level.FOUR)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel4Rooms);

    }

    public JigsawGeneratorConfig level5Configuration(AssetManager assetManager, Random random) {
        return level5Configuration(assetManager, com.bryjamin.wickedwizard.utils.enums.Level.FIVE.getArenaSkin(), random);
    }


    public JigsawGeneratorConfig level5Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate> bossMapGens = new Array<com.bryjamin.wickedwizard.factories.arenas.BossMapCreate>();

        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, arenaSkin).endMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new com.bryjamin.wickedwizard.factories.arenas.levels.Level5Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new ReuseableRooms(assetManager, arenaSkin).startingArena(com.bryjamin.wickedwizard.utils.enums.Level.FIVE).createArena(new com.bryjamin.wickedwizard.utils.MapCoords())))
                .level(com.bryjamin.wickedwizard.utils.enums.Level.FIVE)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel5Rooms);

    }












}
