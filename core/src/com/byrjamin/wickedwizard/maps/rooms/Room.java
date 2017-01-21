package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomGround;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomPlatform;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomWall;
import com.byrjamin.wickedwizard.maps.rooms.spawns.RoomEnemyUpdater;
import com.byrjamin.wickedwizard.maps.rooms.spawns.RoomEnemyWaves;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.entity.player.Wizard;
import com.byrjamin.wickedwizard.entity.enemies.Enemy;

/**
 * Class is currently a stand-in for a future 'Room' Class
 * The idea behind this class is that Arenas/Rooms will decide their layouts, when they enemies spawn
 * and where the wizard is placed inside them
 */
public abstract class Room {

    public float WIDTH = MainGame.GAME_WIDTH;
    public float HEIGHT = MainGame.GAME_HEIGHT;

    public float TILE_SIZE = MainGame.GAME_WIDTH / 10;

    private float WALLWIDTH = Measure.units(5);

    private float tile_height;
    private float tile_width;

    private float x = 0;
    private float y = 0;

    private float[] sectionCenters;

    protected Array<Item> items = new Array<Item>();
    protected Array<RoomExit> roomExits = new Array<RoomExit>();
    protected Array<RoomWall> roomWalls = new Array<RoomWall>();

    private RoomEnemyUpdater roomEnemyUpdater;
    protected Wizard wizard = new Wizard(WALLWIDTH, 0);

    private RoomWall leftWall = new RoomWall(0, 0, WALLWIDTH, HEIGHT);
    private RoomWall rightWall = new RoomWall(WIDTH - WALLWIDTH, 0, WALLWIDTH, HEIGHT);;

    private long seed = 2;

    private Sprite topArrow;
    private Sprite bottomArrow;

    private boolean bottom = false;


    private Array<Rectangle> groundBoundaries = new Array<Rectangle>();
    private Array<RoomPlatform> platforms = new Array<RoomPlatform>();

    private RoomEnemyWaves roomEnemyWaves;
    private Array<Sprite> exits = new Array<Sprite>();

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


    private RoomBackground roomBackground;
    protected RoomGround roomGround;

    public Room(){
        roomEnemyUpdater = new RoomEnemyUpdater();
        roomGround = new RoomGround(PlayScreen.atlas.findRegion("brick"), this,WIDTH, Measure.units(10), false);
        roomEnemyWaves = new RoomEnemyWaves(this);
        sectionSetup();

        platforms.add(new RoomPlatform(0, groundHeight() + (HEIGHT - groundHeight()) / 2, WIDTH, Measure.units(4)));
        state = STATE.ENTRY;
        entry_point = ENTRY_POINT.LEFT;
        roomBackground = new RoomBackground(PlayScreen.atlas.findRegions("backgrounds/wall"), 0, 0 , this.WIDTH, this.HEIGHT);

        roomWalls.add(leftWall);
        roomWalls.add(rightWall);

        groundBoundaries.addAll(roomGround.getBounds());
        groundBoundaries.addAll(platforms);
        /* for(RoomWall wall : roomWalls){
            boundaries.add(wall.getBounds());
        }*/
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
    }

    public void update(float dt, OrthographicCamera gamecam){
        wizard.update(dt, gamecam, this);

        if(state == STATE.ENTRY) {

            boolean inPosition = false;

            switch(entry_point){
                case RIGHT:
                   // wizard.move(sectionCenters[(sectionCenters.length - 1)]);
                    inPosition = !wizard.isFalling() || wizard.getCenterX() < sectionCenters[(sectionCenters.length - 1)];
                    if(inPosition) {
                        wizard.land();
                    }
                   // inPosition = wizard.getCenterX() == sectionCenters[(sectionCenters.length - 1)];
                    System.out.println("Trying to move right");
                    break;
                case LEFT:
                  //  wizard.move(sectionCenters[0]);
                    inPosition = !wizard.isFalling() || wizard.getCenterX() > sectionCenters[(sectionCenters.length - 1)];
                    if(inPosition){
                        wizard.land();
                    }
                    //inPosition = wizard.getCenterX() == sectionCenters[0];
                    //System.out.println("Trying to move left");
                    break;
                case UP:
                    inPosition = !wizard.isFalling();
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
                    for(RoomExit r : roomExits){
                        r.unlock();
                    }
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

        if(state == STATE.UNLOCKED) {
            for (RoomExit r : roomExits) {
                if (r.hasEntered(wizard.getBounds())) {
                    state = STATE.EXIT;
                    exit_point = r.getExit();
                }
            }

        }

        for (RoomPlatform r : platforms) {
            r.update(wizard);
        }

        for (RoomExit r : roomExits) {
            r.update(dt);
        }

        doorCollisionCheck(wizard);
        wallCollisionCheck(wizard, leftWall.getBounds());
        wallCollisionCheck(wizard, rightWall.getBounds());
        groundsCollisionCheck(wizard);

    }

    public void draw(SpriteBatch batch){

        roomBackground.draw(batch);
        roomEnemyUpdater.draw(batch);

        for(Item item : items) {
            item.draw(batch);
            BoundsDrawer.drawBounds(batch, item.getBoundingRectangle());
        }

        for(RoomExit r : roomExits){
            r.draw(batch);
        }

/*        for(RoomWall r : roomWalls){
            r.draw(batch);
            //System.out.println("WALL DRAWIN");
        }*/

        rightWall.draw(batch);
        leftWall.draw(batch);

        BoundsDrawer.drawBounds(batch, groundBoundaries);

        wizard.draw(batch);

        roomGround.draw(batch);

/*        if(platforms.size != 0) {
            BoundsDrawer.drawBounds(batch, platforms);
        }*/

        if(state == STATE.UNLOCKED) {
            for(Sprite arrow : exits){
                arrow.draw(batch);
                BoundsDrawer.drawBounds(batch, arrow.getBoundingRectangle());
            }
        }

    }

    public void enterRoom(Wizard w, ENTRY_POINT entry_point){

        this.wizard = w;
        wizard.setCurrentState(Wizard.STATE.IDLE);
        wizard.cancelDash();
        state = STATE.ENTRY;
        this.entry_point = entry_point;
        switch(entry_point){
            case LEFT:
                wizard.setX(WALLWIDTH);
                break;
            case RIGHT:
                wizard.setX((WIDTH - WALLWIDTH) - wizard.WIDTH);
                break;
            case UP:
                wizard.setY((HEIGHT - WALLWIDTH) - wizard.HEIGHT);
                break;
            case DOWN:
                wizard.setY(groundHeight());
                break;
        }
    }

    public boolean isExitTransitionFinished(){
        return state == STATE.EXIT;
    }


    public boolean tapArrow(float input_x, float input_y){
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

    public boolean isTouchingPlatform(float x, float y){

        for(RoomPlatform r : platforms){
            if(r.contains(x, y)) return true;
        }

        return false;
    }

    private void groundsCollisionCheck(Wizard wizard) {

        boolean fall = false;

        for(RoomPlatform platform : platforms){
            if (platform.overlaps(wizard.getBounds())){
                wizard.land();
                wizard.setY(platform.getY() + platform.getHeight());
                return;
                //System.out.println("LAND THOUGH");
            } else {
                //wizard.fall();
            }
        }



        for(Rectangle rect : roomGround.getBounds()){
            if(rect.overlaps(wizard.getBounds())){
                wizard.land();
                wizard.setY(rect.getY() + rect.getHeight());
                return;
            } else {
                wizard.fall();
            }
        }


    }

    public void doorCollisionCheck(Wizard w){
        for(RoomExit exit : roomExits){
            if(!exit.isUnlocked() && state != STATE.ENTRY){
                if(exit.getBound().overlaps(wizard.getBounds())) {
                    Rectangle r1 = w.getBounds();
                    Rectangle r2 = exit.getBound();
                    //Hit was on left
                    if(r1.x < r2.x) {
                        r1.x = r2.x - wizard.WIDTH;
                    } else if(r1.x > r2.x) {
                        r1.x = r2.x + r2.getWidth();
                    }
                    w.cancelDash();
                    wizard.setX(r1.x);
                }
            }
        }
    }

    public void wallCollisionCheck(Rectangle entityBound, Rectangle wallBound){
        if(wallBound.overlaps(entityBound)){
            if(entityBound.x < wallBound.x) { //Hit was on left
                entityBound.setX(wallBound.x - entityBound.width);
            } else if(entityBound.x > wallBound.x) {//Hit was on right
                entityBound.setX(wallBound.x + wallBound.getWidth());
            }
        }
    }

    public void wallCollisionCheck(Wizard w, Rectangle wallBound){
        if(wallBound.overlaps(w.getBounds())){
            if(w.getX() < wallBound.x) { //Hit was on left
                w.setX(wallBound.x - w.WIDTH);
            } else if(w.getX() > wallBound.x) {//Hit was on right
                w.setX(wallBound.x + wallBound.getWidth());
            }
            w.cancelDash();
        }
    }

    public void setUpBoundaries() {
        groundBoundaries = new Array<Rectangle>();
        groundBoundaries.addAll(roomGround.getBounds());
        groundBoundaries.addAll(platforms);
    }

    public void addLeftExit() {
        roomExits.add(new RoomExit(0, groundHeight(), WALLWIDTH, Measure.units(20),EXIT_POINT.LEFT,false));
        leftWall = new RoomWall(0, groundHeight() + Measure.units(20), WALLWIDTH, HEIGHT);
    }

    public void addRightExit() {
        roomExits.add(new RoomExit(WIDTH - WALLWIDTH, groundHeight(), WALLWIDTH, Measure.units(20),EXIT_POINT.RIGHT,false));
        rightWall = new RoomWall(WIDTH - WALLWIDTH, groundHeight() + Measure.units(20), WALLWIDTH, HEIGHT);
    }

    public void addTopExit() {
        roomExits.add(new RoomExit((WIDTH / 2) - Measure.units(10),HEIGHT - WALLWIDTH, Measure.units(20), WALLWIDTH,EXIT_POINT.UP,true));
    }

    public void setBottomExit() {
        roomExits.add(new RoomExit((WIDTH / 2) - Measure.units(10),0 + WALLWIDTH / 2, Measure.units(20), WALLWIDTH,EXIT_POINT.DOWN,true));
        roomGround = new RoomGround(PlayScreen.atlas.findRegion("brick"), this, WIDTH, 200, true);
    }

    public void lock() {
        state = STATE.LOCKED;
        for(RoomExit r : roomExits){
            r.lock();
            r.lockAnimation();
        }
    }

    public void unlock() {
        state = STATE.UNLOCKED;
        for(RoomExit r : roomExits){
            r.unlock();
            r.unlockAnimation();
        }
    }

    public boolean inTransition(){
        return state == STATE.ENTRY || state == STATE.EXIT;
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

    public Array<Rectangle> getGroundBoundaries() {
        return groundBoundaries;
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

    public Array<RoomExit> getRoomExits() {
        return roomExits;
    }

    public boolean isUnlocked(){
        return state == state.UNLOCKED;
    }

    public float[] getSectionCenters() {
        return sectionCenters;
    }
}
