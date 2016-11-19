package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.byrjamin.wickedwizard.sprites.Wizard;

/**
 * Created by Home on 06/11/2016.
 */
public class EnemySpawner {

    private Array<Enemy> spawnedEnemies;


    public EnemySpawner(){
        spawnedEnemies = new Array<Enemy>();
    }


    public void spawnBlob(int posX, int posY){
        Blob b = new Blob(posX, posY);
        spawnedEnemies.add(b);
    }


    public void startSpawningBlobs(int x, int y){

        final int posX = x;
        final int posY = y;

        Timer.schedule(new Timer.Task(){
                           @Override
                           public void run() {
                               spawnBlob(posX, posY);
                           }
                       }, 0, 1
        );
    }

    public void update(float dt, Wizard wizard){
        for(int i = 0; i < spawnedEnemies.size; i++){
            if(spawnedEnemies.get(i).getHealth() <= 0){
                spawnedEnemies.removeIndex(i);
            }
        }

        for(Enemy e : spawnedEnemies){
            e.update(dt, wizard);
        }
    }

    public void draw(SpriteBatch batch){
        for(Enemy e : spawnedEnemies){
            e.draw(batch);
        }
    }


    public boolean hitScan(int damage, float posx, float posy){

        boolean isHit = false;

        for(int i = 0; i < spawnedEnemies.size; i++){
            System.out.println("inside");
            if(spawnedEnemies.get(i).getSprite().getBoundingRectangle().contains(posx, posy)){
                System.out.println("OW said Blob " + i);
                spawnedEnemies.get(i).reduceHealth(damage);
                isHit = true;
                System.out.println("My health is " + spawnedEnemies.get(i).getHealth() +" said Blob " + i);
            }
        }

        return isHit;

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
