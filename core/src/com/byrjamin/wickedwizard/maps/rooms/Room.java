package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomGround;
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomPlatform;
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomTransition;
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomEnemyUpdater;
import com.byrjamin.wickedwizard.maps.rooms.helper.RoomEnemyWaves;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.player.Wizard;
import com.byrjamin.wickedwizard.enemy.Enemy;

/**
 * Class is currently a stand-in for a future 'Room' Class
 * The idea behind this class is that Arenas/Rooms will decide their layouts, when they enemies spawn
 * and where the wizard is placed inside them
 */
public abstract class Room {

    public float WIDTH = MainGame.GAME_WIDTH;
    public float HEIGHT = MainGame.GAME_HEIGHT;

    private float tile_height;
    private float tile_width;

    private float x = 0;
    private float y = 0;

    private float[] sectionCenters;

    private int currentSection;

    private ShapeRenderer mapRenderer = new ShapeRenderer();

    protected Array<Item> items = new Array<Item>();

    private RoomEnemyUpdater roomEnemyUpdater;
    protected Wizard wizard;

    private Array<Vector2> groundTileTextureCoords;

    private long seed = 2;

    private boolean transition = false;

    private Sprite leftArrow;
    private Sprite rightArrow;
    private Sprite topArrow;
    private Sprite bottomArrow;

    private boolean bottom = false;


    private Array<Rectangle> boundaries;
    private Array<RoomPlatform> platforms = new Array<RoomPlatform>();

    private RoomEnemyWaves roomEnemyWaves;
    private Array<Sprite> exits = new Array<Sprite>();

    public enum STATE {
       ENTRY, LOCKED, UNLOCKED, EXIT
    }

    public STATE state;

    public enum DIRECTION {
        RIGHT, LEFT, UP, DOWN
    }

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
    private RoomGround roomGround;

    private TextureRegion groundTexture;



    public Room(){
        roomEnemyUpdater = new RoomEnemyUpdater();
        wizard = new Wizard(0, 0);
        boundaries = new Array<Rectangle>();

        roomGround = new RoomGround(PlayScreen.atlas.findRegion("brick"), this, 200, false);

        roomEnemyWaves = new RoomEnemyWaves(this);
        groundTexture = PlayScreen.atlas.findRegion("brick");

        sectionSetup();

        state = STATE.ENTRY;
        entry_point = ENTRY_POINT.LEFT;

       // roomTransition = new RoomTransition(WIDTH, HEIGHT);
       // roomTransition.enterFromLeft();

        roomBackground = new RoomBackground(PlayScreen.atlas.findRegions("backgrounds/wall"), 0, 0 , this.WIDTH, this.HEIGHT);

        boundaries.addAll(roomGround.getBounds());

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

            boolean inPosition = false;

            switch(entry_point){
                case RIGHT:
                    wizard.moveLeft(dt);
                    if(wizard.getCenterX() < sectionCenters[(sectionCenters.length - 1)]) {
                        wizard.setCenterX(sectionCenters[(sectionCenters.length - 1)]);
                        inPosition = true;
                    }
                    break;
                case LEFT:
                    wizard.moveRight(dt);
                    if(wizard.getCenterX() > sectionCenters[0]){
                        wizard.setCenterX(sectionCenters[0]);
                        inPosition = true;
                    }
                    break;
                case UP:
                    wizard.moveDown(dt);
                    if(wizard.getY() <= roomGround.getHeight()){
                        wizard.setY(roomGround.getHeight());
                        inPosition = true;
                    }
                    break;
                case DOWN:
                    wizard.moveUp(dt);
                    if(wizard.getY() >= roomGround.getHeight()){
                        wizard.setY(roomGround.getHeight());
                        inPosition = true;
                    }
                    break;

            }

                if(inPosition) {
                    state = STATE.UNLOCKED;
                }
           // }

        } else {

            roomEnemyUpdater.update(dt, this);

            for(Item item : items){
                if(wizard.getBounds().overlaps(item.getBoundingRectangle())){
                    if(!item.isDestroyed()) {
                        item.use(wizard);
                    }
                }

                if(item.isDestroyed()){
                    items.removeValue(item, true);
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
            } else if(exit_point == EXIT_POINT.DOWN){
                if(!wizard.isDashing()){
                    wizard.moveDown(dt);
                }
            } else if(exit_point == EXIT_POINT.UP) {
                if(!wizard.isDashing()) {
                    wizard.moveUp(dt);
                }
            }

            if (isWizardOfScreen()) {
              //  roomTransition.update(dt);
            }

            //System.out.println(wizard.getY());

        }

    }

    public void draw(SpriteBatch batch){

        roomBackground.draw(batch);
        roomEnemyUpdater.draw(batch);

        for(Item item : items) {
            item.draw(batch);
            BoundsDrawer.drawBounds(batch, item.getBoundingRectangle());
        }

        wizard.draw(batch);

        roomGround.draw(batch);

        if(platforms.size != 0) {
            BoundsDrawer.drawBounds(batch, platforms);
        }

        if(state == STATE.UNLOCKED) {
            for(Sprite arrow : exits){
                arrow.draw(batch);
                BoundsDrawer.drawBounds(batch, arrow.getBoundingRectangle());
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
                break;
            case UP:
                wizard.setY(HEIGHT);
                break;
            case DOWN:
                wizard.setY(0);
                break;

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

        if(rightArrow != null && state == STATE.UNLOCKED){
            if(rightArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exit_point = EXIT_POINT.RIGHT;
               // roomTransition.exitToRight();
               // System.out.println("INSIDE RIGHT ARROW METHOD");
                return true;
            }
        }


        if(leftArrow != null && state == STATE.UNLOCKED){
            if(leftArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exit_point = EXIT_POINT.LEFT;
               // roomTransition.exitToLeft();
               // System.out.println("INSIDE LEFT ARROW METHOD");
                return true;
            }
        }


        if(topArrow != null && state == STATE.UNLOCKED){
            if(topArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exit_point = EXIT_POINT.UP;
                return true;
            }
        }

        if(bottomArrow != null && state == STATE.UNLOCKED){
            if(bottomArrow.getBoundingRectangle().contains(input_x, input_y)){
                state = STATE.EXIT;
                exit_point = EXIT_POINT.DOWN;
                return true;
            }
        }

        return false;

    }


    public boolean isWizardOfScreen(){
        return (wizard.getX() > WIDTH) || wizard.getX() < -wizard.WIDTH || wizard.getY() + wizard.WIDTH < 0 || wizard.getY() > HEIGHT;
    }

    public Rectangle getOverlappingRectangle(Rectangle r){

        for(Rectangle rect : boundaries){
            if (r.overlaps(rect)){
                return rect;
            }
        }
        return null;
    }


    public void addLeftExit() {
        leftArrow = PlayScreen.atlas.createSprite("exit_arrow");
        leftArrow.setCenter(200, 500);
        leftArrow.setSize(Measure.units(10), Measure.units(10));
        leftArrow.setFlip(true, true);
        exits.add(leftArrow);
    }

    public void addRightExit() {
        rightArrow = PlayScreen.atlas.createSprite("exit_arrow");
        rightArrow.setCenter(1700, 500);
        rightArrow.setSize(Measure.units(10), Measure.units(10));
        exits.add(rightArrow);
    }

    public void addTopExit() {
        topArrow = PlayScreen.atlas.createSprite("exit_arrow");
        topArrow.setCenter(WIDTH / 2, 1000);
        topArrow.setSize(Measure.units(10), Measure.units(10));
        topArrow.setOriginCenter();
        topArrow.rotate((float) Math.toDegrees(Math.PI / 2));
        exits.add(topArrow);
    }

    public void setBottomExit() {
        bottomArrow = PlayScreen.atlas.createSprite("exit_arrow");
        bottomArrow.setCenter(WIDTH / 2 - bottomArrow.getWidth() / 2, 50);
        bottomArrow.setSize(Measure.units(5), Measure.units(5));
        bottomArrow.setOriginCenter();
        bottomArrow.rotate((float) Math.toDegrees(-Math.PI / 2));
        bottom = true;
        exits.add(bottomArrow);
        roomGround = new RoomGround(PlayScreen.atlas.findRegion("brick"), this, 200, true);
        boundaries = new Array<Rectangle>();
        boundaries.addAll(roomGround.getBounds());
        boundaries.addAll(platforms);
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

    public boolean isExitPointUp(){
        return exit_point == EXIT_POINT.UP;
    }

    public boolean isExitPointDown(){
        return exit_point == EXIT_POINT.DOWN;
    }

    public Array<Rectangle> getBoundaries() {
        return boundaries;
    }

    public Array<RoomPlatform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Array<RoomPlatform> platforms) {
        this.platforms = platforms;
    }

    public Array<Enemy> getEnemies() {
        return roomEnemyUpdater.getSpawnedEnemies();
    }

    public RoomEnemyUpdater getRoomEnemyUpdater() {
        return roomEnemyUpdater;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public float groundHeight(){
        return roomGround.getHeight();
    }


    public boolean isUnlocked(){
        return state == state.UNLOCKED;
    }

    public float[] getSectionCenters() {
        return sectionCenters;
    }
}
