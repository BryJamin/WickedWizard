package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.sprites.Wizard;
import com.byrjamin.wickedwizard.sprites.enemies.Blob;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;

import java.util.Random;

/**
 * Class is currently a stand-in for a future 'Room' Class
 * The idea behind this class is that Arenas/Rooms will decide their layouts, when they enemies spawn
 * and where the wizard is placed inside them
 */
public class Arena {

    private EnemySpawner enemySpawner;
    private Wizard wizard;

    public enum STATE {
        LOCKED, UNLOCKED
    }


    public STATE arenaState;

    public Arena(){
        wizard = new Wizard();
        stage3();
    }



    //A room with one turret that spawns on the left
    public void stage1(){
        wizard = new Wizard();
        enemySpawner = new EnemySpawner();
        enemySpawner.spawnTurret(0, MainGame.GAME_HEIGHT - MainGame.GAME_UNITS * 10);
    }

    //A room with one turret that spawns on the right and one blob on the left
    public void stage2(){
        wizard = new Wizard();
        enemySpawner = new EnemySpawner();
        enemySpawner.spawnBlob(new Blob(0,500));
        enemySpawner.spawnTurret(MainGame.GAME_WIDTH - MainGame.GAME_UNITS * 10, MainGame.GAME_HEIGHT - MainGame.GAME_UNITS * 10);
    }

    //A room with two blobs on both side and the wizard position is moved.
    public void stage3(){
        wizard.getSprite().setPosition(MainGame.GAME_WIDTH / 2, 1000);
        enemySpawner = new EnemySpawner();
        enemySpawner.spawnBlob(new Blob(0,500));
        enemySpawner.spawnBlob(new Blob(MainGame.GAME_WIDTH, 500));
    }




    public void update(float dt){
        wizard.update(dt);
        enemySpawner.update(dt, wizard);
        if(enemySpawner.areAllEnemiesKilled()){
            arenaState = STATE.UNLOCKED;
        } else {
            arenaState = STATE.LOCKED;
        }
    }


    public void draw(SpriteBatch batch){
        wizard.draw(batch);
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
        if(arenaState == STATE.UNLOCKED) {
            nextStage();
        }
    }


    public Array<Enemy> getEnemies() {
        return enemySpawner.getSpawnedEnemies();
    }


    public Wizard getWizard() {
        return wizard;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }
}
