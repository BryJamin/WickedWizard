package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.GiantKugelRoom;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.WandaRoom;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Home on 28/05/2017.
 */

public class MapTeleportationSystem extends EntitySystem {

    private Random random;

    private GiantKugelRoom giantKugelRoom;
    private WandaRoom wandaRoom;


    private AssetManager assetManager;

    private HashMap<BossTeleporterComponent, ArenaMap> mapTracker;

    private boolean processingFlag = false;

    private ComponentMapper<BossTeleporterComponent> btm;
    private ComponentMapper<CollisionBoundComponent> cbm;


    public MapTeleportationSystem(HashMap<BossTeleporterComponent, ArenaMap> map) {
        super(Aspect.all(BossTeleporterComponent.class, CollisionBoundComponent.class));
        this.mapTracker = map;
    }


    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return processingFlag;
    }

    public void goFromTo(BossTeleporterComponent btc){

        if(mapTracker.containsKey(btc)){
            processingFlag = true;
            switchMap(btc);
        }




    }

    public void switchMap(BossTeleporterComponent from){


        System.out.println("like wah");

        boolean exists = false;
        BossTeleporterComponent to = new BossTeleporterComponent();

        for(BossTeleporterComponent btc : mapTracker.keySet()){
            if(from.link == btc.link && from != btc){
                exists = true;
                to = btc;
                break;
            }
        }

        if(exists) {
            if (mapTracker.containsKey(to)) {

                System.out.println(mapTracker.containsKey(to));

                ArenaMap map = mapTracker.get(to);
                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                rts.packRoom(world, rts.getCurrentArena());
                rts.setCurrentMap(map);
                rts.unpackRoom(rts.getCurrentArena());


                PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
                CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);

                System.out.println("size of entites" + this.getEntities().size());
                for (Entity e : this.getEntities()) {
                    if (btm.get(e).link == to.link) {
                        pc.position.x = cbm.get(e).getCenterX();
                        pc.position.y = cbm.get(e).getCenterY();
                    }
                }
            }
        }


    }

}
