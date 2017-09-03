package com.bryjamin.wickedwizard.ecs.systems.level;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.bryjamin.wickedwizard.utils.MapCoords;

/**
 * Created by Home on 28/05/2017.
 */

public class ArenaMap {

    private com.bryjamin.wickedwizard.factories.arenas.Arena currentArena;
    private Array<com.bryjamin.wickedwizard.factories.arenas.Arena> roomArray = new Array<com.bryjamin.wickedwizard.factories.arenas.Arena>();
    private OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena> visitedArenas = new OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena>();
    private OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena> unvisitedButAdjacentArenas = new OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena>();



    public ArenaMap(com.bryjamin.wickedwizard.factories.arenas.Arena currentArena) {
        this.currentArena = currentArena;
        this.roomArray = new Array<com.bryjamin.wickedwizard.factories.arenas.Arena>();
        roomArray.add(currentArena);
    }

    public ArenaMap(com.bryjamin.wickedwizard.factories.arenas.Arena currentArena, Array<com.bryjamin.wickedwizard.factories.arenas.Arena> roomArray) {
        this.currentArena = currentArena;
        this.roomArray = roomArray;
    }

    public ArenaMap(com.bryjamin.wickedwizard.factories.arenas.Arena startingArena, com.bryjamin.wickedwizard.factories.arenas.Arena... additionalArenas) {
        this.currentArena = startingArena;
        this.roomArray.add(startingArena);

        for(com.bryjamin.wickedwizard.factories.arenas.Arena a : additionalArenas) {
            this.roomArray.add(a);
        }
    }


    public ArenaMap(com.bryjamin.wickedwizard.factories.arenas.Arena currentArena, Array<com.bryjamin.wickedwizard.factories.arenas.Arena> roomArray, OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena> visitedArenas, OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena> unvisitedButAdjacentArenas) {
        this.currentArena = currentArena;
        this.roomArray = roomArray;
        this.visitedArenas = visitedArenas;
        this.unvisitedButAdjacentArenas = unvisitedButAdjacentArenas;
    }



    public com.bryjamin.wickedwizard.factories.arenas.Arena getArenaByMapCoords(MapCoords mapCoords){

        for(com.bryjamin.wickedwizard.factories.arenas.Arena a : roomArray){
            if(a.getCotainingCoords().contains(mapCoords, false)) {
                return a;
            }
        }

        return null;

    }




    public com.bryjamin.wickedwizard.factories.arenas.Arena getCurrentArena() {
        return currentArena;
    }

    public void setCurrentArena(com.bryjamin.wickedwizard.factories.arenas.Arena currentArena) {
        this.currentArena = currentArena;
    }

    public Array<com.bryjamin.wickedwizard.factories.arenas.Arena> getRoomArray() {
        return roomArray;
    }

    public void setRoomArray(Array<com.bryjamin.wickedwizard.factories.arenas.Arena> roomArray) {
        this.roomArray = roomArray;
    }

    public OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena> getVisitedArenas() {
        return visitedArenas;
    }

    public void setVisitedArenas(OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena> visitedArenas) {
        this.visitedArenas = visitedArenas;
    }

    public OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena> getUnvisitedButAdjacentArenas() {
        return unvisitedButAdjacentArenas;
    }

    public void setUnvisitedButAdjacentArenas(OrderedSet<com.bryjamin.wickedwizard.factories.arenas.Arena> unvisitedButAdjacentArenas) {
        this.unvisitedButAdjacentArenas = unvisitedButAdjacentArenas;
    }
}
