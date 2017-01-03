package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomTransition;
import com.byrjamin.wickedwizard.maps.rooms.helper.ArenaSpawner;
import com.byrjamin.wickedwizard.maps.rooms.helper.ArenaWaves;
import com.byrjamin.wickedwizard.enemy.EnemyBullets;
import com.byrjamin.wickedwizard.item.ItemGenerator;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.player.Wizard;
import com.byrjamin.wickedwizard.enemy.Enemy;
import com.byrjamin.wickedwizard.spelltypes.blastwaves.DispelWave;

/**
 * Class is currently a stand-in for a future 'Room' Class
 * The idea behind this class is that Arenas/Rooms will decide their layouts, when they enemies spawn
 * and where the wizard is placed inside them
 */
public class Room {

    public float WIDTH = MainGame.GAME_WIDTH;
    public float HEIGHT = MainGame.GAME_HEIGHT;

    private float tile_height;
    private float tile_width;

    private float x = 0;
    private float y = 0;

    private float[] sectionCenters;

    private int currentSection;

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
    private RoomBackground roomBackground;


    public enum EVENT {
        WAVE, ITEM, IMP, BOSS
    }

    private TextureRegion groundTexture;

    Array<EVENT> day;



    public Room(){
        enemyBullets = new EnemyBullets();
        arenaSpawner = new ArenaSpawner();

        ig = new ItemGenerator();
        wizard = new Wizard(0, 0);
        ground = new Rectangle(0,0, WIDTH, 200);
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


        groundTexture = PlayScreen.atlas.findRegion("brick");
        //Bleeding.fixBleeding(groundTexture);

        sectionSetup();

        state = STATE.ENTRY;
        entry_point = ENTRY_POINT.LEFT;

       // roomTransition = new RoomTransition(WIDTH, HEIGHT);
       // roomTransition.enterFromLeft();

        roomBackground = new RoomBackground(PlayScreen.atlas.findRegions("background/brick"), 0, 0 + ground.getHeight(), this.WIDTH, this.HEIGHT - ground.getHeight());

    }

    /**
     * This assumes each arena has 3 sections. (LEFT, MIDDLE, RIGHT)
     * A player can dodge between these 3 sections.
     */
    public void sectionSetup(){

        sectionCenters = new float[3];
        float lengths = WIDTH / 3;
        for(int i = 0; i < sectionCenters.length; i++){
            sectionCenters[i] = (i * lengths) + (lengths / 2);
        }

        //TODO this only really works if the length of the array is odd.
        currentSection = (sectionCenters.length - 1) / 2;
    }

    public void update(float dt, OrthographicCamera gamecam){

        wizard.update(dt, gamecam, this);

        if(state == STATE.ENTRY) {

           // roomTransition.update(dt);

           // if(roomTransition.isFinished()){

                boolean inPosition = false;

                if(entry_point == ENTRY_POINT.RIGHT){
                    wizard.moveLeft(dt);

                    if(wizard.getCenterX() < sectionCenters[(sectionCenters.length - 1)]){
                        wizard.setCenterX(sectionCenters[(sectionCenters.length - 1)]);
                        currentSection = sectionCenters.length - 1;
                        inPosition = true;
                    }

                } else if(entry_point == ENTRY_POINT.LEFT){
                    wizard.moveRight(dt);

                    if(wizard.getCenterX() > sectionCenters[0]){
                        wizard.setCenterX(sectionCenters[0]);
                        currentSection = 0;
                        inPosition = true;
                    }

                }

                if(inPosition) {
                    state = STATE.UNLOCKED;
                }
           // }

        } else {
            
            arenaSpawner.update(dt, this);
            enemyBullets.update(dt, gamecam, this.getWizard());

            for (DispelWave d : wizard.getDispelWaves()) {
                for (int i = 0; i < EnemyBullets.activeBullets.size; i++) {
                    if (d.collides(EnemyBullets.activeBullets.get(i).getSprite().getBoundingRectangle())) {
                        EnemyBullets.activeBullets.get(i).dispellProjectile(d.getDispelDirection());
                    }
                }
            }

        }

        if (state == STATE.EXIT) {
            if(exit_point == EXIT_POINT.RIGHT) {
                if(!wizard.isDashing()) {
                    wizard.moveRight(dt);
                }
            } else if(exit_point == EXIT_POINT.LEFT){
                if(!wizard.isDashing()) {
                    wizard.moveLeft(dt);
                }
            }

            if (isWizardOfScreen()) {
              //  roomTransition.update(dt);
            }
        }

    }

    /**
     * Shifts the player to the next available section of the room based on where the input is placed.
     * This also triggers the player to 'dash' there.
     * @param input_x
     */
    public void shift(float input_x){
        if(state != STATE.ENTRY && state != STATE.EXIT && !wizard.isDashing() && !wizard.isFiring()) {
            if (input_x <= wizard.getCenterX()) {
                shiftWizardLeft();
            } else {
                shiftWizardRight();
            }
        }


        System.out.println("Current Section is: " + currentSection);

    }

    /**
     * Shifts the wizard to the next available left section of the room.
     */
    public void shiftWizardLeft(){
        if(currentSection != 0){
            currentSection--;
            wizard.dash(sectionCenters[currentSection]);
        }
    }

    /**
     * Shifts the player to the next avaliable right section of the room.
     */
    public void shiftWizardRight(){
        if(currentSection < sectionCenters.length - 1){
            currentSection++;
            wizard.dash(sectionCenters[currentSection]);
        }
    }


    public void draw(SpriteBatch batch){

        roomBackground.draw(batch);

        arenaSpawner.draw(batch);
        enemyBullets.draw(batch);
        wizard.draw(batch);

        for(Vector2 v : groundTileTextureCoords){
            batch.draw(groundTexture, v.x, v.y, tile_width, tile_height);
        }

        if(state == STATE.UNLOCKED) {

            if(leftExit){
                leftArrow.draw(batch);
                BoundsDrawer.drawBounds(batch, leftArrow.getBoundingRectangle());
            }

            if(rightExit){
                rightArrow.draw(batch);
                BoundsDrawer.drawBounds(batch, rightArrow.getBoundingRectangle());
            }

        }

        if(itemSprite != null){
            itemSprite.draw(batch);
        }

        if(roomTransition != null){
           // roomTransition.draw(batch);
        }

    }


    public void genGroundCoords(float ground_width, float ground_height, float rows){

        groundTileTextureCoords = new Array<Vector2>();

        tile_height = ground_height / rows;
        tile_width = ground_height / rows;
        //Assuming were using square tiles here
        float columns = ground_width / tile_width;

        for(float i = 0; i < columns; i++){
            for(float j = 0; j < rows; j++){
                groundTileTextureCoords.add(new Vector2(((i * tile_height - 5)),(j * tile_width - 5)));
            }
        }
    }


    public void enterRoom(ENTRY_POINT entry_point){
        state = STATE.ENTRY;
        this.entry_point = entry_point;

        switch(entry_point){
            case LEFT:
                wizard.setX(0);
               // roomTransition.enterFromLeft();

                break;
            case RIGHT:
                wizard.setX(WIDTH);
               // roomTransition.enterFromRight();
        }

    }

    /**
     * Checks to see if the Room Transition has finished it's animation.
     * @return
     */
    public boolean isRoomTransitionFinished(){
        if(roomTransition != null){
            return roomTransition.isFinished();
        }
        return false;
    }


    public boolean isExitTransitionFinished(){
        //return roomTransition.isFinished() && state == STATE.EXIT;
        return isWizardOfScreen() && state == STATE.EXIT;
    }


    public boolean tapArrow(float input_x, float input_y){

        if(rightExit && state == STATE.UNLOCKED){
            if(rightArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exit_point = EXIT_POINT.RIGHT;
               // roomTransition.exitToRight();
               // System.out.println("INSIDE RIGHT ARROW METHOD");
                return true;
            }
        }


        if(leftExit && state == STATE.UNLOCKED){
            if(leftArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exit_point = EXIT_POINT.LEFT;
               // roomTransition.exitToLeft();
               // System.out.println("INSIDE LEFT ARROW METHOD");
                return true;
            }
        }

        return false;

    }


    public boolean isWizardOfScreen(){
        return (wizard.getX() > WIDTH) || wizard.getX() < -wizard.WIDTH;
    }

    private void spawnItem(Item ig) {
        itemSprite = PlayScreen.atlas.createSprite(ig.getSpriteName());
        itemSprite.setSize(MainGame.GAME_UNITS * 7, MainGame.GAME_UNITS * 7);
        itemSprite.setCenter(this.WIDTH / 2, (this.HEIGHT / 4) * 3);
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

    public float[] getSectionCenters() {
        return sectionCenters;
    }
}
