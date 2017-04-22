package com.byrjamin.wickedwizard.utils;

/**
 * Created by Home on 22/04/2017.
 */

public class WeightedObject<T> {

    private T t;
    private int weight;

    public WeightedObject(T t, int weight) {
        this.t = t;
        this.weight = weight;
    }

    public T obj() {
        return t;
    }

    public int getWeight() {
        return weight;
    }
}
