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
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.factories.arenas.JigsawGenerator;
import com.byrjamin.wickedwizard.factories.arenas.ArenaShellFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.RoomTransition;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 13/03/2017.
 */

public class RoomTransitionSystem extends EntitySystem {

    ComponentMapper<BulletComponent> bm;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<GravityComponent> gm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ActiveOnTouchComponent> aotm;
    ComponentMapper<DoorComponent> dm;
    ComponentMapper<MoveToComponent> mtm;
    ComponentMapper<ParentComponent> parm;
    ComponentMapper<ExpireComponent> em;
    ComponentMapper<ChildComponent> cm;



    private Arena currentArena;
    private Array<Arena> roomArray;
    private OrderedSet<Arena> visitedArenas = new OrderedSet<Arena>();
    private OrderedSet<Arena> unvisitedButAdjacentArenas = new OrderedSet<Arena>();

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
    public RoomTransitionSystem(Arena currentArena, Array<Arena> roomArray) {
        super(Aspect.all().exclude(PlayerComponent.class));
        this.currentArena = currentArena;
        //visitedArenas.addAll(roomArray);
        this.roomArray = roomArray;
        visitedArenas.add(currentArena);
        unvisitedButAdjacentArenas.addAll(getAdjacentArenas(currentArena));
    }

    public RoomTransition getEntryTransition() {
        return entryTransition;
    }

    public RoomTransition getExitTransition() {
        return exitTransition;
    }

    @Override
    protected void processSystem() {


        if(canNowExitTransition) {

            if (exitTransition == null) {
                blackScreen2(currentDoor.exit, world.getSystem(CameraSystem.class).getGamecam());
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

        if(entryTransition == null) {
            //world.getSystem(PlayerInputSystem.class).setEnabled(false);

            for(BaseSystem s: world.getSystems()){
                if(!(s instanceof RenderingSystem) && !(s instanceof RoomTransitionSystem)) {
                    s.setEnabled(false);
                }
            }

            blackScreen(currentDoor.exit, world.getSystem(CameraSystem.class).getGamecam());
        }

        if(!entryTransition.isFinished()){
            entryTransition.update(world.delta);
            return;
        } else {
        }


        //TODO add entity that covers screen once is has been completed continue to process system

        //Pack

        packRoom(world, currentArena);


        currentArena = findRoom(destination);
        if(currentArena == null){
            return;
        }
        visitedArenas.add(currentArena);
        if(unvisitedButAdjacentArenas.contains(currentArena)){
            unvisitedButAdjacentArenas.remove(currentArena);
        }

        for(Arena a : getAdjacentArenas(currentArena)){
            if(!visitedArenas.contains(a)){
                unvisitedButAdjacentArenas.add(a);
            }
        }

        for(Bag<Component> b : currentArena.getBagOfEntities()){
            Entity e = world.createEntity();
            for(Component c : b){
                e.edit().add(c);
            }

            if(aotm.has(e)){
                aotm.get(e).isActive = false;
            }

            if(dm.has(e)){
                DoorComponent dc = dm.get(e);
                CollisionBoundComponent cbc = cbm.get(e);
                PositionComponent player =  world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
                CollisionBoundComponent pBound = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);

                float doorEntryY = cbc.bound.y + (cbc.bound.getHeight() * doorEntryPercentage);

                if(dc.currentCoords.equals(destination) && dc.leaveCoords.equals(previousDestination)){
                    switch (dc.exit){
                        case LEFT: player.position.x = cbc.bound.getX() + cbc.bound.getWidth() + pBound.bound.getWidth();
                            player.position.y = doorEntryY;
                            break;
                        case RIGHT: player.position.x = cbc.bound.getX() - pBound.bound.getWidth();
                            player.position.y = doorEntryY;
                            break;
                        case UP:
                            player.position.x = cbc.getCenterX();
                            player.position.y = cbc.getCenterY();
                            vc.velocity.y /= 2;
                            break;
                        case DOWN:
                            player.position.x = cbc.getCenterX();
                            player.position.y = cbc.getCenterY();
                            vc.velocity.y = 0;
                            break;
                    }

                    pBound.bound.x = player.position.x;
                    pBound.bound.y = player.position.y;

                }


            }



        }

        world.getSystem(FindPlayerSystem.class).getPC(GravityComponent.class).ignoreGravity = false;
        world.getSystem(FindPlayerSystem.class).getPC(MoveToComponent.class).reset();
        Vector2 velocity  = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class).velocity;

        velocity.y = velocity.y / 2;
        velocity.x = velocity.x / 2 ;

        world.getSystem(com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem.class).getPlayerInput().activeGrapple = false;
        //System.out.println("VISITED ARENA SIZE :" + visitedArenas.size);

        System.out.println(currentArena.cotainingCoords);


        for(BaseSystem s: world.getSystems()){
            if(s instanceof CameraSystem) {
                s.setEnabled(true);
            }
        }

        canNowExitTransition = true;

    }


    public Arena findRoom(MapCoords destination){
        for(Arena a : roomArray) {
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


    public void recreateWorld(){

        packRoom(world, currentArena);

        JigsawGenerator jg = world.getSystem(ChangeLevelSystem.class).incrementLevel();
        jg.generateTutorial = false;

        visitedArenas.clear();
        unvisitedButAdjacentArenas.clear();

        this.roomArray = jg.generate();
        this.currentArena = jg.getStartingRoom();

        unpackRoom(currentArena);

        visitedArenas.add(currentArena);
        unvisitedButAdjacentArenas.addAll(getAdjacentArenas(currentArena));


        PositionComponent player =  world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
        player.position.x = currentArena.getWidth() / 2;
        player.position.y = currentArena.getHeight() / 2;


        //TODO this is some cheesy code, please fix later.
        world.getSystem(RoomTypeSystem.class).nextLevelDoor = false;


    }

    public void updateGUI(ArenaGUI aGUI, OrthographicCamera gamecam){
        aGUI.update(world.delta, gamecam, visitedArenas, unvisitedButAdjacentArenas,
                getCurrentArena(),
                getCurrentPlayerLocation()
                );
    }


    /**
     * The direction the transition starts from
     * @param start
     * @param gamecam
     */
    public void blackScreen(Direction start, OrthographicCamera gamecam) {

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        float startX;
        float startY;

        float width = gamecam.viewportWidth;
        float height = gamecam.viewportHeight;

        float endX;
        float endY;

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
/*

        blackScreenTarget = new MoveToComponent();
        blackScreenTarget.targetX = endX;
        blackScreenTarget.targetY = endY;
        blackScreenTarget.accelX = Measure.units(10f);
        blackScreenTarget.accelY = Measure.units(10f);
        blackScreenTarget.maxX = Measure.units(10f);
        blackScreenTarget.maxY = Measure.units(10f);

        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(startX, startY))
                .add(new VelocityComponent())
                .add(new AccelerantComponent(Measure.units(20f), Measure.units(20f)))
                .add(blackScreenTarget)
                .add(new CollisionBoundComponent(new Rectangle(startX, startY, width, height)));


        ShapeComponent sc = new ShapeComponent(width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR);
        sc.color = Color.WHITE;

        e.edit().add(sc);
        e.edit().add(new IntangibleComponent());
*/

    }


    public void blackScreen2(Direction start, OrthographicCamera gamecam) {

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        float startX;
        float startY;

        float width = gamecam.viewportWidth;
        float height = gamecam.viewportHeight;

        float endX;
        float endY;

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

    public Arena getCurrentArena() {
        return currentArena;
    }

    public MapCoords getCurrentPlayerLocation(){
        CollisionBoundComponent pBound = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
        playerLocation.setX(currentArena.getStartingCoords().getX() + (int) (pBound.getCenterX() / ArenaShellFactory.SECTION_WIDTH));
        playerLocation.setY(currentArena.getStartingCoords().getY() + (int) (pBound.getCenterY() / ArenaShellFactory.SECTION_HEIGHT));
        return playerLocation;
    }

    public Array<Arena> getRoomArray() {
        return roomArray;
    }


}
