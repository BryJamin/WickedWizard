package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.archive.maps.MapCoords;

/**
 * Created by Home on 13/03/2017.
 */

public class Arena {

    private float width;
    private float height;

    public enum RoomType {
        BATTLE, BOSS, ITEM, SHOP
    }

    public RoomType roomType;

    private MapCoords startingCoords;
    public Array<MapCoords> cotainingCoords;
    public Array<MapCoords> adjacentCoords = new Array<MapCoords>();

    public Array<DoorComponent> doors = new Array<DoorComponent>();

    //private Bag<Bag<Component>> doors = new Bag<Bag<Component>>();
    private Bag<Bag<Component>> bagOfEntities = new Bag<Bag<Component>>();
    private Bag<Bag<Component>> doorBags = new Bag<Bag<Component>>();

    /**
     * The first co-ordinate of mapcoords is taken to be the initial drawing co-orindate
     * @param mapCoords
     */
    public Arena(Array<MapCoords> mapCoords) {
        startingCoords = mapCoords.get(0);
        this.cotainingCoords = mapCoords;
        roomType = RoomType.BATTLE;
    }

    public Arena(Array<MapCoords> mapCoords, RoomType roomType) {
        startingCoords = mapCoords.get(0);
        this.cotainingCoords = mapCoords;
        this.roomType = roomType;
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




    public void addDoor(Bag<Component> door){
        for(Component c : door){
            if(c instanceof DoorComponent){
                adjacentCoords.add(((DoorComponent) c).leaveCoords);
                doors.add((DoorComponent) c);
                doorBags.add(door);
                bagOfEntities.add(door);
                return;
            }
        }
    }

    public Array<DoorComponent> getDoors() {
        return doors;
    }

    public void addEntity(Bag<Component> entityBag){
        bagOfEntities.add(entityBag);
    }

    public void addEntity(Bag<Component>... entityBags){
        for(Bag<Component> bag : entityBags) {
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
}
