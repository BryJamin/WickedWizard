package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.enemy.bosses.BiggaBlobba;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.spelltypes.BlastWave;
import com.byrjamin.wickedwizard.spelltypes.Dispellable;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.item.ItemGenerator;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.player.Wizard;
import com.byrjamin.wickedwizard.enemy.Enemy;

/**
 * Class is currently a stand-in for a future 'Room' Class
 * The idea behind this class is that Arenas/Rooms will decide their layouts, when they enemies spawn
 * and where the wizard is placed inside them
 */
public class Room {

    public static EnemyBullets enemyBullets;
    private ArenaSpawner arenaSpawner;
    private ItemGenerator ig;
    private Wizard wizard;

    private Rectangle ground;

    private Array<Vector2> groundTileTextureCoords;
    private Sprite itemSprite;


    public float ARENA_WIDTH = MainGame.GAME_WIDTH;
    public float ARENA_HEIGHT = MainGame.GAME_HEIGHT;

    private float tile_height;
    private float tile_width;

    private long seed = 2;

    private Array<Rectangle> platforms;

    private ArenaWaves arenaWaves;

    int count = 0;

    public enum STATE {
        LOCKED, UNLOCKED
    }

    public enum EVENT {
        WAVE, ITEM, IMP, BOSS
    }

    Array<EVENT> day;


    public STATE arenaState;



    public Room(){
        enemyBullets = new EnemyBullets();
        arenaSpawner = new ArenaSpawner();
        ig = new ItemGenerator();
        wizard = new Wizard();
        ground = new Rectangle(0,0,ARENA_WIDTH, MainGame.GAME_UNITS * 10);
        genGroundCoords(ground.getWidth(), ground.getHeight(), 1);
        platforms = new Array<Rectangle>();
        platforms.add(ground);

        arenaWaves = new ArenaWaves(this);
        //arenaWaves.nextWave(0, arenaSpawner.getSpawnedEnemies());

/*        day = new Array<EVENT>();
        for(int i = 0; i < 2; i++){
            day.add(EVENT.WAVE);
        }

        day.insert(0, EVENT.BOSS);*/
        //day.insert(5, EVENT.ITEM);
        //day.add(EVENT.BOSS);
        //stage3();
    }


    public void update(float dt, OrthographicCamera gamecam){
        wizard.update(dt,gamecam, this);
        arenaSpawner.update(dt, this);



/*
        if(day.size == 0){
            arenaState = STATE.UNLOCKED;
        } else {
            arenaState = STATE.LOCKED;
        }
*/

        enemyBullets.update(dt, gamecam, this.getWizard());

        for(BlastWave b : wizard.getBlastWaves()){
            for(int i = 0; i < EnemyBullets.activeBullets.size; i++){
                if(b.collides(EnemyBullets.activeBullets.get(i).getSprite().getBoundingRectangle())){
                    EnemyBullets.activeBullets.get(i).dispellProjectile(b.getDispelDirection());
                }
            }
        }
/*
        if(day.size != 0) {
            triggerNextStage();
        }*/

    }


    public void draw(SpriteBatch batch){

        wizard.draw(batch);

        for(Vector2 v : groundTileTextureCoords){
            batch.draw(PlayScreen.atlas.findRegion("brick"), v.x, v.y, tile_width, tile_height);
        }


       // wizard.draw(batch);
        arenaSpawner.draw(batch);
        enemyBullets.draw(batch);

        //batch.draw(PlayScreen.atlas.findRegion("brick"), 0, 0, 200, 200);
        //batch.draw(PlayScreen.atlas.findRegion("brick"), 200, 0, 200, 200);

        if(itemSprite != null){
            itemSprite.draw(batch);
        }

    }


    public void genGroundCoords(float ground_width, float ground_height, float rows){

        groundTileTextureCoords = new Array<Vector2>();

        tile_height = ground_height / rows;
        tile_width = ground_height / rows;
        //Assuming were using square tiles here
        float columns = ground_width / tile_width;

        for(int i = 0; i < columns; i++){
            for(int j = 0; j < rows; j++){
                groundTileTextureCoords.add(new Vector2((i * tile_height),(j * tile_width)));
            }
        }



    }

/*    public void triggerNextStage(){
        if(arenaSpawner.areAllEnemiesKilled() && day.size != 0) {


            if(day.get(0) == EVENT.WAVE) {
                arenaWaves.nextWave(arenaSpawner.getSpawnedEnemies());
                day.removeIndex(0);
            } else if(day.get(0) == EVENT.ITEM && itemSprite == null) {
               // wizard.applyItem(ig.getItem(seed));
                System.out.println("inside");
                spawnItem(ig.getItem(seed));
              //  day.removeIndex(0);
            } else if(day.get(0) == EVENT.BOSS) {
                arenaSpawner.getSpawnedEnemies().add(new BiggaBlobba(1100, 2000));
                day.removeIndex(0);

                //TODO figure out what happens when the boss is defeated
            }

        }
    }*/

    private void spawnItem(Item ig) {
        itemSprite = PlayScreen.atlas.createSprite(ig.getSpriteName());
        itemSprite.setSize(MainGame.GAME_UNITS * 7, MainGame.GAME_UNITS * 7);
        itemSprite.setCenter(this.ARENA_WIDTH / 2, (this.ARENA_HEIGHT / 4) * 3);
    }


    public void itemGet(float input_x, float input_y){

        if(day.size != 0) {
            if(day.get(0) == EVENT.ITEM) {
                if (itemSprite.getBoundingRectangle().contains(input_x, input_y)) {
                    Item i = ig.getItem(seed);
                    wizard.applyItem(i);
                    itemSprite = null;
                    day.removeIndex(0);
                }
            }
        }
    }

    public Rectangle getOverlappingRectangle(Rectangle r){
        for(Rectangle rect : platforms){
            if (r.overlaps(rect)){
                return rect;
            }
        }
        return null;
    }

    public Array<Rectangle> getPlatforms() {
        return platforms;
    }

    public Array<Enemy> getEnemies() {
        return arenaSpawner.getSpawnedEnemies();
    }

    public ArenaSpawner getArenaSpawner() {
        return arenaSpawner;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public EVENT getcurrentEvent(){
        return day.get(0);
    }

    public float groundHeight(){
        return ground.getHeight();
    }


    public boolean isUnlocked(){
        return arenaState == STATE.UNLOCKED;
    }
}
