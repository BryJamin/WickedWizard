package com.bryjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.ai.InCombatActionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.bryjamin.wickedwizard.ecs.components.object.GrappleableComponent;
import com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.bryjamin.wickedwizard.utils.BagSearch;
import com.bryjamin.wickedwizard.utils.MapCoords;

/**
 * Created by BB on 14/09/2017.
 */

public class MapCleaner {

    private DecorFactory decorFactory;


    public MapCleaner(DecorFactory decorFactory){
        this.decorFactory = decorFactory;
    }


    public void cleanArenas(Array<Arena> arenas){
        for(int i = 0; i < arenas.size; i++) {
            Arena a = arenas.get(i);
            cleanArena(a, arenas);
        }
    }

    public void cleanArena(Arena a, Array<Arena> arenas){
        for(int j = a.getDoors().size - 1; j >=0; j--) {//for (DoorComponent dc : a.getDoors()) {
            DoorComponent dc = a.getDoors().get(j);
            if (!findDoorWithinFoundRoom(dc, arenas)) {
                replaceDoorWithWall(dc, a);
            }
        }

    }

    private void replaceDoorWithWall(DoorComponent dc, Arena arena){

        Bag<Component> bag = arena.findBag(dc);
        if (BagSearch.contains(GrappleableComponent.class, bag) && BagSearch.contains(InCombatActionComponent.class, bag)) {
            arena.getBagOfEntities().remove(bag);
        } else {
            CollisionBoundComponent cbc = BagSearch.getObjectOfTypeClass(CollisionBoundComponent.class, bag);
            if(cbc != null) {
                arena.getBagOfEntities().remove(bag);
                arena.addEntity(decorFactory.wallBag(cbc.bound.x, cbc.bound.y, cbc.bound.getWidth(), cbc.bound.getHeight(), arena.getArenaSkin()));
            }
        }
        arena.doors.removeValue(dc, true);

    }



    private Arena findRoom(MapCoords mc, Array<Arena> arenas){
        for(Arena a : arenas){
            //System.out.println("Find room " + a.cotainingCoords.contains(mc, false));
            if(a.cotainingCoords.contains(mc, false)){
                return a;
            }
        }
        return null;
    }



    private boolean checkAdjacentDoorsContainCoordinates(Arena arena, MapCoords mapCoords){
        for(DoorComponent dc : arena.getDoors()){

            if(dc.leaveCoords.equals(mapCoords)){
                return true;
            }
        }
        return false;
    }


    private boolean findDoorWithinFoundRoom(DoorComponent dc, Array<Arena> arenas){
        Arena a = findRoom(dc.leaveCoords, arenas);
        if(a != null) {
            return checkAdjacentDoorsContainCoordinates(a, dc.currentCoords);
        }
        return false;
    }




}
