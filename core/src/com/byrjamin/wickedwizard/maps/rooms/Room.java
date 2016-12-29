package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.maps.RoomTransitionAnim;
import com.byrjamin.wickedwizard.maps.rooms.helper.ArenaSpawner;
import com.byrjamin.wickedwizard.maps.rooms.helper.ArenaWaves;
import com.byrjamin.wickedwizard.enemy.EnemyBullets;
import com.byrjamin.wickedwizard.spelltypes.BlastWave;
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

    public float ARENA_WIDTH = MainGame.GAME_WIDTH;
    public float ARENA_HEIGHT = MainGame.GAME_HEIGHT;

    private float tile_height;
    private float tile_width;

    public static EnemyBullets enemyBullets;
    private ArenaSpawner arenaSpawner;
    private ItemGenerator ig;
    private Wizard wizard;

    private RoomTransitionAnim exitingAnimation;

    private Rectangle ground;

    private Array<Vector2> groundTileTextureCoords;
    private Sprite itemSprite;

    private long seed = 2;

    private boolean transition = false;

    private boolean leftExit;
    private boolean rightExit;
    private boolean topExit;
    private boolean bottomExit;

    private Sprite leftArrow;
    private Sprite rightArrow;
    private Sprite topArrow;
    private Sprite bottomArrow;


    private Array<Rectangle> platforms;

    private ArenaWaves arenaWaves;

    public enum STATE {
       ENTRY, LOCKED, UNLOCKED, EXIT
    }

    public STATE state;

    public enum ENTRY_POINT {
        RIGHT, LEFT, UP, DOWN
    }

    public ENTRY_POINT entry_point;

    public enum EXIT_POINT {
        RIGHT, LEFT, UP, DOWN
    }

    public EXIT_POINT exit_point;



    public enum EVENT {
        WAVE, ITEM, IMP, BOSS
    }

    Array<EVENT> day;



    public Room(){
        enemyBullets = new EnemyBullets();
        arenaSpawner = new ArenaSpawner();

        ig = new ItemGenerator();
        wizard = new Wizard(200, 400);
        ground = new Rectangle(0,0,ARENA_WIDTH, MainGame.GAME_UNITS * 10);
        genGroundCoords(ground.getWidth(), ground.getHeight(), 1);
        platforms = new Array<Rectangle>();
        platforms.add(ground);

        arenaWaves = new ArenaWaves(this);

        leftArrow = PlayScreen.atlas.createSprite("exit_arrow");
        leftArrow.setCenter(1700, 500);
        leftArrow.setSize(Measure.units(10), Measure.units(10));

        rightArrow = PlayScreen.atlas.createSprite("exit_arrow");
        rightArrow.setCenter(200, 500);
        rightArrow.setSize(Measure.units(10), Measure.units(10));
        rightArrow.setFlip(true, true);

        state = STATE.ENTRY;

        exitingAnimation = new RoomTransitionAnim(0, 0,
                -ARENA_WIDTH,0,
                ARENA_WIDTH, ARENA_HEIGHT
                );

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


        if(state == STATE.ENTRY) {
            exitingAnimation.update(dt);

            if(exitingAnimation.isFinished()){
                state = STATE.LOCKED;
            }

        } else {

            wizard.update(dt, gamecam, this);
            arenaSpawner.update(dt, this);
            enemyBullets.update(dt, gamecam, this.getWizard());

            for (BlastWave b : wizard.getBlastWaves()) {
                for (int i = 0; i < EnemyBullets.activeBullets.size; i++) {
                    if (b.collides(EnemyBullets.activeBullets.get(i).getSprite().getBoundingRectangle())) {
                        EnemyBullets.activeBullets.get(i).dispellProjectile(b.getDispelDirection());
                    }
                }
            }

            if (state == STATE.EXIT) {
                wizard.moveRight(dt);

                if (isWizardOfScreen()) {
                    exitingAnimation.update(dt);
                }

            }
        }

    }


    public void draw(SpriteBatch batch){

        wizard.draw(batch);
        arenaSpawner.draw(batch);
        enemyBullets.draw(batch);


        for(Vector2 v : groundTileTextureCoords){
            batch.draw(PlayScreen.atlas.findRegion("brick"), v.x, v.y, tile_width, tile_height);
        }

        if(state == STATE.UNLOCKED) {
            leftArrow.draw(batch);
        }

        if(itemSprite != null){
            itemSprite.draw(batch);
        }

        if(exitingAnimation != null){
            exitingAnimation.draw(batch);
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

    public boolean isExitTransitionFinished(){
        if(exitingAnimation != null){
            return exitingAnimation.isFinished();
        }
        return false;
    }



    public void tapArrow(float input_x, float input_y){

        if(leftExit && state == STATE.UNLOCKED){
            if(leftArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exitingAnimation = new RoomTransitionAnim(ARENA_WIDTH, 0,
                        0, 0,
                        ARENA_WIDTH, ARENA_HEIGHT);
            }
        }


        if(rightExit && state == STATE.UNLOCKED){
            if(rightArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exitingAnimation = new RoomTransitionAnim(-ARENA_WIDTH, 0,
                        0, 0,
                        ARENA_WIDTH, ARENA_HEIGHT);
            }
        }

    }


    public boolean isWizardOfScreen(){
        return (wizard.getX() > ARENA_WIDTH) || wizard.getX() < 0;
    }

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



    public void exitRight(){

    }


    public void setLeftExit(boolean leftExit) {
        this.leftExit = leftExit;
    }

    public void setRightExit(boolean rightExit) {
        this.rightExit = rightExit;
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
        return state == state.UNLOCKED;
    }
}
