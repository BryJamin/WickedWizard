package com.byrjamin.wickedwizard.arenas;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.deck.cards.spelltypes.Projectile;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.Wizard;
import com.byrjamin.wickedwizard.sprites.enemies.Blob;
import com.byrjamin.wickedwizard.sprites.enemies.Enemy;
import com.byrjamin.wickedwizard.sprites.enemies.Turret;

import java.util.Random;

/**
 * Class is currently a stand-in for a future 'Room' Class
 * The idea behind this class is that Arenas/Rooms will decide their layouts, when they enemies spawn
 * and where the wizard is placed inside them
 */
public class Arena {

    private ActiveBullets activeBullets;
    public static EnemyBullets enemyBullets;
    private EnemySpawner enemySpawner;
    private Wizard wizard;

    private Rectangle ground;

    private Array<Vector2> groundTileTextureCoords;

    private float tile_height;
    private float tile_width;

    private Array<Rectangle> platforms;


    public enum STATE {
        LOCKED, UNLOCKED
    }


    public STATE arenaState;

    public Arena(){
        activeBullets = new ActiveBullets();
        enemyBullets = new EnemyBullets();
        enemySpawner = new EnemySpawner();
        wizard = new Wizard();

        ground = new Rectangle(0,0,MainGame.GAME_WIDTH, MainGame.GAME_UNITS * 10);

        genGroundCoords(ground.getWidth(), ground.getHeight(), 1);
        platforms = new Array<Rectangle>();
        platforms.add(ground);

        stage3();
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


    //A room with one turret that spawns on the left
    public void stage1(){
        wizard = new Wizard();
        enemySpawner = new EnemySpawner();
        Array<Enemy> k = new Array<Enemy>();
        k.add(new Turret(0, MainGame.GAME_HEIGHT - MainGame.GAME_UNITS * 10));
        enemySpawner.setSpawnedEnemies(k);
        //enemySpawner.spawnTurret(0, MainGame.GAME_HEIGHT - MainGame.GAME_UNITS * 10);
    }

    //A room with one turret that spawns on the right and one blob on the left
    public void stage2(){
        wizard = new Wizard();
        enemySpawner = new EnemySpawner();
        enemySpawner.spawnBlob(new Blob(0,1000));
        enemySpawner.spawnTurret(MainGame.GAME_WIDTH - MainGame.GAME_UNITS * 10, MainGame.GAME_HEIGHT - MainGame.GAME_UNITS * 10);
    }

    //A room with two blobs on both side and the wizard position is moved.
    public void stage3(){
        wizard = new Wizard();
        wizard.getSprite().setPosition(MainGame.GAME_WIDTH / 2, 1000);
        enemySpawner = new EnemySpawner();
        enemySpawner.spawnBlob(new Blob(0,1000));
        enemySpawner.spawnBlob(new Blob(MainGame.GAME_WIDTH, 500));
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

    public Rectangle getOverlappingRectangle(Rectangle r){
        for(Rectangle rect : platforms){
            if (r.overlaps(rect)){
                return rect;
            }
        }
        return null;
    }

    public float testinggravity(Rectangle r){

        for(Rectangle rect : platforms){
            if (r.overlaps(rect)){
                return rect.getY() + rect.getHeight();
            }
        }

        return r.getY() + r.getHeight();

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
