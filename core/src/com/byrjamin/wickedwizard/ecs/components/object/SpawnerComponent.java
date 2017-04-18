package com.byrjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.enemy.SpawnerFactory;

/**
 * Created by Home on 25/03/2017.
 */

public class SpawnerComponent extends Component{

    private enum type {
        RANDOM, ORDERED
    }

    public boolean isEndless = false;

    public int life = 1;

    public float time;
    public float resetTime;

    public float offsetX = 0;
    public float offsetY = 0;



    private Bag<Component> spawnedEntity;

    private Array<SpawnerFactory.Spawner> spawner = new Array<SpawnerFactory.Spawner>();

    public SpawnerComponent(){

    }

    public SpawnerComponent(Bag<Component> spawnedEntity, float time){
        this.spawnedEntity = spawnedEntity;
        this.time = time;
        this.resetTime = time;
    }

    public SpawnerComponent(Array<SpawnerFactory.Spawner> spawner, float time){
        this.spawner = spawner;
        this.time = time;
        this.resetTime = time;
    }

    public SpawnerComponent(float time, SpawnerFactory.Spawner... spawners){
        for(SpawnerFactory.Spawner s : spawners){
            spawner.add(s);
        }
        this.time = time;
        this.resetTime = time;
    }

    public Bag<Component> getSpawnedEntity() {
        return spawnedEntity;
    }

    public Array<SpawnerFactory.Spawner> getSpawner() {
        return spawner;
    }

}
