package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.utils.BagSearch;

import java.util.HashMap;

/**
 * Created by Home on 28/05/2017.
 */

public class MapTeleportationSystem extends EntitySystem {

    private HashMap<BossTeleporterComponent, ArenaMap> mapTracker;

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
        return false;
    }


    /**
     * Checks to see if the source Teleporter has a map associated with it.
     * Then checks if the source Teleporter links to a destination that exists in the Map Tracker
     * If this is the case the maps are the player is then moved into the other map and a screenwipe is
     * triggered
     *
     * @param source - The Teleporter that was touched the player triggering the Map Switch
     */
    public void goFromSourceToDestination(BossTeleporterComponent source){

        if(mapTracker.containsKey(source)){

            final BossTeleporterComponent destination = isTeleportAvaliable(source);

            if(destination != null) {
                    world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                        @Override
                        public void performAction(World world, Entity e) {
                            switchMap(destination);
                            for(BaseSystem s: world.getSystems()){
                                s.setEnabled(true);
        /*                if(s instanceof CameraSystem || s instanceof FollowPositionSystem) {
                            s.setEnabled(true);
                        }*/
                            }
                        }
                    });
                }
        }
    }

    /**
     * Switchs the map to another map in the Arena Map Tracker
     * @param destination - The Teleporter that is being switched to
     */
    private void switchMap(BossTeleporterComponent destination){

            if (mapTracker.containsKey(destination)) {

                ArenaMap map = mapTracker.get(destination);
                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                rts.packRoom(world, rts.getCurrentArena());
                rts.setCurrentMap(map);
                rts.unpackRoom(rts.getCurrentArena());

                placePlayerAfterTeleport(rts.getCurrentArena());

            }
    }


    /**
     * Uses the offset parameters of the destination BossTeleportComponenet to place the player after they have been
     * teleported
     * @param currentArena - The destination arena the player has been teleported to
     */
    private void placePlayerAfterTeleport(Arena currentArena){

        PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class);
        CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class);
        MoveToComponent mtc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(MoveToComponent.class);
        world.getSystem(PlayerInputSystem.class).turnOffGlide();

        for(Bag<Component> b : currentArena.getBagOfEntities()) {
            if (BagSearch.contains(BossTeleporterComponent.class, b)  && BagSearch.contains(CollisionBoundComponent.class, b)) {

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



    /**
     * Checks if there is a teleporter available in the Map Tracker with the same link
     * @param source - The Teleporter that was entered
     * @return - The Destination teleporter is returned if one exists, if not a null value is returned
     */
    private BossTeleporterComponent isTeleportAvaliable(BossTeleporterComponent source){
        for(BossTeleporterComponent destination : mapTracker.keySet()){
            if(source.link == destination.link && source != destination){
                return destination;
            }
        }
        return null;
    }


    /**
     * Currently this method is used to change the level from 1 to 2 and etc.
     *
     * Needs to be improved to better place the player after the map change has taken place.
     *
     * Also sets the new Map Tracker used for different Arena Map teleportations
     *
     */
    public void createNewLevel(){

        RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
        rts.packRoom(world, rts.getCurrentArena());

        JigsawGenerator jg = world.getSystem(ChangeLevelSystem.class).incrementLevel();
        jg.generate();

        mapTracker = jg.getMapTracker();

        rts.setCurrentMap(jg.getStartingMap());
        rts.unpackRoom(rts.getCurrentArena());

        PositionComponent player =  world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class);
        player.position.x = rts.getCurrentArena().getWidth() / 2;
        player.position.y = rts.getCurrentArena().getHeight() / 2;
        VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class);
        MoveToComponent mtc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(MoveToComponent.class);
        vc.velocity.x = 0;
        vc.velocity.y = 0;
        world.getSystem(PlayerInputSystem.class).turnOffGlide();

    }

}
