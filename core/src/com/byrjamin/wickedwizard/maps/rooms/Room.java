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
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomTransition;
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomTransitionAnim;
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


    private RoomTransition roomTransition;


    public enum EVENT {
        WAVE, ITEM, IMP, BOSS
    }

    Array<EVENT> day;



    public Room(){
        enemyBullets = new EnemyBullets();
        arenaSpawner = new ArenaSpawner();

        ig = new ItemGenerator();
        wizard = new Wizard(0, 0);
        ground = new Rectangle(0,0,ARENA_WIDTH, MainGame.GAME_UNITS * 10);
        genGroundCoords(ground.getWidth(), ground.getHeight(), 1);
        platforms = new Array<Rectangle>();
        platforms.add(ground);

        arenaWaves = new ArenaWaves(this);

        rightArrow = PlayScreen.atlas.createSprite("exit_arrow");
        rightArrow.setCenter(1700, 500);
        rightArrow.setSize(Measure.units(10), Measure.units(10));

        leftArrow = PlayScreen.atlas.createSprite("exit_arrow");
        leftArrow.setCenter(200, 500);
        leftArrow.setSize(Measure.units(10), Measure.units(10));
        leftArrow.setFlip(true, true);

        state = STATE.ENTRY;
        entry_point = ENTRY_POINT.LEFT;

        roomTransition = new RoomTransition(ARENA_WIDTH, ARENA_HEIGHT);
        roomTransition.enterFromLeft();

    }


    public void update(float dt, OrthographicCamera gamecam){

        wizard.update(dt, gamecam, this);

        if(state == STATE.ENTRY) {
            roomTransition.update(dt);

            if(roomTransition.isFinished()){

                boolean inPosition = false;

                if(entry_point == ENTRY_POINT.RIGHT){
                    wizard.moveLeft(dt);

                    if(wizard.getX() < 1600){
                        wizard.setX(1600);
                        inPosition = true;
                    }

                } else if(entry_point == ENTRY_POINT.LEFT){
                    wizard.moveRight(dt);

                    if(wizard.getX() > 200){
                        wizard.setX(200);
                        inPosition = true;
                    }

                }

                if(inPosition) {
                    state = STATE.UNLOCKED;
                }
            }

        } else {
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


                if(exit_point == EXIT_POINT.RIGHT) {
                    wizard.moveRight(dt);
                } else if(exit_point == EXIT_POINT.LEFT){
                    wizard.moveLeft(dt);
                }

                if (isWizardOfScreen()) {
                    roomTransition.update(dt);
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

        if(state != STATE.LOCKED) {

            if(leftExit){
                leftArrow.draw(batch);
            }

            if(rightExit){
                rightArrow.draw(batch);
            }

        }

        if(itemSprite != null){
            itemSprite.draw(batch);
        }

        if(roomTransition != null){
            roomTransition.draw(batch);
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


    public void enterRoom(ENTRY_POINT entry_point){
        state = STATE.ENTRY;
        this.entry_point = entry_point;

        switch(entry_point){
            case LEFT:
                wizard.setX(0);
                roomTransition.enterFromLeft();

                break;
            case RIGHT:
                wizard.setX(ARENA_WIDTH);
                roomTransition.enterFromRight();
        }

    }

    public boolean isExitTransitionFinished(){
        if(roomTransition != null){
            return roomTransition.isFinished();
        }
        return false;
    }



    public void tapArrow(float input_x, float input_y){

        if(rightExit && state == STATE.UNLOCKED){
            if(rightArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exit_point = EXIT_POINT.RIGHT;
                roomTransition.exitToRight();
            }
        }


        if(leftExit && state == STATE.UNLOCKED){
            if(leftArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exit_point = EXIT_POINT.LEFT;
                roomTransition.exitToLeft();
            }
        }

    }


    public boolean isWizardOfScreen(){
        return (wizard.getX() > ARENA_WIDTH) || wizard.getX() < -wizard.WIDTH;
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

    public EXIT_POINT getExit_point() {
        return exit_point;
    }

    public boolean isExitPointRight(){
        return exit_point == EXIT_POINT.RIGHT;
    }

    public boolean isExitPointLeft(){
        return exit_point == EXIT_POINT.LEFT;
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
