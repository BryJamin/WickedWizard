package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.BulletComponent;
import com.byrjamin.wickedwizard.factories.Arena;
import com.byrjamin.wickedwizard.maps.MapCoords;

/**
 * Created by Home on 13/03/2017.
 */

public class RoomTransitionSystem extends EntitySystem {

    ComponentMapper<BulletComponent> bm;

    private Arena currentArena;
    private Array<Arena> roomArray;

    private boolean processingFlag = false;
    private MapCoords destination;


    @SuppressWarnings("unchecked")
    public RoomTransitionSystem(Arena currentArena, Array<Arena> roomArray) {
        super(Aspect.all());
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
        System.out.println(destination);
        for(Bag<Component> b : currentArena.getBagOfEntities()){
            Entity e = world.createEntity();
            for(Component c : b){
                e.edit().add(c);
            }
        }
    }


    public Arena findRoom(MapCoords destination){
        System.out.println(roomArray.size);
        for(Arena a : roomArray) {
            if(a.location.equals(destination)){
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



    public void goTo(MapCoords destination){
        processingFlag = true;
        this.destination = destination;
        processSystem();
    }

}
