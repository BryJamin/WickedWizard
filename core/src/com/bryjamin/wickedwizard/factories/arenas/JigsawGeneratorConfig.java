package com.bryjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.bryjamin.wickedwizard.factories.arenas.levels.Level1Rooms;
import com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.bryjamin.wickedwizard.factories.items.ItemStore;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

;

/**
 * Created by Home on 10/07/2017.
 */

public class JigsawGeneratorConfig {

    public int noBattleRooms = 0;


    public int noRandomizerRooms = 1;


    public final Random random;
    public final AssetManager assetManager;

    public Level level = Level.ONE;


    public ItemStore itemStore;
    public ArenaMap startingMap;
    public Array<ArenaCreate> arenaGens;
    public Array<BossMapCreate> bossMapGens;


    public JigsawGeneratorConfig(AssetManager assetManager, Random random){
        this.assetManager = assetManager;
        this.random = random;
        this.startingMap = new ArenaMap(new ArenaShellFactory(assetManager, level.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(), Arena.ArenaType.NORMAL));
        this.arenaGens = new Level1Rooms(assetManager, level.getArenaSkin(), random).getLevel1RoomArray();
        this.itemStore = new com.bryjamin.wickedwizard.factories.items.ItemStore(random);
        bossMapGens = new Array<BossMapCreate>();
        bossMapGens.add(new BossMaps(assetManager, level.getArenaSkin()).blobbaMapCreate());
        bossMapGens.add(new BossMaps(assetManager, level.getArenaSkin()).adojMapCreate());

    }


    public JigsawGeneratorConfig noBattleRooms(int val)
    { noBattleRooms = val; return this; }

    public JigsawGeneratorConfig noRandomizerRooms(int val)
    { noRandomizerRooms = val; return this; }

    public JigsawGeneratorConfig startingMap(com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap val)
    { startingMap = val; return this; }


    public JigsawGeneratorConfig startArena(Arena val)
    { startingMap.setCurrentArena(val); return this; }


    public JigsawGeneratorConfig level(Level val)
    { level = val; return this; }

    public JigsawGeneratorConfig itemStore(com.bryjamin.wickedwizard.factories.items.ItemStore val)
    { itemStore = val; return this; }

    public JigsawGeneratorConfig arenaCreates(Array<ArenaCreate> val)
    { arenaGens = val; return this; }

    public JigsawGeneratorConfig bossMapCreates(Array<BossMapCreate> val)
    { bossMapGens = val; return this; }




    public JigsawGenerator build(){
        return new JigsawGenerator(this);
    }




}
