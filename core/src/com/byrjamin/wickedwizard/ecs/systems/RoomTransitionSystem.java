package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.DoorComponent;
import com.byrjamin.wickedwizard.ecs.components.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;
import com.byrjamin.wickedwizard.factories.Arena;
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

    private Arena currentArena;
    private Array<Arena> roomArray;

    private boolean processingFlag = false;
    private MapCoords destination;
    private MapCoords previousDestination;


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
        }

        for(DoorComponent doorComponent : currentArena.getDoors()){
            System.out.println(doorComponent.currentCoords + " Current");
            System.out.println(doorComponent.leaveCoords + "leave");

            System.out.println(destination + " des");
            System.out.println(previousDestination + "prev");
            if(doorComponent.currentCoords.equals(destination) && doorComponent.leaveCoords.equals(previousDestination)){
                switch (doorComponent.exit){
                    case left: pm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).position.x = 100;
                        break;
                    case right: pm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).position.x = 1100;
                        break;
                    case up: pm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).position.y = 800;
                        break;
                    case down: pm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).position.y = 300;
                        break;
                }
                System.out.println("HAUWHDAUIWDHNAUIWNDAWUID");

            }
        }
        gm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).ignoreGravity = false;
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
        System.out.println(roomArray.size);
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

}