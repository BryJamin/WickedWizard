package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.artemis.utils.Bag;

/**
 * Created by Home on 25/03/2017.
 */

public class SpawnerComponent extends Component{

    public float time;

    private Bag<Component> spawnedEntity;

    public SpawnerComponent(){

    }

    public SpawnerComponent(Bag<Component> spawnedEntity, float time){
        this.spawnedEntity = spawnedEntity;
        this.time = time;
    }

    public Bag<Component> getSpawnedEntity() {
        return spawnedEntity;
    }
}
