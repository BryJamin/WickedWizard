package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.enemy.bosses.BiggaBlobba;

/**
 * Created by Home on 24/12/2016.
 */
public class BattleRoom extends Room{

    private ArenaWaves arenaWaves;

    private int numberOfWaves = 3;

    public BattleRoom(){
        super();
        arenaWaves = new ArenaWaves(this);
        arenaWaves.nextWave(this.getEnemies());
    }



    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        if(numberOfWaves == 0){
            arenaState = STATE.UNLOCKED;
        } else {
            arenaState = STATE.LOCKED;
            if(getArenaSpawner().areAllEnemiesKilled()) {
                arenaWaves.nextWave(this.getEnemies());
                numberOfWaves--;
                System.out.println("NUMBER OF WAVES: " + numberOfWaves);
            }
        }


    }




    public void draw(SpriteBatch batch){
        super.draw(batch);
    }


/*    public void triggerNextStage(){
        if(arenaSpawner.areAllEnemiesKilled() && day.size != 0) {


            if(day.get(0) == EVENT.WAVE) {
                arenaWaves.nextWave(arenaSpawner.getSpawnedEnemies());
                day.removeIndex(0);
            } else if(day.get(0) == EVENT.ITEM *//*&& itemSprite == null*//*) {
                // wizard.applyItem(ig.getItem(seed));
               // System.out.println("inside");
               // spawnItem(ig.getItem(seed));
                //  day.removeIndex(0);
            } else if(day.get(0) == EVENT.BOSS) {
                arenaSpawner.getSpawnedEnemies().add(new BiggaBlobba(1100, 2000));
                day.removeIndex(0);

                //TODO figure out what happens when the boss is defeated
            }

        }
    }*/









}
