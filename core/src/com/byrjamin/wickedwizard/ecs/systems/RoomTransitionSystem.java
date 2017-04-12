package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGUI;
import com.byrjamin.wickedwizard.factories.arenas.RoomFactory;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.archive.maps.MapCoords;

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
    ComponentMapper<ChildComponent> cm;



    private Arena currentArena;
    private Array<Arena> roomArray;
    private OrderedSet<Arena> visitedArenas = new OrderedSet<Arena>();
    private OrderedSet<Arena> unvisitedButAdjacentArenas = new OrderedSet<Arena>();

    private boolean processingFlag = false;
    private MapCoords destination;
    private MapCoords previousDestination;
    private MapCoords playerLocation = new MapCoords(0,0);
    private float doorEntryPercentage;


    @SuppressWarnings("unchecked")
    public RoomTransitionSystem(Arena currentArena, Array<Arena> roomArray) {
        super(Aspect.all().exclude(PlayerComponent.class));
        this.currentArena = currentArena;
        //visitedArenas.addAll(roomArray);
        this.roomArray = roomArray;
        visitedArenas.add(currentArena);
        unvisitedButAdjacentArenas.addAll(getAdjacentArenas(currentArena));
    }

    @Override
    protected void processSystem() {
        currentArena.getBagOfEntities().clear();
        //Pack
        for(Entity e : this.getEntities()){
            if(!bm.has(e)) {

                if(cm.has(e)){
                    if(world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class).
                            children.contains(cm.get(e), true)){
                        continue;
                    }
                }

                Bag<Component> b = new Bag<Component>();
                e.getComponents(new Bag<Component>());
                currentArena.getBagOfEntities().add(e.getComponents(b));
            }

            e.deleteFromWorld();
        }


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

                float doorEntryY = cbc.bound.y + (cbc.bound.getHeight() * doorEntryPercentage);

                if(dc.currentCoords.equals(destination) && dc.leaveCoords.equals(previousDestination)){
                    switch (dc.exit){
                        case left: player.position.x = cbc.bound.getX() + cbc.bound.getWidth() + pBound.bound.getWidth();
                            player.position.y = doorEntryY;
                            break;
                        case right: player.position.x = cbc.bound.getX() - pBound.bound.getWidth();
                            player.position.y = doorEntryY;
                            break;
                        case up:
                            player.position.x = cbc.getCenterX();
                            player.position.y = cbc.getCenterY();
                            break;
                        case down:
                            player.position.x = cbc.getCenterX();
                            player.position.y = cbc.getCenterY();
                            break;
                    }
                }

            }



        }

        world.getSystem(FindPlayerSystem.class).getPC(GravityComponent.class).ignoreGravity = false;
        world.getSystem(FindPlayerSystem.class).getPC(MoveToComponent.class).reset();
        Vector2 velocity  = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class).velocity;

/*        if(Math.abs(velocity.x) > Measure.units(60f) / 2) {
            velocity.x = velocity.x > 0 ? Measure.units(60f) / 2 : -Measure.units(60f) / 2;
        }*/
/*
        if(velocity.y > Measure.units(60f)) {
            velocity.y = Measure.units(60f);
        }*/

        velocity.y = velocity.y / 2;
        velocity.x = velocity.x / 2 ;

        world.getSystem(PlayerInputSystem.class).activeGrapple = false;
        System.out.println("VISITED ARENA SIZE :" + visitedArenas.size);

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
            processingFlag = false;
            return true;
        }
        return false;
    }


    public void updateGUI(ArenaGUI aGUI, OrthographicCamera gamecam){
        aGUI.update(world.delta, gamecam, visitedArenas, unvisitedButAdjacentArenas,
                getCurrentArena(),
                getCurrentPlayerLocation()
                );
    }


    public boolean goFromTo(MapCoords previousDestination, MapCoords destination, float doorEntryPercentage){

        Arena next = findRoom(destination);
        if(next != null) {
            processingFlag = true;
            this.previousDestination = previousDestination;
            this.destination = destination;
            this.doorEntryPercentage = doorEntryPercentage;
            processSystem();
            return true;
        }
        return false;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public MapCoords getCurrentPlayerLocation(){
        CollisionBoundComponent pBound = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
        playerLocation.setX(currentArena.getStartingCoords().getX() + (int) (pBound.getCenterX() / RoomFactory.SECTION_WIDTH));
        playerLocation.setY(currentArena.getStartingCoords().getY() + (int) (pBound.getCenterY() / RoomFactory.SECTION_HEIGHT));
        return playerLocation;
    }

    public Array<Arena> getRoomArray() {
        return roomArray;
    }


}
