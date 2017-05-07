package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.MapCoords;

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

    //private Bag<Bag<Component>> doors = new Bag<Bag<Component>>();
    private Bag<Bag<Component>> bagOfEntities = new Bag<Bag<Component>>();
    private Bag<Bag<Component>> doorBags = new Bag<Bag<Component>>();

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

    public ArenaSkin getArenaSkin() {
        return arenaSkin;
    }
}
