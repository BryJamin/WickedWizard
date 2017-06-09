package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Direction;

import java.util.HashMap;

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

            final BossTeleporterComponent hmm = btc;


            world.getSystem(ScreenWipeSystem.class).startScreenWipe(Direction.DOWN, new Action() {
                @Override
                public void performAction(World world, Entity e) {
                    switchMap(hmm);
                    for(BaseSystem s: world.getSystems()){
                        if(s instanceof CameraSystem || s instanceof FollowPositionSystem) {
                            s.setEnabled(true);
                        }
                    }
                }

                @Override
                public void cleanUpAction(World world, Entity e) {

                }
            });





        }
    }

    public void switchMap(BossTeleporterComponent from){

        BossTeleporterComponent to = isTeleportAvaliable(from);

        if(to != null) {

            if (mapTracker.containsKey(to)) {

                ArenaMap map = mapTracker.get(to);
                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                rts.packRoom(world, rts.getCurrentArena());
                rts.setCurrentMap(map);
                rts.unpackRoom(rts.getCurrentArena());
                rts.getVisitedArenas().add(rts.getCurrentArena());
                rts.getUnvisitedButAdjacentArenas().addAll(rts.getAdjacentArenas(rts.getCurrentArena()));

                for(Arena a : rts.getAdjacentArenas(rts.getCurrentArena())) {
                    if(!rts.getVisitedArenas().contains(a)) {
                        rts.getUnvisitedButAdjacentArenas().add(a);
                    }
                }

                PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
                CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
                MoveToComponent mtc = world.getSystem(FindPlayerSystem.class).getPC(MoveToComponent.class);
                world.getSystem(PlayerInputSystem.class).turnOffGlide();

                //System.out.println("size of entites" + this.getEntities().size());
                for (Entity e : this.getEntities()) {
                    if (btm.get(e).link == to.link) {

                        cbc.setCenterX(cbm.get(e).getCenterX());
                        cbc.bound.y = cbm.get(e).bound.getY() - cbc.bound.getHeight() * 2;
                        vc.velocity.y = 0;

                        pc.setX(cbc.bound.x);
                        pc.setY(cbc.bound.y);

                        mtc.reset();
                    }
                }
            }
        }


    }


    public BossTeleporterComponent isTeleportAvaliable(BossTeleporterComponent from){
        for(BossTeleporterComponent btc : mapTracker.keySet()){
            if(from.link == btc.link && from != btc){
                return btc;
            }
        }
        return null;
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
