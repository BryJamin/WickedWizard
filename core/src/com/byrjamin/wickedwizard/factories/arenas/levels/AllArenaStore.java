package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;

import java.util.Random;

/**
 * Created by Home on 11/07/2017.
 */

public class AllArenaStore extends AbstractFactory {


    private ArenaSkin arenaSkin;

    private Array<ArenaRepostiory> allLevels = new Array<ArenaRepostiory>();


    public AllArenaStore(AssetManager assetManager, ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        allLevels.add(new Level1Rooms(assetManager, arenaSkin, random));
        allLevels.add(new Level2Rooms(assetManager, arenaSkin, random));
        allLevels.add(new Level3Rooms(assetManager, arenaSkin, random));
        allLevels.add(new Level4Rooms(assetManager, arenaSkin, random));
        allLevels.add(new Level5Rooms(assetManager, arenaSkin, random));
    }


    public ArenaCreate getArenaGen(int level, int room) {
        try {
            return allLevels.get(level).getAllArenas().get(room);
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
            return allLevels.get(0).getAllArenas().get(0);
        }
    }


}

