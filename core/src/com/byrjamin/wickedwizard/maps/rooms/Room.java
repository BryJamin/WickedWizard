package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.helper.collider.WizardCollider;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomGround;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomPlatform;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomTeleporter;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomWall;
import com.byrjamin.wickedwizard.maps.rooms.spawns.RoomEnemyUpdater;
import com.byrjamin.wickedwizard.maps.rooms.spawns.RoomEnemyWaves;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.entity.player.Wizard;
import com.byrjamin.wickedwizard.entity.enemies.Enemy;

import java.util.Random;

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
    protected Array<RoomTeleporter> roomTeleporters = new Array<RoomTeleporter>();
    protected Array<RoomWall> roomWalls = new Array<RoomWall>();

    private RoomEnemyUpdater roomEnemyUpdater;
    protected Wizard wizard = new Wizard(WALLWIDTH * 2, 600);

    private RoomWall leftWall;
    private RoomWall rightWall;
    private Array<RoomWall> ceiling = new Array<RoomWall>();
    private Array<RoomWall> ground = new Array<RoomWall>();

    private long seed = 2;

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

        leftWall= new RoomWall(0, groundHeight(), WALLWIDTH, HEIGHT, WALLWIDTH);
        rightWall = new RoomWall(WIDTH - WALLWIDTH,  groundHeight(), WALLWIDTH, HEIGHT, WALLWIDTH);
        ceiling.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        ground.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));

//        platforms.add(new RoomPlatform(0, groundHeight() + (HEIGHT - groundHeight()) / 2, WIDTH, Measure.units(4)));
        state = STATE.ENTRY;
        entry_point = ENTRY_POINT.LEFT;
        roomBackground = new RoomBackground(PlayScreen.atlas.findRegions("backgrounds/wall"), 0, 0 , this.WIDTH, this.HEIGHT);

        roomWalls.add(leftWall);
        roomWalls.add(rightWall);

        Random random = new Random();
        if(random.nextBoolean()){
            platforms.add(new RoomPlatform(WIDTH - WALLWIDTH * 9, HEIGHT / 2, WALLWIDTH * 9, WALLWIDTH));
        }



        groundBoundaries.addAll(roomGround.getBounds());
        groundBoundaries.addAll(platforms);
        System.out.println("PLATFORM SIZE IS: " + platforms.size);
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
                    inPosition = !wizard.isFalling() || wizard.getCenterX() < sectionCenters[(sectionCenters.length - 1)];
                    break;
                case LEFT:
                    inPosition = !wizard.isFalling() || wizard.getCenterX() > sectionCenters[(sectionCenters.length - 1)];
                    break;
                case UP:
                    inPosition = !wizard.isFalling();
                    break;
                case DOWN:
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

            for (RoomTeleporter r : roomTeleporters) {
                if (r.hasEntered(wizard.getBounds())) {
                    state = STATE.EXIT;
                    exit_point = r.getExit();
                    r.setActive(false);
                }
            }
        }

        for (RoomPlatform r : platforms) {
            r.update(wizard);
        }

        for (RoomExit r : roomExits) {
            r.update(dt);
        }

        doorCollisionCheck(wizard, dt);
        groundsCollisionCheck(wizard);
        wallCollisionCheck(wizard, leftWall.getBounds(), dt);
        wallCollisionCheck(wizard, rightWall.getBounds(), dt);

        for(RoomWall r : ceiling){
            wallCollisionCheck(wizard, r.getBounds(), dt);
        }

        for(RoomWall r : ground){
            wallCollisionCheck(wizard, r.getBounds(), dt);
        }


    }

    public void draw(SpriteBatch batch){

        roomBackground.draw(batch);
        for(RoomTeleporter rt: roomTeleporters){
            rt.draw(batch);
        }
        roomEnemyUpdater.draw(batch);

        for(Item item : items) {
            item.draw(batch);
            BoundsDrawer.drawBounds(batch, item.getBoundingRectangle());
        }

        for(RoomExit r : roomExits){
            r.draw(batch);
        }

        rightWall.draw(batch);
        leftWall.draw(batch);

        for(RoomWall r : ceiling){
            r.draw(batch);
        }

        for(RoomWall r : ground){
            r.draw(batch);
        }

        BoundsDrawer.drawBounds(batch, platforms);

        wizard.draw(batch);

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
        wizard.cancelMovementHorizontalSpeed();
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
                wizard.setY((HEIGHT - WALLWIDTH) - wizard.HEIGHT * 2);
                wizard.setVerticalVelocity(-Measure.units(50f));
                break;
            case DOWN:
                wizard.setY(groundHeight() + 50);
                break;
        }
    }

    public boolean isExitTransitionFinished(){
        return state == STATE.EXIT;
    }

    public boolean isWizardOfScreen(){
        return (wizard.getX() > WIDTH) || wizard.getX() < -wizard.WIDTH || wizard.getY() + wizard.WIDTH < 0 || wizard.getY() > HEIGHT;
    }

    private void groundsCollisionCheck(Wizard wizard) {
        for(RoomPlatform platform : platforms){
            if (platform.overlaps(wizard.getBounds())){
                wizard.resetGravity();
                wizard.setY(platform.getY() + platform.getHeight());
                return;
            } else if(Collider.isOnTop(wizard.getBounds(), platform)){
                if(platform.isCollisionOn()) {
                    wizard.setY(platform.getY() + platform.getHeight());
                }
                break;
            }
        }
    }

    public void doorCollisionCheck(Wizard w, float dt){
        for(RoomExit exit : roomExits){
            if(!exit.isUnlocked() && state != STATE.ENTRY){
                wallCollisionCheck(w, exit.getBound(), dt);
            }
        }
    }

    public void wallCollisionCheck(Wizard w, Rectangle wallBound , float dt){
        WizardCollider.collisionCheck(w, wallBound, dt);
    }

    public void setUpBoundaries() {
        groundBoundaries = new Array<Rectangle>();
        //groundBoundaries.addAll(roomGround.getBounds());

        groundBoundaries.addAll(platforms);
        roomWalls = new Array<RoomWall>();
        roomWalls.addAll(leftWall, rightWall);
        roomWalls.addAll(ceiling);
        roomWalls.addAll(ground);

        for(RoomWall roomWall : ground){
            groundBoundaries.add(roomWall.getBounds());
        }
    }

    public void addLeftExit() {
        roomExits.add(new RoomExit(0, groundHeight(), WALLWIDTH, Measure.units(20),EXIT_POINT.LEFT,false));
        leftWall = new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT, WALLWIDTH);
    }

    public void addRightExit() {
        roomExits.add(new RoomExit(WIDTH - WALLWIDTH, groundHeight(), WALLWIDTH, Measure.units(20),EXIT_POINT.RIGHT,false));
        rightWall = new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT, WALLWIDTH);
    }

    public void addTopExit() {
        roomTeleporters.add(new RoomTeleporter(WALLWIDTH * 8, HEIGHT - WALLWIDTH * 3,EXIT_POINT.UP, false));
    }

    public void setBottomExit() {
        roomTeleporters.add(new RoomTeleporter(WALLWIDTH * 8, WALLWIDTH * 3,EXIT_POINT.DOWN, false));
    }

    public void lock() {
        state = STATE.LOCKED;
        for(RoomExit r : roomExits){
            r.lock();
            r.lockAnimation();
        }

        for(RoomTeleporter r : roomTeleporters){
            r.lock();
        }

    }

    public void unlock() {
        state = STATE.UNLOCKED;
        for(RoomExit r : roomExits){
            r.unlock();
            r.unlockAnimation();
        }

        for(RoomTeleporter r : roomTeleporters){
            r.unlock();
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
        return 200;
    }

    public Array<RoomExit> getRoomExits() {
        return roomExits;
    }

    public Array<RoomWall> getRoomWalls() {
        return roomWalls;
    }

    public boolean isUnlocked(){
        return state == state.UNLOCKED;
    }

    public float[] getSectionCenters() {
        return sectionCenters;
    }


    public Array<RoomTeleporter> getRoomTeleporters() {
        return roomTeleporters;
    }
}
