package com.bryjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.factories.enemy.SpawnerFactory;

/**
 * Created by Home on 25/03/2017.
 */

public class SpawnerComponent extends Component{

    private enum type {
        RANDOM, ORDERED
    }

    public boolean isEndless = false;

    public int life = 1;

    public float spawnTime;
    public float resetTime;

    public float offsetX = 0;
    public float offsetY = 0;

    private Array<SpawnerFactory.Spawner> spawner = new Array<SpawnerFactory.Spawner>();

    public SpawnerComponent(){

    }

    public SpawnerComponent(Array<SpawnerFactory.Spawner> spawner, float spawnTime){
        this.spawner = spawner;
        this.spawnTime = spawnTime;
        this.resetTime = spawnTime;
    }

    public SpawnerComponent(float spawnTime, SpawnerFactory.Spawner... spawners){
        for(SpawnerFactory.Spawner s : spawners){
            spawner.add(s);
        }
        this.spawnTime = spawnTime;
        this.resetTime = spawnTime;
    }

    public Array<SpawnerFactory.Spawner> getSpawner() {
        return spawner;
    }


    public void addToSpawner(SpawnerFactory.Spawner spawner){
        this.spawner.add(spawner);
    }

}
