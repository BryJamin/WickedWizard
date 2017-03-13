package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.byrjamin.wickedwizard.maps.MapCoords;

/**
 * Created by Home on 13/03/2017.
 */

public class Arena {

    public MapCoords location = new MapCoords(0,0);
    private Bag<Bag<Component>> bagOfEntities;

    public Arena(MapCoords location, Bag<Bag<Component>> bagOfEntities) {
        this.location = location;
        this.bagOfEntities = bagOfEntities;
    }

    public Bag<Bag<Component>> getBagOfEntities() {
        return bagOfEntities;
    }
}
