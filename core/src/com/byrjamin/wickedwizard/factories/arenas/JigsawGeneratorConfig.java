package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level1Rooms;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.LightGraySkin;
import com.byrjamin.wickedwizard.factories.items.ItemStore;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.enums.Level;

import java.util.Random;

/**
 * Created by Home on 10/07/2017.
 */

public class JigsawGeneratorConfig {

    public int noBattleRooms = 0;

    public final Random random;
    public ArenaMap startingMap;

    public final AssetManager assetManager;

    public Level level = Level.ONE;


    public ItemStore itemStore;

    public Array<ArenaCreate> arenaGens;
    public Array<BossMapCreate> bossMapGens;


    public JigsawGeneratorConfig(AssetManager assetManager, Random random){
        this.assetManager = assetManager;
        this.random = random;
        this.startingMap = new ArenaMap(new ArenaShellFactory(assetManager, level.getArenaSkin()).createOmniArenaHiddenGrapple(new MapCoords(), Arena.ArenaType.NORMAL));
        this.arenaGens = new Level1Rooms(assetManager, level.getArenaSkin(), random).getLevel1RoomArray();
        this.itemStore = new ItemStore(random);
        bossMapGens = new Array<BossMapCreate>();
        bossMapGens.add(new BossMaps(assetManager, level.getArenaSkin()).blobbaMapCreate());
        bossMapGens.add(new BossMaps(assetManager, level.getArenaSkin()).adojMapCreate());

    }


    public JigsawGeneratorConfig noBattleRooms(int val)
    { noBattleRooms = val; return this; }

    public JigsawGeneratorConfig startingMap(ArenaMap val)
    { startingMap = val; return this; }


    public JigsawGeneratorConfig startArena(Arena val)
    { startingMap.setCurrentArena(val); return this; }


    public JigsawGeneratorConfig level(Level val)
    { level = val; return this; }

    public JigsawGeneratorConfig itemStore(ItemStore val)
    { itemStore = val; return this; }

    public JigsawGeneratorConfig arenaCreates(Array<ArenaCreate> val)
    { arenaGens = val; return this; }

    public JigsawGeneratorConfig bossMapCreates(Array<BossMapCreate> val)
    { bossMapGens = val; return this; }




    public JigsawGenerator build(){
        return new JigsawGenerator(this);
    }




}
