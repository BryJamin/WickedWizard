package com.byrjamin.wickedwizard.archive.maps.rooms.spawns;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.archive.gameobject.enemies.Enemy;
import com.byrjamin.wickedwizard.utils.AnimationPacker;
import com.byrjamin.wickedwizard.utils.timer.StateTimer;

/**
 * Created by Home on 06/11/2016.
 */
public class RoomEnemyUpdater {

    private Array<Enemy> spawnedEnemies = new Array<Enemy>();
    private Array<Enemy> spawningEnemies = new Array<Enemy>();
    private Array<Array<Enemy>> enemyWaves = new Array<Array<Enemy>>();

    private StateTimer spawnTimer = new StateTimer(1.0f);
    Animation<TextureRegion> spawningAnimation;

    private float animationTime;

    public RoomEnemyUpdater(){
        spawnedEnemies = new Array<Enemy>();
        spawningAnimation = AnimationPacker.genAnimation(1.0f / 35f, "circle", Animation.PlayMode.LOOP_REVERSED);

    }

    /**
     * Updates all Enemies that have been spawned. If the enemy is dead it is removed from the array.
     * @param dt
     * @param a
     */
    public void update(float dt, com.byrjamin.wickedwizard.archive.maps.rooms.Room a){

        for(Enemy e : spawnedEnemies) {
            e.update(dt, a);
            if (e.getPosition().y + 500 < 0) {
                e.setState(Enemy.STATE.DYING);
            }
        }

        animationTime+=dt;

        if(spawningEnemies.size > 0){
            spawnTimer.update(dt);
            if(spawnTimer.isFinished()){
                spawnedEnemies.addAll(spawningEnemies);
                spawningEnemies.clear();
            }
        }

        if(enemyWaves.size > 0 && areAllEnemiesKilled() && spawningEnemies.size == 0) {

            System.out.println("INSIDE");
            spawnTimer.reset();
            spawningEnemies.addAll(enemyWaves.first());
            enemyWaves.removeIndex(0);
        }




    }

    public void draw(SpriteBatch batch){
/*        if(spawnTimer.isFinished()) {
            for (Enemy e : spawnedEnemies) {
                e.draw(batch);
                e.bulletDraw(batch);
            }
        } else {

        }*/

        for(Enemy e : spawnedEnemies) {
            e.draw(batch);
            e.bulletDraw(batch);
        }

        for(Enemy e : spawningEnemies) {
            batch.draw(spawningAnimation.getKeyFrame(animationTime), e.getCollisionBound().x, e.getCollisionBound().y, e.getCollisionBound().getWidth(), e.getCollisionBound().getHeight());
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


    public boolean areAllWavesKilled(){
        for(Enemy e : spawnedEnemies) {
            if(e.getState() == Enemy.STATE.ALIVE){
                return false;
            }
        }
        return enemyWaves.size == 0 && spawningEnemies.size == 0;
    }



    public Array<Enemy> getSpawnedEnemies() {
        return spawnedEnemies;
    }

    public void setSpawnedEnemies(Array<Enemy> spawnedEnemies) {
        this.spawnedEnemies = spawnedEnemies;
    }

    public void projectileHitCheck(int damage){

    }

    public void addWave(Array<Enemy> enemyWave){
        enemyWaves.add(enemyWave);
    }

    public void addSpawningEnemy(Enemy e){
        spawningEnemies.add(e);
    }





}
