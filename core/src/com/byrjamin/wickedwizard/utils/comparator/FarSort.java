package com.byrjamin.wickedwizard.utils.comparator;

import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.utils.MapCoords;

import java.util.Comparator;

/**
 * Created by Home on 15/07/2017.
 */

public class FarSort implements Comparator<MapCoords> {

    private MapCoords center;

    public FarSort(MapCoords center){
        this.center = center;
    }

    //TODO takes center has TWO COMPARATORS INSIDE OF IT

    @Override
    public int compare(MapCoords mapCoords, MapCoords mapCoords2) {


        return 0;
    }

    public int getDistance(MapCoords mapCoords){

        int x = Math.abs(mapCoords.getX() - center.getX());
        int y = Math.abs(mapCoords.getY() - center.getY());

        return x >= y  ? x : y;

    }


    public int compare(DoorComponent mapCoords, DoorComponent t1) {
        return 0;
    }

}
