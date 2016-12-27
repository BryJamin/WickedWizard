package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.enemy.enemies.Blob;
import com.byrjamin.wickedwizard.enemy.Enemy;
import com.byrjamin.wickedwizard.enemy.enemies.Turret;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 06/11/2016.
 */
public class ArenaSpawner {

    private Array<Enemy> spawnedEnemies;

    public ArenaSpawner(){
        spawnedEnemies = new Array<Enemy>();
    }

    public void spawnBlob(Blob b){
        spawnedEnemies.add(b);
    }

    public void spawnTurret(Turret t){
        spawnedEnemies.add(t);
    }

    /**
     * Updates all Enemies that have been spawned. If the enemy is dead it is removed from the array.
     * @param dt
     * @param a
     */
    public void update(float dt, Room a){
        for(Enemy e : spawnedEnemies){
            if(e.getState() == Enemy.STATE.DEAD){
                spawnedEnemies.removeValue(e, true);
            } else {
                e.update(dt, a);
            }
        }
    }

    public void draw(SpriteBatch batch){
        for(Enemy e : spawnedEnemies){
            e.draw(batch);
        }
    }


    public boolean areAllEnemiesKilled(){
        return spawnedEnemies.size == 0;
    }

    public Array<Enemy> getSpawnedEnemies() {
        return spawnedEnemies;
    }

    public void setSpawnedEnemies(Array<Enemy> spawnedEnemies) {
        this.spawnedEnemies = spawnedEnemies;
    }

    public void projectileHitCheck(int damage){

    }





}
