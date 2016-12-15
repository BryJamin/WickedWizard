package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.deck.cards.spelltypes.Projectile;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.item.ItemGenerator;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.Wizard;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;

/**
 * Class is currently a stand-in for a future 'Room' Class
 * The idea behind this class is that Arenas/Rooms will decide their layouts, when they enemies spawn
 * and where the wizard is placed inside them
 */
public class Arena {

    private ActiveBullets activeBullets;
    public static EnemyBullets enemyBullets;
    private EnemySpawner enemySpawner;
    private ItemGenerator ig;
    private Wizard wizard;

    private Rectangle ground;

    private Array<Vector2> groundTileTextureCoords;


    public float ARENA_WIDTH = MainGame.GAME_WIDTH;
    public float ARENA_HEIGHT = MainGame.GAME_HEIGHT;

    private float tile_height;
    private float tile_width;

    private long seed = 2;

    private Array<Rectangle> platforms;

    private EnemyWaves enemyWaves;

    int count = 0;


    public enum STATE {
        LOCKED, UNLOCKED
    }

    public enum EVENT {
        WAVE, ITEM, IMP, BOSS
    }

    Array<EVENT> day;


    public STATE arenaState;

    public Arena(){
        activeBullets = new ActiveBullets();
        enemyBullets = new EnemyBullets();
        enemySpawner = new EnemySpawner();
        ig = new ItemGenerator();
        wizard = new Wizard();

        ground = new Rectangle(0,0,ARENA_WIDTH, MainGame.GAME_UNITS * 10);

        genGroundCoords(ground.getWidth(), ground.getHeight(), 1);
        platforms = new Array<Rectangle>();
        platforms.add(ground);

        enemyWaves = new EnemyWaves(this);
        enemyWaves.nextWave(0, enemySpawner.getSpawnedEnemies());

        day = new Array<EVENT>();

        for(int i = 0; i < 5; i++){
            day.add(EVENT.WAVE);
        }

        day.add(EVENT.BOSS);

        day.insert(3, EVENT.ITEM);

        //stage3();
    }

    public void addProjectile(float x, float y) {
        if(getWizard().getReloader().isReady()){
            activeBullets.addProjectile(getWizard().generateProjectile(x, y));
        }
    }

    public void dispell(float velocityX, float velocityY) {
        if(Math.abs(velocityY)> 500){
            enemyBullets.dispellProjectiles(Projectile.DISPELL.VERTICAL);
        } else if(Math.abs(velocityX) > 500){
            enemyBullets.dispellProjectiles(Projectile.DISPELL.HORIZONTAL);
        }
    }


    public void update(float dt, OrthographicCamera gamecam){
        wizard.update(dt, this);
        enemySpawner.update(dt, this);
        if(enemySpawner.areAllEnemiesKilled()){
            arenaState = STATE.UNLOCKED;
        } else {
            arenaState = STATE.LOCKED;
        }
        activeBullets.update(dt, gamecam, this.getEnemies());
        enemyBullets.update(dt, gamecam, this.getWizard());

        triggerNextStage();

    }


    public void draw(SpriteBatch batch){
        activeBullets.draw(batch);
        wizard.draw(batch);
        enemySpawner.draw(batch);
        enemyBullets.draw(batch);

        batch.draw(PlayScreen.atlas.findRegion("brick"), 0, 0, 200, 200);
        batch.draw(PlayScreen.atlas.findRegion("brick"), 200, 0, 200, 200);

        for(Vector2 v : groundTileTextureCoords){
            batch.draw(PlayScreen.atlas.findRegion("brick"), v.x, v.y, tile_width, tile_height);
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

                System.out.println("x:" + (i * tile_height) + " y:" + (j * tile_width));

            }
        }



    }

    public void triggerNextStage(){
        if(arenaState == STATE.UNLOCKED && day.size != 0) {


            if(day.get(0) == EVENT.WAVE) {
                enemyWaves.nextWave(3, enemySpawner.getSpawnedEnemies());
                day.removeIndex(0);
            } else if(day.get(0) == EVENT.ITEM) {
                wizard.applyItem(ig.getItem(seed));
                day.removeIndex(0);
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

    public Array<Enemy> getEnemies() {
        return enemySpawner.getSpawnedEnemies();
    }


    public Wizard getWizard() {
        return wizard;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public float groundHeight(){
        return ground.getY();
    }
}
