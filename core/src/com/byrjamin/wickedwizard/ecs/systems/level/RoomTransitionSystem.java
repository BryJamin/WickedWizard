package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.RoomTransition;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 13/03/2017.
 */

public class RoomTransitionSystem extends EntitySystem {

    private ComponentMapper<BulletComponent> bm;
    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<VelocityComponent> vm;
    private ComponentMapper<GravityComponent> gm;
    private ComponentMapper<CollisionBoundComponent> cbm;
    private ComponentMapper<ActiveOnTouchComponent> aotm;
    private ComponentMapper<DoorComponent> dm;
    private ComponentMapper<MoveToComponent> mtm;
    private ComponentMapper<ParentComponent> parm;
    private ComponentMapper<ExpireComponent> em;
    private ComponentMapper<ChildComponent> cm;

    private ArenaMap currentMap;

    private boolean processingFlag = false;
    private MapCoords destination;
    private MapCoords previousDestination;
    private MapCoords playerLocation = new MapCoords(0,0);
    private DoorComponent currentDoor;
    private float doorEntryPercentage;


    public RoomTransition entryTransition;
    public RoomTransition exitTransition;

    private boolean canNowExitTransition = false;

    private MoveToComponent blackScreenTarget;


    @SuppressWarnings("unchecked")
    public RoomTransitionSystem(ArenaMap arenaMap) {
        super(Aspect.all().exclude(PlayerComponent.class));
        this.currentMap = arenaMap;
        currentMap.getVisitedArenas().add(currentMap.getCurrentArena());
        currentMap.getUnvisitedButAdjacentArenas().addAll(getAdjacentArenas(currentMap.getCurrentArena()));
    }

    public RoomTransition getEntryTransition() {
        return entryTransition;
    }

    public RoomTransition getExitTransition() {
        return exitTransition;
    }

    @Override
    protected void processSystem() {

        if(!canNowExitTransition) {
            if(entryTransition == null) {
                for(BaseSystem s: world.getSystems()){
                    if(!(s instanceof RenderingSystem) && !(s instanceof RoomTransitionSystem)) {
                        s.setEnabled(false);
                    }
                }
                entryAnimation(currentDoor.exit, world.getSystem(CameraSystem.class).getGamecam());
            }

            if(!entryTransition.isFinished()){
                entryTransition.update(world.delta);
                return;
            }
        }



        if(canNowExitTransition) {

            if (exitTransition == null) {
                exitAnimation(currentDoor.exit, world.getSystem(CameraSystem.class).getGamecam());
                entryTransition = null;
                return;
            }

            if(!exitTransition.isFinished()){
                exitTransition.update(world.delta);
                return;
            } else {
                canNowExitTransition = false;
                exitTransition = null;
                //world.getSystem(PlayerInputSystem.class).setEnabled(true);
                processingFlag = false;

                for(BaseSystem s: world.getSystems()){
                    s.setEnabled(true);
                }
            }


            return;

        }


        switchRooms();


/*        if(currentArena.roomType == Arena.RoomType.ITEM){

            for(AltarComponent ac : currentArena.altars){
                if(ac.hasItem){

                }
            }
            currentArena.fin




        }*/

        for(BaseSystem s: world.getSystems()){
            if(s instanceof CameraSystem || s instanceof FollowPositionSystem) {
                s.setEnabled(true);
            }
        }

        canNowExitTransition = true;


    }


    public void switchRooms(){

        packRoom(world, currentMap.getCurrentArena());


        currentMap.setCurrentArena(findRoom(destination));
        if(currentMap.getCurrentArena() == null){
            return;
        }

        currentMap.getVisitedArenas().add(currentMap.getCurrentArena());
        currentMap.getUnvisitedButAdjacentArenas().remove(currentMap.getCurrentArena());

        for(Arena a : getAdjacentArenas(currentMap.getCurrentArena())){
            if(!currentMap.getVisitedArenas().contains(a)){
                currentMap.getUnvisitedButAdjacentArenas().add(a);
            }
        }

        for(Bag<Component> b : currentMap.getCurrentArena().getBagOfEntities()){
            Entity e = world.createEntity();
            for(Component c : b){
                e.edit().add(c);
            }

            if(aotm.has(e)){
                aotm.get(e).isActive = false;
            }

            if(dm.has(e)){
                placePlayerAfterTransition(dm.get(e),
                        cbm.get(e),
                        world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class),
                        world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class),
                        world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class),
                        doorEntryPercentage);
            }



        }

        world.getSystem(FindPlayerSystem.class).getPC(GravityComponent.class).ignoreGravity = false;
        world.getSystem(FindPlayerSystem.class).getPC(MoveToComponent.class).reset();
        Vector2 velocity  = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class).velocity;

        velocity.y = velocity.y / 2;
        velocity.x = velocity.x / 2 ;

        world.getSystem(com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem.class).getPlayerInput().activeGrapple = false;
        //System.out.println("VISITED ARENA SIZE :" + visitedArenas.size);


    }

    public void placePlayerAfterTransition(DoorComponent dc,
                                           CollisionBoundComponent doorBoundary,
                                           PositionComponent playerPosition,
                                           CollisionBoundComponent playerBoundary,
                                           VelocityComponent playerVelocity,
                                           float doorEntryPercentage){

        float doorEntryY = doorBoundary.bound.y + (doorBoundary.bound.getHeight() * doorEntryPercentage);

        if(dc.currentCoords.equals(destination) && dc.leaveCoords.equals(previousDestination)){
            switch (dc.exit){
                case LEFT: playerPosition.position.x = doorBoundary.bound.getX() + doorBoundary.bound.getWidth() + playerBoundary.bound.getWidth();
                    playerPosition.position.y = doorEntryY;
                    break;
                case RIGHT: playerPosition.position.x = doorBoundary.bound.getX() - playerBoundary.bound.getWidth();
                    playerPosition.position.y = doorEntryY;
                    break;
                case UP:
                    System.out.println("UP");
                    playerPosition.position.x = doorBoundary.getCenterX();
                    playerPosition.position.y = doorBoundary.bound.getY() - playerBoundary.bound.getHeight();;
                    playerVelocity.velocity.y = (playerVelocity.velocity.y > 0) ? 0 : playerVelocity.velocity.y;
                    break;
                case DOWN:
                    System.out.println("DOWN");
                    playerPosition.position.x = doorBoundary.getCenterX();
                    playerPosition.position.y = doorBoundary.bound.getY() + playerBoundary.bound.getHeight() * 2;
                    playerVelocity.velocity.y = (playerVelocity.velocity.y < Measure.units(70f)) ? Measure.units(70f) : playerVelocity.velocity.y;
                    break;
            }

            playerBoundary.bound.x = playerPosition.position.x;
            playerBoundary.bound.y = playerPosition.position.y;

        }
    }

    public Arena findRoom(MapCoords destination){
        for(Arena a : currentMap.getRoomArray()) {
            if(a.cotainingCoords.contains(destination, false)){
                return a;
            }
        }
        //TODO return an error
        return null;
    }

    public Array<Arena> getAdjacentArenas(Arena a){
        Array<Arena> arenas = new Array<Arena>();
        for(DoorComponent dc : a.getDoors()){
            Arena arena = findRoom(dc.leaveCoords);
            if(arena != null){
                arenas.add(arena);
            }
        }
        return arenas;
    }

    //public Arena findRoom(MapCoords destination){

    //}


    @Override
    protected boolean checkProcessing() {
        if (processingFlag) {
            return true;
        }
        return false;
    }


    public void packRoom(World world, Arena arena){

        arena.getBagOfEntities().clear();

        for(Entity e : this.getEntities()){

            if(!bm.has(e) && !em.has(e)) {

                if(cm.has(e)){
                    if(world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class).
                            children.contains(cm.get(e), true)){
                        continue;
                    }
                }

                Bag<Component> b = new Bag<Component>();
                e.getComponents(new Bag<Component>());
                arena.getBagOfEntities().add(e.getComponents(b));
            }

            e.deleteFromWorld();
        }

    }


    public void unpackRoom(Arena arena) {

        for(Bag<Component> b : arena.getBagOfEntities()) {
            Entity e = world.createEntity();
            for (Component c : b) {
                e.edit().add(c);
            }

            if (aotm.has(e)) {
                aotm.get(e).isActive = false;
            }

        }

    }


    /**
     * The direction the transition starts from
     * @param start
     * @param gamecam
     */
    public void entryAnimation(Direction start, OrthographicCamera gamecam) {

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        float width = gamecam.viewportWidth;
        float height = gamecam.viewportHeight;

        switch(start){
            case LEFT:
            default:
                entryTransition = new RoomTransition(camX, camY, width, height);
                entryTransition.fromLeftToCenter();
                break;
            case RIGHT:
                entryTransition = new RoomTransition(camX, camY, width, height);
                entryTransition.fromRightToCenter();
                break;
            case UP:
                entryTransition = new RoomTransition(camX, camY, width, height);
                entryTransition.fromTopToCenter();
                break;
            case DOWN:
                entryTransition = new RoomTransition(camX, camY, width, height);
                entryTransition.fromBottomToCenter();
                break;
        }

    }


    public void exitAnimation(Direction start, OrthographicCamera gamecam) {

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        float width = gamecam.viewportWidth;
        float height = gamecam.viewportHeight;

        switch (start) {
            case LEFT:
            default:
                exitTransition = new RoomTransition(camX, camY, width, height);
                exitTransition.fromCenterToLeft();
                break;
            case RIGHT:
                exitTransition = new RoomTransition(camX, camY, width, height);
                exitTransition.fromCenterToRight();
                break;
            case UP:
                exitTransition = new RoomTransition(camX, camY, width, height);
                exitTransition.fromCenterToBottom();
                break;
            case DOWN:
                exitTransition = new RoomTransition(camX, camY, width, height);
                exitTransition.fromCenterToTop();
                break;
        }

    }


    public boolean goFromTo(DoorComponent dc, float doorEntryPercentage){

        Arena next = findRoom(dc.leaveCoords);
        if(next != null) {
            processingFlag = true;
            this.currentDoor = dc;
            this.previousDestination = dc.currentCoords;
            this.destination = dc.leaveCoords;
            this.doorEntryPercentage = doorEntryPercentage;
            return true;
        }
        return false;
    }


    public MapCoords getCurrentPlayerLocation(){
        CollisionBoundComponent pBound = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
        playerLocation.setX(currentMap.getCurrentArena().getStartingCoords().getX() + (int) (pBound.getCenterX() / ArenaShellFactory.SECTION_WIDTH));
        playerLocation.setY(currentMap.getCurrentArena().getStartingCoords().getY() + (int) (pBound.getCenterY() / ArenaShellFactory.SECTION_HEIGHT));
        return playerLocation;
    }

    public void setCurrentMap(ArenaMap currentMap) {
        this.currentMap = currentMap;
    }

    public ArenaMap getCurrentMap() {
        return currentMap;
    }

    public Arena getCurrentArena(){
        return currentMap.getCurrentArena();
    }

    public Array<Arena> getRoomArray(){
        return currentMap.getRoomArray();
    }


    public OrderedSet<Arena> getVisitedArenas(){
        return currentMap.getVisitedArenas();
    }

    public OrderedSet<Arena> getUnvisitedButAdjacentArenas(){
        return currentMap.getUnvisitedButAdjacentArenas();
    }

}
