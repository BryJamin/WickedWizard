package com.bryjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;

/**
 * Created by Home on 13/03/2017.
 */

public class Arena {

    private float width;
    private float height;

    public enum ArenaType {
        TRAP, BOSS, ITEM, SHOP, NORMAL
    }

    public ArenaType arenaType;

    private com.bryjamin.wickedwizard.utils.MapCoords startingCoords;

    private ArenaSkin arenaSkin;

    public Array<com.bryjamin.wickedwizard.utils.MapCoords> cotainingCoords = new Array<com.bryjamin.wickedwizard.utils.MapCoords>();

    public Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> doors = new Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>();
    public Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> mandatoryDoors = new Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>();

    private Bag<Bag<Component>> bagOfEntities = new Bag<Bag<Component>>();

    public Arena(ArenaSkin arenaSkin, com.bryjamin.wickedwizard.utils.MapCoords... mapCoords) {
        this(ArenaType.NORMAL, arenaSkin, mapCoords);
    }

    public Arena(ArenaType arenaType, ArenaSkin arenaSkin, com.bryjamin.wickedwizard.utils.MapCoords... mapCoords) {

        startingCoords = mapCoords[0];

        for(com.bryjamin.wickedwizard.utils.MapCoords m : mapCoords) {
            this.cotainingCoords.add(m);
        }

        this.arenaType = arenaType;
        this.arenaSkin = arenaSkin;
    }

    public Arena(ArenaType arenaType, ArenaSkin arenaSkin, Array<com.bryjamin.wickedwizard.utils.MapCoords> mapCoords) {

        startingCoords = mapCoords.get(0);

        for(com.bryjamin.wickedwizard.utils.MapCoords m : mapCoords) {
            this.cotainingCoords.add(m);
        }

        this.arenaType = arenaType;
        this.arenaSkin = arenaSkin;
    }


    public void addCoords(com.bryjamin.wickedwizard.utils.MapCoords coords) {
        if(!this.cotainingCoords.contains(coords, false)) this.cotainingCoords.add(coords);
    }


    public Bag<Bag<Component>> getBagOfEntities() {
        return bagOfEntities;
    }

    public Array<com.bryjamin.wickedwizard.utils.MapCoords> getCotainingCoords() {
        return cotainingCoords;
    }

    private Queue<Bag<Bag<Component>>> waves = new Queue<Bag<Bag<Component>>>();

    public Arena addWave(Bag<Component>... bags){
        Bag<Bag<Component>> bagOfBags = new Bag<Bag<Component>>();

        for(Bag<Component> b : bags){
            bagOfBags.add(b);
        }
        waves.addLast(bagOfBags);

        return this;

    }


    public void addWave(Bag<Bag<Component>> bags){
        waves.addLast(bags);
    }

    public void shuffleWaves(){
        Array<Bag<Bag<Component>>> arrayOfWaves = new Array<Bag<Bag<Component>>>();
        for(Bag<Bag<Component>> wave : waves) arrayOfWaves.add(wave);

        waves.clear();
        arrayOfWaves.shuffle();

        for(Bag<Bag<Component>> wave : arrayOfWaves) waves.addLast(wave);
    }


    public Queue<Bag<Bag<Component>>> getWaves() {
        return waves;
    }

    public void addDoor(Bag<Component> door, boolean isMandatory){
        for(Component c : door){
            if(c instanceof com.bryjamin.wickedwizard.ecs.components.object.DoorComponent){
                doors.add((com.bryjamin.wickedwizard.ecs.components.object.DoorComponent) c);
                if(isMandatory) mandatoryDoors.add((com.bryjamin.wickedwizard.ecs.components.object.DoorComponent) c);
                bagOfEntities.add(door);
                return;
            }
        }

    }

    public void addDoor(Bag<Component> door){
        addDoor(door, false);
    }


    public Array<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> getDoors() {
        return doors;
    }

    public Arena addEntity(Bag<Component> entityBag){
        bagOfEntities.add(entityBag);
        return this;
    }

    public void addEntity(Bag<Component>... entityBags){
        for(Bag<Component> bag : entityBags) {
            bagOfEntities.add(bag);
        }
    }

    public void addEntity(Array<? extends Bag<Component>> entityBags){
        for(Bag<Component> bag : entityBags) {
            bagOfEntities.add(bag);
        }
    }

    Bag<Component> findBag(Component c){
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


    public void setStartingCoords(com.bryjamin.wickedwizard.utils.MapCoords startingCoords) {
        this.startingCoords = startingCoords;
    }

    public com.bryjamin.wickedwizard.utils.MapCoords getStartingCoords() {
        return startingCoords;
    }


    public void setArenaSkin(ArenaSkin arenaSkin) {
        this.arenaSkin = arenaSkin;
    }

    public ArenaSkin getArenaSkin() {
        return arenaSkin;
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag createArenaBag(){
        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        this.addEntity(bag);
        return bag;
    }


}
