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
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.GiantKugelRoom;
import com.byrjamin.wickedwizard.factories.arenas.bossrooms.WandaRoom;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Home on 28/05/2017.
 */

public class MapTeleportationSystem extends EntitySystem {

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

                ArenaMap map = mapTracker.get(to);
                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                rts.packRoom(world, rts.getCurrentArena());
                rts.setCurrentMap(map);
                rts.unpackRoom(rts.getCurrentArena());
                rts.getVisitedArenas().add(rts.getCurrentArena());
                rts.getUnvisitedButAdjacentArenas().addAll(rts.getAdjacentArenas(rts.getCurrentArena()));


                PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
                CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
                MoveToComponent mtc = world.getSystem(FindPlayerSystem.class).getPC(MoveToComponent.class);
                world.getSystem(PlayerInputSystem.class).turnOffGlide();

                //System.out.println("size of entites" + this.getEntities().size());
                for (Entity e : this.getEntities()) {
                    if (btm.get(e).link == to.link) {

                        cbc.setCenterY(cbm.get(e).getCenterY());
                        cbc.bound.y = cbm.get(e).bound.getY() - cbc.bound.getHeight();
                        vc.velocity.y = -Measure.units(40f);

                        pc.setX(cbc.bound.x);
                        pc.setY(cbc.bound.y);

                        mtc.reset();


                    }
                }
            }
        }


    }


    public void recreateWorld(){

        RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
        rts.packRoom(world, rts.getCurrentArena());

        JigsawGenerator jg = world.getSystem(ChangeLevelSystem.class).incrementLevel();
        jg.generateTutorial = false;
        jg.generate();

        mapTracker = jg.getMapTracker();

        rts.setCurrentMap(jg.getStartingMap());
        rts.unpackRoom(rts.getCurrentArena());
        rts.getVisitedArenas().add(rts.getCurrentArena());
        rts.getUnvisitedButAdjacentArenas().addAll(rts.getAdjacentArenas(rts.getCurrentArena()));

        PositionComponent player =  world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
        player.position.x = rts.getCurrentArena().getWidth() / 2;
        player.position.y = rts.getCurrentArena().getHeight() / 2;

    }

}
