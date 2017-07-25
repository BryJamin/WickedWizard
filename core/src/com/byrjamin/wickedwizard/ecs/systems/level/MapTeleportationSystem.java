package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.collider.Collider;
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

            final BossTeleporterComponent bossTeleporterComponent = btc;


            world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.LEFT_TO_RIGHT, new Task() {
                @Override
                public void performAction(World world, Entity e) {
                    switchMap(bossTeleporterComponent);
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

    public void switchMap(BossTeleporterComponent source){

        BossTeleporterComponent destination = isTeleportAvaliable(source);

        if(destination != null) {

            if (mapTracker.containsKey(destination)) {

                ArenaMap map = mapTracker.get(destination);
                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                rts.packRoom(world, rts.getCurrentArena());
                rts.setCurrentMap(map);
                rts.unpackRoom(rts.getCurrentArena());

                //This is so when you enter a new map it updates itself.
                if(rts.getVisitedArenas().size == 0){
                    rts.getVisitedArenas().add(rts.getCurrentArena());
                    rts.getUnvisitedButAdjacentArenas().addAll(rts.getAdjacentArenas(rts.getCurrentArena()));
                }

                PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
                CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
                MoveToComponent mtc = world.getSystem(FindPlayerSystem.class).getPC(MoveToComponent.class);
                world.getSystem(PlayerInputSystem.class).turnOffGlide();

               // this.process();
               // world.process();
                //System.out.println("size of entites" + this.getEntities().size());
                IntBag bag = world.getAspectSubscriptionManager().get(Aspect.all(BossTeleporterComponent.class)).getEntities();

                System.out.println(bag.size());

                for(Bag<Component> b : rts.getCurrentArena().getBagOfEntities()) {
                    if (BagSearch.contains(BossTeleporterComponent.class, b)) {

                        BossTeleporterComponent btc = BagSearch.getObjectOfTypeClass(BossTeleporterComponent.class, b);
                        CollisionBoundComponent teleporterBound = BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, b);
                        cbc.bound.setCenter(
                                teleporterBound.getCenterX() + btc.offsetX,
                                teleporterBound.getCenterY() + btc.offsetY);

                        vc.velocity.y = 0;
                        vc.velocity.x = 0;

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
