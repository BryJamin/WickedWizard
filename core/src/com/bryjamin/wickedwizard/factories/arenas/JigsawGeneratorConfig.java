package com.bryjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by Home on 10/07/2017.
 */

public class JigsawGeneratorConfig {

    public int noBattleRooms = 0;

    public final Random random;
    public com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap startingMap;

    public final AssetManager assetManager;

    public com.bryjamin.wickedwizard.utils.enums.Level level = com.bryjamin.wickedwizard.utils.enums.Level.ONE;


    public com.bryjamin.wickedwizard.factories.items.ItemStore itemStore;

    public Array<ArenaCreate> arenaGens;
    public Array<BossMapCreate> bossMapGens;


    public JigsawGeneratorConfig(AssetManager assetManager, Random random){
        this.assetManager = assetManager;
        this.random = random;
        this.startingMap = new com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap(new com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, level.getArenaSkin()).createOmniArenaHiddenGrapple(new com.bryjamin.wickedwizard.utils.MapCoords(), Arena.ArenaType.NORMAL));
        this.arenaGens = new com.bryjamin.wickedwizard.factories.arenas.levels.Level1Rooms(assetManager, level.getArenaSkin(), random).getLevel1RoomArray();
        this.itemStore = new com.bryjamin.wickedwizard.factories.items.ItemStore(random);
        bossMapGens = new Array<BossMapCreate>();
        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, level.getArenaSkin()).blobbaMapCreate());
        bossMapGens.add(new com.bryjamin.wickedwizard.factories.arenas.presetmaps.BossMaps(assetManager, level.getArenaSkin()).adojMapCreate());

    }


    public JigsawGeneratorConfig noBattleRooms(int val)
    { noBattleRooms = val; return this; }

    public JigsawGeneratorConfig startingMap(com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap val)
    { startingMap = val; return this; }


    public JigsawGeneratorConfig startArena(Arena val)
    { startingMap.setCurrentArena(val); return this; }


    public JigsawGeneratorConfig level(com.bryjamin.wickedwizard.utils.enums.Level val)
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
