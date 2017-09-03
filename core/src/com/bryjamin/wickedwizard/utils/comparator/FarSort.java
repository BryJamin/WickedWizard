package com.bryjamin.wickedwizard.utils.comparator;

import com.bryjamin.wickedwizard.utils.MapCoords;

import java.util.Comparator;

/**
 * Created by Home on 15/07/2017.
 */

public class FarSort {

    private MapCoords center;

    public FarSort(MapCoords center){
        this.center = center;
    }

    //TODO takes center has TWO COMPARATORS INSIDE OF IT


    /**
     * Sorts MapCoords by highest to lowest distance from the center co-ordinate
     */
    public Comparator<MapCoords> HIGHEST_TO_LOWEST_DIST = new Comparator<MapCoords>() {
        @Override
        public int compare(MapCoords mapCoords1, MapCoords mapCoords2) {
            Integer i1 = getHighestDistance(mapCoords1);
            Integer i2 = getHighestDistance(mapCoords2);
            return i1 > i2?-1:(i1.equals(i2) ?0:1);
        }
    };


    public Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> DOOR_FAR_MAPCOORDS = new Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>() {
        @Override
        public int compare(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc1, com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc2) {
            return HIGHEST_TO_LOWEST_DIST.compare(dc1.leaveCoords, dc2.leaveCoords);
        }
    };


    public int getHighestDistance(MapCoords mapCoords){
        int x = Math.abs(mapCoords.getX() - center.getX());
        int y = Math.abs(mapCoords.getY() - center.getY());
        return x >= y  ? x : y;

    }



    public Comparator<MapCoords> LEFTMOST_DISTANCE = new Comparator<MapCoords>() {
        @Override
        public int compare(MapCoords mapCoords1, MapCoords mapCoords2) {
            Integer i1 = mapCoords1.getX();
            Integer i2 = mapCoords2.getX();
            return i1 < i2?-1:(i1.equals(i2) ?0:1);
        }
    };


    public Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> LEFTMOST_DISTANCE_DOORS = new Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>() {
        @Override
        public int compare(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc1, com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc2) {
            return BIGGEST_Y_VALUE_SORT.compare(dc1.leaveCoords, dc2.leaveCoords);
        }
    };


    public Comparator<MapCoords> RIGHTMOST_DISTANCE = new Comparator<MapCoords>() {
        @Override
        public int compare(MapCoords mapCoords1, MapCoords mapCoords2) {
            Integer i1 = mapCoords1.getX();
            Integer i2 = mapCoords2.getX();
            return i1 > i2?-1:(i1.equals(i2) ?0:1);
        }
    };


    public Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> RIGHTMOST_DISTANCE_DOORS = new Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>() {
        @Override
        public int compare(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc1, com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc2) {
            return RIGHTMOST_DISTANCE.compare(dc1.leaveCoords, dc2.leaveCoords);
        }
    };



    public Comparator<MapCoords> BIGGEST_Y_VALUE_SORT = new Comparator<MapCoords>() {
        @Override
        public int compare(MapCoords mapCoords1, MapCoords mapCoords2) {
            Integer i1 = mapCoords1.getY();
            Integer i2 = mapCoords2.getY();
            return i1 > i2?-1:(i1.equals(i2) ?0:1);
        }
    };


    public Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> SORT_DOORS_BY_LARGEST_Y = new Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>() {
        @Override
        public int compare(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc1, com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc2) {
            return BIGGEST_Y_VALUE_SORT.compare(dc1.leaveCoords, dc2.leaveCoords);
        }
    };



    public Comparator<MapCoords> SORT_COORDS_BY_LOWEST_Y = new Comparator<MapCoords>() {
        @Override
        public int compare(MapCoords mapCoords1, MapCoords mapCoords2) {
            Integer i1 = mapCoords1.getY();
            Integer i2 = mapCoords2.getY();
            return i1 < i2?-1:(i1.equals(i2) ?0:1);
        }
    };


    public Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent> SORT_DOORS_BY_LOWEST_Y = new Comparator<com.bryjamin.wickedwizard.ecs.components.object.DoorComponent>() {
        @Override
        public int compare(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc1, com.bryjamin.wickedwizard.ecs.components.object.DoorComponent dc2) {
            return SORT_COORDS_BY_LOWEST_Y.compare(dc1.leaveCoords, dc2.leaveCoords);
        }
    };


    public int compare(com.bryjamin.wickedwizard.ecs.components.object.DoorComponent mapCoords, com.bryjamin.wickedwizard.ecs.components.object.DoorComponent t1) {
        return 0;
    }

}
