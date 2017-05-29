package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.GiantKugelRoom;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.WandaRoom;
import com.byrjamin.wickedwizard.utils.MapCoords;

import java.util.Random;

/**
 * Created by Home on 28/05/2017.
 */

public class BossRoomSystem extends BaseSystem {

    private Random random;

    private GiantKugelRoom giantKugelRoom;
    private WandaRoom wandaRoom;


    private AssetManager assetManager;

    public BossRoomSystem(AssetManager assetManager, Random random){
        this.random = random;
        this.assetManager = assetManager;
    }



    public void createBoss(ChangeLevelSystem.Level level){


        switch (level) {
            case SOLITARY:
                new GiantKugelRoom(assetManager, level.getArenaSkin()).giantKugelArena().createArena(new MapCoords());
                giantKugelRoom.giantKugelArena().createArena(new MapCoords());
                break;
            case PRISON:
                new WandaRoom(assetManager, level.getArenaSkin()).wandaArena().createArena(new MapCoords());

                break;
            case FOUNDARY:
                new GiantKugelRoom(assetManager, level.getArenaSkin()).giantKugelArena().createArena(new MapCoords());
                break;
            case CBLOCK:
                new GiantKugelRoom(assetManager, level.getArenaSkin()).giantKugelArena().createArena(new MapCoords());
                break;
            case FREEDOMRUN:
                new GiantKugelRoom(assetManager, level.getArenaSkin()).giantKugelArena().createArena(new MapCoords());
                break;
        }





    }


    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }
}
