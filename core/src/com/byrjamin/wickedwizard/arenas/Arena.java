package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.sprites.Wizard;
import com.byrjamin.wickedwizard.sprites.enemies.Blob;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;

import java.util.Random;

/**
 * Created by Home on 27/11/2016.
 */
public class Arena {

    private EnemySpawner enemySpawner;

    public enum STATE {
        LOCKED, UNLOCKED
    }


    public STATE arenaState;

    public Arena(Wizard wizard){
        stage3();
    }



    public void stage1(){
        enemySpawner = new EnemySpawner();
        enemySpawner.spawnTurret(0, MainGame.GAME_HEIGHT - MainGame.GAME_UNITS * 10);
    }


    public void stage2(){
        enemySpawner = new EnemySpawner();
        enemySpawner.spawnBlob(new Blob(0,500));
        enemySpawner.spawnTurret(MainGame.GAME_WIDTH - MainGame.GAME_UNITS * 10, MainGame.GAME_HEIGHT - MainGame.GAME_UNITS * 10);
    }

    public void stage3(){
        enemySpawner = new EnemySpawner();
        enemySpawner.spawnBlob(new Blob(0,500));
        enemySpawner.spawnBlob(new Blob(MainGame.GAME_WIDTH, 500));
    }




    public void update(float dt, Wizard wizard){
        enemySpawner.update(dt, wizard);
        if(enemySpawner.areAllEnemiesKilled()){
            arenaState = STATE.UNLOCKED;
            nextStage();
        }
    }


    public void draw(SpriteBatch batch){
        enemySpawner.draw(batch);
    }

    public void nextStage(){
        Random random = new Random();

        int temp = random.nextInt(3) + 1;

        switch(temp){
            case 1: stage1();
                break;
            case 2: stage2();
                break;
            case 3: stage3();
                break;
            default:
                stage1();
        }

    }


    public void triggerNextStage(){

    }


    public Array<Enemy> getEnemies() {
        return enemySpawner.getSpawnedEnemies();
    }






}
