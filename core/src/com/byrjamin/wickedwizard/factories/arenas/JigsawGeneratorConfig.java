package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.ChangeLevelSystem;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level1Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level2Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level3Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level4Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.Level5Rooms;
import com.byrjamin.wickedwizard.factories.arenas.levels.TutorialFactory;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.BossMaps;
import com.byrjamin.wickedwizard.factories.arenas.presets.ItemArenaFactory;
import com.byrjamin.wickedwizard.factories.arenas.presets.ShopFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;
import com.byrjamin.wickedwizard.utils.MapCoords;

import java.io.File;
import java.util.Random;

/**
 * Created by Home on 10/07/2017.
 */

public class JigsawGeneratorConfig {

    public int noBattleRooms = 0;

    public final Random random;
    public ArenaMap startingMap;

    public final AssetManager assetManager;
    public ArenaSkin arenaSkin;

    public ChangeLevelSystem.Level currentLevel = ChangeLevelSystem.Level.ONE;


    public JigsawGeneratorConfig(AssetManager assetManager, Random random){
        this.assetManager = assetManager;
        this.random = random;
        this.arenaSkin = new SolitarySkin(assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class));
        this.startingMap = new ArenaMap(new Arena(arenaSkin, new MapCoords()));
    }

    public JigsawGeneratorConfig noBattleRooms(int val)
    { noBattleRooms = val; return this; }

    public JigsawGeneratorConfig arenaSkin(ArenaSkin val)
    { arenaSkin = val; return this; }

    public JigsawGeneratorConfig startingMap(ArenaMap val)
    { startingMap = val; return this; }

    public JigsawGeneratorConfig currentLevel(ChangeLevelSystem.Level val)
    { currentLevel = val; return this; }


    public JigsawGenerator build(){
        return new JigsawGenerator(this);
    }




}
