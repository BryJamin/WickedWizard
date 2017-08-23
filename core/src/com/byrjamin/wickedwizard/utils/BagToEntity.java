package com.byrjamin.wickedwizard.utils;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;

/**
 * Created by Home on 31/05/2017.
 */

public class BagToEntity {
/*
    public static void bagsToEntities(World world, Bag<Bag<Component>> bags){
        for(Bag<Component> bag : bags){
            bagToEntity(world.createEntity(), bag);
        }
    }*/
/*    public static void bagsToEntities(World world, Bag<? extends T> bags){
        for(Bag<Component> bag : bags){
            bagToEntity(world.createEntity(), bag);
        }
    }*/

    public static Bag<Entity> bagsToEntities(World world, Bag<? extends Bag<Component>> bags){

        Bag<Entity> entityBag = new Bag<Entity>();

        for(Bag<Component> bag : bags){
            entityBag.add(bagToEntity(world.createEntity(), bag));
        }

        return entityBag;
    }

    public static Entity bagToEntity(Entity e, Bag<Component> bag){
        for(Component c : bag){
            e.edit().add(c);
        }
        return e;
    }

}
