package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by Home on 11/07/2017.
 */

public class AllArenaStore extends AbstractFactory {


    private ArenaSkin arenaSkin;

    public Array<ArenaRepostiory> allLevels = new Array<ArenaRepostiory>();

    public ArenaCreate blank;


    public AllArenaStore(final AssetManager assetManager, final ArenaSkin arenaSkin, Random random) {
        super(assetManager);
        allLevels.add(new Level1Rooms(assetManager, Level.ONE.getArenaSkin(), random));
        allLevels.add(new Level2Rooms(assetManager, random));
        allLevels.add(new Level3Rooms(assetManager, Level.THREE.getArenaSkin(), random));
        allLevels.add(new Level4Rooms(assetManager, Level.FOUR.getArenaSkin(), random));
        allLevels.add(new Level5Rooms(assetManager, Level.FIVE.getArenaSkin(), random));
        this.blank = new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {
                return new ArenaShellFactory(assetManager, arenaSkin).createOmniArenaHiddenGrapple(new MapCoords());
            }
        };


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

