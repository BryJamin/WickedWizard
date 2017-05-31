package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.object.AltarComponent;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.MapCoords;

import java.util.Stack;

/**
 * Created by Home on 13/03/2017.
 */

public class Arena {

    private float width;
    private float height;

    public enum RoomType {
        TRAP, BOSS, ITEM, SHOP, NORMAL
    }

    public RoomType roomType;

    private MapCoords startingCoords;

    private ArenaSkin arenaSkin;

    public Array<MapCoords> cotainingCoords = new Array<MapCoords>();
    public Array<MapCoords> adjacentCoords = new Array<MapCoords>();

    public Array<DoorComponent> doors = new Array<DoorComponent>();
    public Array<AltarComponent> altars = new Array<AltarComponent>();

    private Bag<Bag<Component>> bagOfEntities = new Bag<Bag<Component>>();

    public Arena(ArenaSkin arenaSkin, MapCoords... mapCoords) {
        this(RoomType.NORMAL, arenaSkin, mapCoords);
    }

    public Arena(RoomType roomType, ArenaSkin arenaSkin, MapCoords... mapCoords) {

        startingCoords = mapCoords[0];

        for(MapCoords m : mapCoords) {
            this.cotainingCoords.add(m);
        }

        this.roomType = roomType;
        this.arenaSkin = arenaSkin;
    }





    public Bag<Bag<Component>> getBagOfEntities() {
        return bagOfEntities;
    }

    public Array<MapCoords> getCotainingCoords() {
        return cotainingCoords;
    }

    public Array<MapCoords> getAdjacentCoords() {
        return adjacentCoords;
    }


    private Stack<Bag<Bag<Component>>> waves = new Stack<Bag<Bag<Component>>>();

    public void addWave(Bag<Component>... bags){
        Bag<Bag<Component>> bagOfBags = new Bag<Bag<Component>>();

        for(Bag<Component> b : bags){
            bagOfBags.add(b);
        }
        waves.add(bagOfBags);
    }


    public void addWave(Bag<Bag<Component>> bags){
        waves.add(bags);
    }

    public Stack<Bag<Bag<Component>>> getWaves() {
        return waves;
    }


    public void addDoor(Bag<Component> door){
        for(Component c : door){
            if(c instanceof DoorComponent){
                adjacentCoords.add(((DoorComponent) c).leaveCoords);
                doors.add((DoorComponent) c);
                bagOfEntities.add(door);
                return;
            }
        }
    }

    public void addAltar(Bag<Component> altar){
        for(Component c : altar){
            if(c instanceof AltarComponent){
                altars.add((AltarComponent) c);
                bagOfEntities.add(altar);
                return;
            }
        }
    }

    public Array<DoorComponent> getDoors() {
        return doors;
    }

    public void addEntity(Bag<Component> entityBag){
        if(BagSearch.contains(AltarComponent.class, entityBag)){
            altars.add(BagSearch.getObjectOfTypeClass(AltarComponent.class, entityBag));
        }
        bagOfEntities.add(entityBag);
    }

    public void addEntity(Bag<Component>... entityBags){
        for(Bag<Component> bag : entityBags) {
            if(BagSearch.contains(AltarComponent.class, bag)){
                altars.add(BagSearch.getObjectOfTypeClass(AltarComponent.class, bag));
            }
            bagOfEntities.add(bag);
        }
    }

    public Bag<Component> findBag(Component c){
        for(Bag<Component> bc : bagOfEntities){
            if(bc.contains(c)){
                return bc;
            }
        }
        return null;
    }


    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public MapCoords getStartingCoords() {
        return startingCoords;
    }

    public ArenaSkin getArenaSkin() {
        return arenaSkin;
    }
}
