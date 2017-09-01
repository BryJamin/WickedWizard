package com.bryjamin.wickedwizard.utils;

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

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
