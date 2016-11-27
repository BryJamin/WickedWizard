package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.sprites.Wizard;
import com.byrjamin.wickedwizard.sprites.enemies.Blob;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;
import com.byrjamin.wickedwizard.sprites.enemies.Turret;

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


    public void spawnBlob(float posX, float posY){
        Blob b = new Blob(posX, posY);
        spawnedEnemies.add(b);
    }

    public void spawnBlob(Blob b){
        spawnedEnemies.add(b);
    }

    public void spawnTurret(float posX, float posY){
        Turret t = new Turret(posX, posY);
        spawnedEnemies.add(t);
    }

    public void spawnTurret(Turret t){
        spawnedEnemies.add(t);
    }


    public void startSpawningBlobs(float x, float y){

        spawnBlob(x, y);
        spawnTurret(MainGame.GAME_WIDTH - MainGame.GAME_UNITS * 10, MainGame.GAME_HEIGHT - MainGame.GAME_UNITS * 10);
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


    public boolean areAllEnemiesKilled(){
        return dyingEnemies.size == 0 && spawnedEnemies.size == 0;
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
