package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.sprites.Wizard;

/**
 * Created by Home on 06/11/2016.
 */
public class EnemySpawner {

    private Array<Enemy> spawnedEnemies;
    private Array<Enemy> dyingEnemies;


    public EnemySpawner(){
        spawnedEnemies = new Array<Enemy>();
        dyingEnemies = new Array<Enemy>();
    }


    public void spawnBlob(int posX, int posY){
        Blob b = new Blob(posX, posY);
        spawnedEnemies.add(b);
    }

    public void spawnTurret(int posX, int posY){
        Turret t = new Turret(posX, posY);
        spawnedEnemies.add(t);
    }


    public void startSpawningBlobs(int x, int y){

        final int posX = x;
        final int posY = y;

        Timer.schedule(new Timer.Task(){
                           @Override
                           public void run() {
                               spawnBlob(posX, posY);
                               spawnTurret(MainGame.GAME_WIDTH - MainGame.GAME_UNITS * 10, MainGame.GAME_HEIGHT - MainGame.GAME_UNITS * 10);

                           }
                       }, 0, 1
        );
    }

    public void update(float dt, Wizard wizard){
        for(Enemy e : spawnedEnemies){
            if(e.getState() == Enemy.STATE.DYING){
                dyingEnemies.add(e);
                spawnedEnemies.removeValue(e, true);
            } else {
                e.update(dt, wizard);
            }
        }

        for(Enemy e : dyingEnemies){
            if(e.getState() == Enemy.STATE.DEAD){
                dyingEnemies.removeValue(e, true);
            } else {
                e.update(dt, wizard);
            }
        }
    }

    public void draw(SpriteBatch batch){

        for(Enemy e : dyingEnemies){
            e.draw(batch);
        }

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
