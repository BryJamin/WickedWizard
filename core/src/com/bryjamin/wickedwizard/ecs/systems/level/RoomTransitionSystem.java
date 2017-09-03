package com.bryjamin.wickedwizard.ecs.systems.level;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.CurrencyComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.UnpackableComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.utils.MapCoords;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 13/03/2017.
 */

public class RoomTransitionSystem extends EntitySystem {

    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent> bm;
    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent> vm;
    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent> gm;
    private ComponentMapper<CollisionBoundComponent> cbm;
    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> dm;
    private ComponentMapper<MoveToComponent> mtm;
    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent> parm;
    private ComponentMapper<ExpireComponent> expireM;
    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent> cm;
    private ComponentMapper<UnpackableComponent> unpackableComponent;

    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.OffScreenPickUpComponent> offScreenPickUpM;

    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.ai.OnRoomLoadActionComponent> onRoomLoadMapper;

    private ComponentMapper<CurrencyComponent> currencyComponentComponentMapper;

    private com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap currentMap;

    private boolean processingFlag = false;
    private MapCoords destination;
    private MapCoords previousDestination;
    private MapCoords playerLocation = new MapCoords(0,0);
    private com.bryjamin.wickedwizard.ecs.components.object.DoorComponent currentDoor;
    private float doorEntryPercentage;


    @SuppressWarnings("unchecked")
    public RoomTransitionSystem(com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap arenaMap) {
        super(Aspect.all().exclude(com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent.class, UnpackableComponent.class));
        this.setCurrentMap(arenaMap);
    }

    @Override
    protected void processSystem() {

        world.getSystem(ScreenWipeSystem.class).instantWipe(new Action() {
            @Override
            public void performAction(World world, Entity e) {
                switchRooms();

                for(BaseSystem s: world.getSystems()){
                    if(s instanceof CameraSystem || s instanceof FollowPositionSystem) {
                        s.setEnabled(true);
                    }
                }

                //world.getSystem(SoundSystem.class).disposeMusic();

            }
        });

        processingFlag = false;

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


        unpackRoom(currentMap.getCurrentArena());

        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent.class).ignoreGravity = false;
        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(MoveToComponent.class).reset();
        Vector2 velocity  = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class).velocity;

        velocity.y = velocity.y / 2;
        velocity.x = velocity.x / 2 ;

        world.getSystem(PlayerInputSystem.class).getPlayerInput().activeGrapple = false;


        //System.out.println("VISITED ARENA SIZE :" + visitedArenas.size);


    }

    public void placePlayerAfterTransition(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc,
                                           CollisionBoundComponent doorBoundary,
                                           PositionComponent playerPosition,
                                           CollisionBoundComponent playerBoundary,
                                           com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent playerVelocity,
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
                    playerPosition.position.x = doorBoundary.getCenterX();
                    playerPosition.position.y = doorBoundary.bound.getY() - playerBoundary.bound.getHeight();;
                    playerVelocity.velocity.y = (playerVelocity.velocity.y > 0) ? 0 : playerVelocity.velocity.y;
                    break;
                case DOWN:
                    playerPosition.position.x = doorBoundary.getCenterX();
                    playerPosition.position.y = doorBoundary.bound.getY() + playerBoundary.bound.getHeight() * 2;
                    playerVelocity.velocity.y = (playerVelocity.velocity.y < Measure.units(70f)) ? Measure.units(70f) : playerVelocity.velocity.y;
                    break;
            }

            playerBoundary.bound.x = playerPosition.position.x;
            playerBoundary.bound.y = playerPosition.position.y;

        }
    }

    /**
     * Checks if an arena Exists via it's Map Co-ordinates
     * @param destination
     * @return
     */
    public Arena findRoom(MapCoords destination){
        for(Arena a : currentMap.getRoomArray()) {
            if(a.cotainingCoords.contains(destination, false)){
                return a;
            }
        }
        //TODO return an error
        return null;
    }

    /**
     * Gets all Adjacent arenas of the current Arena (May store this in a seperate class as a static)
     * @param a
     * @return
     */
    public Array<Arena> getAdjacentArenas(Arena a){
        Array<Arena> arenas = new Array<Arena>();
        for(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc : a.getDoors()){
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


    /**
     * Gets all currently active entites and converted them into a Bag of COmponents that will be stored
     * in the selected Arena.
     *
     * Certain entities are not stored such as those who are set to Expire. Or are picked up off screen.
     *
     * @param world - The current world
     * @param arena - The target where all new Bags of Components will be stored
     */
    public void packRoom(World world, Arena arena){

        arena.getBagOfEntities().clear();

        for(Entity e : this.getEntities()){

            //if(unpackableComponent.has(e)) continue;

            if(!bm.has(e) && !expireM.has(e) && !offScreenPickUpM.has(e)) {

                if(cm.has(e)){
                    if(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class).
                            children.contains(cm.get(e), true)){
                        continue;
                    }
                }

                Bag<Component> b = new Bag<Component>();
                e.getComponents(new Bag<Component>());
                arena.getBagOfEntities().add(e.getComponents(b));
            }

            if(offScreenPickUpM.has(e)){
                if(world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerEntity() != null) {
                    offScreenPickUpM.get(e).getPickUp().applyEffect(world, world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerEntity());
                }
            }



            e.deleteFromWorld();
        }

    }


    /**
     * Unpacks the bags of bags of components stored in the selected Arena and turns them into
     * Entities inside of the world
     * @param arena - The arena who's Bags of Bags of Components will be unpacked
     */
    public void unpackRoom(Arena arena) {

        for(Bag<Component> b : arena.getBagOfEntities()) {

            Entity e = world.createEntity();
            for(Component c : b){
                e.edit().add(c);
            }

            if(dm.has(e)){
                placePlayerAfterTransition(dm.get(e),
                        cbm.get(e),
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(PositionComponent.class),
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class),
                        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent.class),
                        doorEntryPercentage);
            }

            if(onRoomLoadMapper.has(e)){

                OnRoomLoadActionComponent onRoomLoadActionComponent = e.getComponent(OnRoomLoadActionComponent.class);
                onRoomLoadActionComponent.action.performAction(world, e);
                if(!onRoomLoadActionComponent.repeat){
                    e.edit().remove(onRoomLoadActionComponent);
                }

            }


        }

    }

    /**
     * Triggers the running of the RoomTransitionSystem if a door can be found in the map of existing arenas
     *
     * @param dc - The active door that has been touched by the player entity
     * @param doorEntryHeightPercentage - The height the player was at when he touched the door
     * @return - True if there is a matching door, False if there is not
     */
    public boolean goFromSourceDoorToDestinationDoor(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc, float doorEntryHeightPercentage){

        Arena next = findRoom(dc.leaveCoords);
        if(next != null) {
            processingFlag = true;
            this.currentDoor = dc;
            this.previousDestination = dc.currentCoords;
            this.destination = dc.leaveCoords;
            this.doorEntryPercentage = doorEntryHeightPercentage;
            return true;
        }
        return false;
    }


    /**
     * Calcuates the current player position in Map Co-ordinates using the position of the player
     * This is to show in real spawnTime the player position in rooms of size larger than 1 square
     * @return - The Player's position in map co-ordinates
     */
    public MapCoords getCurrentPlayerLocation(){
        CollisionBoundComponent pBound = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        playerLocation.setX(currentMap.getCurrentArena().getStartingCoords().getX() + (int) (pBound.getCenterX() / com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH));
        playerLocation.setY(currentMap.getCurrentArena().getStartingCoords().getY() + (int) (pBound.getCenterY() / com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT));
        return playerLocation;
    }

    /**
     * Sets the current Map of the RoomTransitionSystem
     *
     * Also runs the update method on new Maps
     *
     * @param currentMap - The map that is going to be the current Arena Map of the game
     */
    public void setCurrentMap(com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap currentMap) {
        this.currentMap = currentMap;
        this.updateNewMap(currentMap);
    }

    public com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap getCurrentMap() {
        return currentMap;
    }

    public Arena getCurrentArena(){
        return currentMap.getCurrentArena();
    }


    /**
     * If the current map has just been added and does not have any visited or unvisited arenas,
     * Use this is set them up.
     */
    public void updateNewMap(com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap currentMap){
        if(currentMap.getVisitedArenas().size == 0){
            currentMap.getVisitedArenas().add(currentMap.getCurrentArena());
            currentMap.getUnvisitedButAdjacentArenas().addAll(this.getAdjacentArenas(currentMap.getCurrentArena()));
        }
    }

}
