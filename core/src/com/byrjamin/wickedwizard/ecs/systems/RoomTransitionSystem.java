package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.RoomFactory;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.MapCoords;

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

    private Arena currentArena;
    private Array<Arena> roomArray;

    private boolean processingFlag = false;
    private MapCoords destination;
    private MapCoords previousDestination;
    private MapCoords playerLocation = new MapCoords(0,0);


    @SuppressWarnings("unchecked")
    public RoomTransitionSystem(Arena currentArena, Array<Arena> roomArray) {
        super(Aspect.all().exclude(PlayerComponent.class));
        this.currentArena = currentArena;
        this.roomArray = roomArray;
    }

    @Override
    protected void processSystem() {
        currentArena.getBagOfEntities().clear();
        //Pack
        for(Entity e : this.getEntities()){
            if(!bm.has(e)) {
                Bag<Component> b = new Bag<Component>();
                e.getComponents(new Bag<Component>());
                currentArena.getBagOfEntities().add(e.getComponents(b));
            }
            e.deleteFromWorld();
            //System.out.println("Deletion");
        }

        currentArena = findRoom(destination);

        if(currentArena == null){
            return;
        }

        System.out.println(destination);
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
                PositionComponent player =  pm.get(world.getSystem(FindPlayerSystem.class).getPlayer());
                CollisionBoundComponent pBound = cbm.get(world.getSystem(FindPlayerSystem.class).getPlayer());

                if(dc.currentCoords.equals(destination) && dc.leaveCoords.equals(previousDestination)){
                    switch (dc.exit){
                        case left: player.position.x = cbc.bound.getX() + cbc.bound.getWidth() + pBound.bound.getWidth();
                            player.position.y = cbc.getCenterY();
                            break;
                        case right: player.position.x = cbc.bound.getX() - pBound.bound.getWidth();
                            player.position.y = cbc.getCenterY();
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

        gm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).ignoreGravity = false;
        mtm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).reset();
        Vector2 velocity  = vm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).velocity;

        if(Math.abs(velocity.x) > Measure.units(60f) / 2) {
            velocity.x = velocity.x > 0 ? Measure.units(60f) / 2 : -Measure.units(60f) / 2;
        }

        if(velocity.y > Measure.units(60f)) {
            velocity.y = Measure.units(60f);
        }

        world.getSystem(PlayerInputSystem.class).grappleDestination = null;
        world.getSystem(PlayerInputSystem.class).hasTarget = false;

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



    public boolean goFromTo(MapCoords previousDestination, MapCoords destination){

        Arena next = findRoom(destination);
        if(next != null) {
            processingFlag = true;
            this.previousDestination = previousDestination;
            this.destination = destination;
            processSystem();
            return true;
        }
        return false;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public MapCoords getCurrentPlayerLocation(){
        CollisionBoundComponent pBound = cbm.get(world.getSystem(FindPlayerSystem.class).getPlayer());
        playerLocation.setX(currentArena.getStartingCoords().getX() + (int) (pBound.getCenterX() / RoomFactory.SECTION_WIDTH));
        playerLocation.setY(currentArena.getStartingCoords().getY() + (int) (pBound.getCenterY() / RoomFactory.SECTION_HEIGHT));
        return playerLocation;
    }

    public Array<Arena> getRoomArray() {
        return roomArray;
    }
}
