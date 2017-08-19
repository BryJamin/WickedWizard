package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.factories.arenas.BossMapCreate;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGeneratorConfig;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.enums.Level;

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


    public JigsawGeneratorConfig level1Configuration(AssetManager assetManager, Random random) {
        return level1Configuration(assetManager, Level.ONE.getArenaSkin(), random);
    }

    public JigsawGeneratorConfig level1Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).blobbaMapCreate());
        bossMapGens.add(new BossMaps(assetManager, arenaSkin).adojMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new Level1Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new StartingRooms(assetManager, arenaSkin).startingArena(Level.ONE).createArena(new MapCoords())))
                .level(Level.ONE)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel1Rooms);

    }

    public JigsawGeneratorConfig level2Configuration(AssetManager assetManager, Random random) {
        return level2Configuration(assetManager, Level.TWO.getArenaSkin(), random);
    }


    public JigsawGeneratorConfig level2Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).wandaMapCreate());
        bossMapGens.add(new BossMaps(assetManager, arenaSkin).giantSpinnerMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new Level2Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new StartingRooms(assetManager, arenaSkin).startingArena(Level.TWO).createArena(new MapCoords())))
                .level(Level.TWO)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel2Rooms);

    }

    public JigsawGeneratorConfig level3Configuration(AssetManager assetManager, Random random) {
        return level3Configuration(assetManager, Level.THREE.getArenaSkin(), random);
    }


    public JigsawGeneratorConfig level3Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).boomyMapCreate());
        bossMapGens.add(new BossMaps(assetManager, arenaSkin).ajirMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new Level3Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new StartingRooms(assetManager, arenaSkin).startingArena(Level.THREE).createArena(new MapCoords())))
                .level(Level.THREE)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel3Rooms);
    }

    public JigsawGeneratorConfig level4Configuration(AssetManager assetManager, Random random) {
        return level4Configuration(assetManager, Level.FOUR.getArenaSkin(), random);
    }


    public JigsawGeneratorConfig level4Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).amalgamaMapCreate());
        bossMapGens.add(new BossMaps(assetManager, arenaSkin).wraithMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new Level4Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new StartingRooms(assetManager, arenaSkin).startingArena(Level.FOUR).createArena(new MapCoords())))
                .level(Level.FOUR)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel4Rooms);

    }

    public JigsawGeneratorConfig level5Configuration(AssetManager assetManager, Random random) {
        return level5Configuration(assetManager, Level.FIVE.getArenaSkin(), random);
    }


    public JigsawGeneratorConfig level5Configuration(AssetManager assetManager, ArenaSkin arenaSkin, Random random){

        Array<BossMapCreate> bossMapGens = new Array<BossMapCreate>();

        bossMapGens.add(new BossMaps(assetManager, arenaSkin).endMapCreate());

        return new JigsawGeneratorConfig(assetManager, random)
                .arenaCreates(new Level5Rooms(assetManager, arenaSkin, random).getAllArenas())
                .startingMap(new ArenaMap(new StartingRooms(assetManager, arenaSkin).startingArena(Level.FIVE).createArena(new MapCoords())))
                .level(Level.FIVE)
                .bossMapCreates(bossMapGens)
                .noBattleRooms(numberOfLevel5Rooms);

    }












}
