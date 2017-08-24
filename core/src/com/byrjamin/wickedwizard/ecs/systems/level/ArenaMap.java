package com.byrjamin.wickedwizard.ecs.systems.level;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.utils.MapCoords;

/**
 * Created by Home on 28/05/2017.
 */

public class ArenaMap {

    private Arena currentArena;
    private Array<Arena> roomArray = new Array<Arena>();
    private OrderedSet<Arena> visitedArenas = new OrderedSet<Arena>();
    private OrderedSet<Arena> unvisitedButAdjacentArenas = new OrderedSet<Arena>();



    public ArenaMap(Arena currentArena) {
        this.currentArena = currentArena;
        this.roomArray = new Array<Arena>();
        roomArray.add(currentArena);
    }

    public ArenaMap(Arena currentArena, Array<Arena> roomArray) {
        this.currentArena = currentArena;
        this.roomArray = roomArray;
    }

    public ArenaMap(Arena startingArena, Arena... additionalArenas) {
        this.currentArena = startingArena;
        this.roomArray.add(startingArena);

        for(Arena a : additionalArenas) {
            this.roomArray.add(a);
        }
    }


    public ArenaMap(Arena currentArena, Array<Arena> roomArray, OrderedSet<Arena> visitedArenas, OrderedSet<Arena> unvisitedButAdjacentArenas) {
        this.currentArena = currentArena;
        this.roomArray = roomArray;
        this.visitedArenas = visitedArenas;
        this.unvisitedButAdjacentArenas = unvisitedButAdjacentArenas;
    }



    public Arena getArenaByMapCoords(MapCoords mapCoords){

        for(Arena a : roomArray){
            if(a.getCotainingCoords().contains(mapCoords, false)) {
                return a;
            }
        }

        return null;

    }




    public Arena getCurrentArena() {
        return currentArena;
    }

    public void setCurrentArena(Arena currentArena) {
        this.currentArena = currentArena;
    }

    public Array<Arena> getRoomArray() {
        return roomArray;
    }

    public void setRoomArray(Array<Arena> roomArray) {
        this.roomArray = roomArray;
    }

    public OrderedSet<Arena> getVisitedArenas() {
        return visitedArenas;
    }

    public void setVisitedArenas(OrderedSet<Arena> visitedArenas) {
        this.visitedArenas = visitedArenas;
    }

    public OrderedSet<Arena> getUnvisitedButAdjacentArenas() {
        return unvisitedButAdjacentArenas;
    }

    public void setUnvisitedButAdjacentArenas(OrderedSet<Arena> unvisitedButAdjacentArenas) {
        this.unvisitedButAdjacentArenas = unvisitedButAdjacentArenas;
    }
}
