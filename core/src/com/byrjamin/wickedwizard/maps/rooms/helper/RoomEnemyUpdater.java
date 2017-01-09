package com.byrjamin.wickedwizard.maps.rooms.helper;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.entity.enemies.Enemy;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 06/11/2016.
 */
public class RoomEnemyUpdater {

    private Array<Enemy> spawnedEnemies;

    public RoomEnemyUpdater(){
        spawnedEnemies = new Array<Enemy>();
    }

    /**
     * Updates all Enemies that have been spawned. If the enemy is dead it is removed from the array.
     * @param dt
     * @param a
     */
    public void update(float dt, Room a){
        for(Enemy e : spawnedEnemies){
                e.update(dt, a);
            if(e.getPosition().y + 500 < 0){
                e.setState(Enemy.STATE.DYING);
            }
        }
    }

    public void draw(SpriteBatch batch){
        for(Enemy e : spawnedEnemies){
            e.draw(batch);
            e.bulletDraw(batch);
        }
    }


    public boolean areAllEnemiesKilled(){

        for(Enemy e : spawnedEnemies) {
            if(e.getState() == Enemy.STATE.ALIVE){
                return false;
            }
        }

        return true;

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
