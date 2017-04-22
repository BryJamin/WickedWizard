package com.byrjamin.wickedwizard.utils;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by Home on 22/04/2017.
 */

public class WeightedRoll<T> {

    private Array<WeightedObject<T>> weightedObjects;


    private Random random;

    public WeightedRoll(Array<WeightedObject<T>> weightedObjects, Random random){
        this.weightedObjects = weightedObjects;
        this.random = random;
    }


    public T roll(){

        Array<T> objects = new Array<T>();
        objects.setSize(weightedObjects.size);
        int[] percentages = new int[weightedObjects.size];

        for(int i = 0; i < weightedObjects.size; i++){
            objects.set(i, weightedObjects.get(i).obj());
            percentages[i] = weightedObjects.get(i).getWeight();
        }

        int totalWeight = 0;
        for(int i : percentages){
            totalWeight += i;
        }

        int roll = random.nextInt(totalWeight);

        T chosen = null;

        for(int i = 0; i < percentages.length; i++){
            if(roll < percentages[i]){
                chosen = objects.get(i);
                break;
            }
            roll -= percentages[i];
        }


        return chosen;


    }






}
