package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.helper.collider.WizardCollider;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.layout.GrapplePoint;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomPlatform;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomTeleporter;
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


    public float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public float SECTION_HEIGHT = MainGame.GAME_HEIGHT;


    public float WIDTH = MainGame.GAME_WIDTH;
    public float HEIGHT = MainGame.GAME_HEIGHT;

    public float TILE_SIZE = MainGame.GAME_WIDTH / 10;

    private float WALLWIDTH = Measure.units(5);

    private float tile_height;
    private float tile_width;

    private float x = 0;
    private float y = 0;

    private boolean transition;

    private Array<MapCoords> mapCoordsArray = new Array<MapCoords>();

    private MapCoords startCoords;
    private MapCoords wizardCoords;
    private MapCoords leaveMapCoords;
    private MapCoords leaveEntryMapCoords;

    protected Array<Item> items = new Array<Item>();
    protected Array<RoomExit> roomExits = new Array<RoomExit>();
    protected Array<RoomTeleporter> roomTeleporters = new Array<RoomTeleporter>();
    protected Array<RoomWall> roomWalls = new Array<RoomWall>();
    protected Array<GrapplePoint> grapplePoints = new Array<GrapplePoint>();

    private RoomEnemyUpdater roomEnemyUpdater = new RoomEnemyUpdater();
    protected Wizard wizard = new Wizard(WALLWIDTH * 2, 600);

    private RoomWall leftWall;
    private RoomWall rightWall;
    private Array<RoomWall> ceiling = new Array<RoomWall>();
    private Array<RoomWall> ground = new Array<RoomWall>();

    private long seed = 2;

    private Array<Rectangle> groundBoundaries = new Array<Rectangle>();
    private Array<RoomPlatform> platforms = new Array<RoomPlatform>();

    private RoomEnemyWaves roomEnemyWaves;


    public enum STATE {
       ENTRY, LOCKED, UNLOCKED, EXIT
    }

    public STATE state = STATE.UNLOCKED;

    private RoomBackground roomBackground;

    public Room(MapCoords startCoords, int scalex, int scaley) {

        //mapCoordsArray.add(startCoords);
        this.startCoords = startCoords;
        wizardCoords = startCoords;

        WIDTH = WIDTH * scalex;
        HEIGHT = HEIGHT * scaley;


        roomExits.add(new RoomExit(0, groundHeight(), WALLWIDTH, Measure.units(20),
                new MapCoords(startCoords.getX(), startCoords.getY()),
                new MapCoords(startCoords.getX() - 1, startCoords.getY()), false));
        roomWalls.add(new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 6, WALLWIDTH));

        int xs = (int) WIDTH / (int) SECTION_WIDTH;
        int ys = (int) HEIGHT / (int) SECTION_HEIGHT;

        System.out.println(startCoords.getX() + xs);

        System.out.println("XS is " + xs);
        System.out.println("YS is " + ys);


        roomExits.add(new RoomExit(WIDTH - WALLWIDTH, groundHeight(), WALLWIDTH,
                Measure.units(20),
                new MapCoords(startCoords.getX() + xs - 1, startCoords.getY()),
                new MapCoords(startCoords.getX() + xs, startCoords.getY()),false));
        roomWalls.add(new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 6, WALLWIDTH));

        for(int i = 0; i < xs; i++){
            //mapCoordsArray.add(new MapCoords(startCoords.getX() + (i + 1), startCoords.getY()));
        }

        for(int i = 1; i < ys; i++){
            //mapCoordsArray.add(new MapCoords(startCoords.getX(), startCoords.getY()+ (i + 1)));

            roomExits.add(new RoomExit(WIDTH - WALLWIDTH, groundHeight() + SECTION_HEIGHT , WALLWIDTH,
                    Measure.units(20),
                    new MapCoords(startCoords.getX() + xs - 1, startCoords.getY() + i),
                    new MapCoords(startCoords.getX() + xs, startCoords.getY() + i),false));
            roomWalls.add(new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6 + SECTION_HEIGHT, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 6, WALLWIDTH));

            System.out.println("HMMM " + (startCoords.getY() + i + 1));

        }


        for(int i = 0; i < xs; i++){
            for(int j = 0; j < ys; j++){
                mapCoordsArray.add(new MapCoords(startCoords.getX() + i, startCoords.getY() + j));
            }
        }




        for(MapCoords m : mapCoordsArray){
            System.out.println(m.toString());
        }


    }

    public Room(MapCoords startCoords){
        mapCoordsArray.add(startCoords);
        this.startCoords = startCoords;

        roomExits.add(new RoomExit(0, groundHeight(), WALLWIDTH, Measure.units(20),
                new MapCoords(startCoords.getX(), startCoords.getY()),
                new MapCoords(startCoords.getX() - 1, startCoords.getY()), false));
        roomWalls.add(new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, HEIGHT, WALLWIDTH));
        roomExits.add(new RoomExit(WIDTH - WALLWIDTH, groundHeight(), WALLWIDTH, Measure.units(20),
                new MapCoords(startCoords.getX(), startCoords.getY()),
                new MapCoords(startCoords.getX() + 1, startCoords.getY()),false));
        roomWalls.add(new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, HEIGHT, WALLWIDTH));

        roomTeleporters.add(new RoomTeleporter(WALLWIDTH * 8, HEIGHT - WALLWIDTH * 3,
                new MapCoords(startCoords.getX(), startCoords.getY()),
                new MapCoords(startCoords.getX(), startCoords.getY() + 1)));
        roomTeleporters.add(new RoomTeleporter(WALLWIDTH * 8, WALLWIDTH * 3,
                new MapCoords(startCoords.getX(), startCoords.getY()),
                new MapCoords(startCoords.getX(), startCoords.getY() - 1)));

    }

    public void update(float dt, OrthographicCamera gamecam){
        wizard.update(dt, gamecam, this);

        //System.out.println(startCoords.getX() + (int) wizard.getX() / (int) SECTION_WIDTH);

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



        if(state == STATE.UNLOCKED && !transition) {
            for (RoomExit r : roomExits) {
                if (r.hasEntered(wizard.getBounds())) {
                    transition = true;
                    leaveMapCoords = r.getLeaveCoords();
                    leaveEntryMapCoords = r.getRoomCoords();
                    break;
                }
            }

            for (RoomTeleporter r : roomTeleporters) {
                if (r.hasEntered(wizard.getBounds())) {
                    transition = true;
                    leaveMapCoords = r.getLeaveCoords();
                    leaveEntryMapCoords = r.getRoomCoords();
                    r.setActive(false);
                    break;
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

        for(RoomWall r : roomWalls){
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

        for(RoomWall r : roomWalls){
            r.draw(batch);
        }

        for(GrapplePoint r : grapplePoints){
            r.draw(batch);
        }

        BoundsDrawer.drawBounds(batch, platforms);

        wizard.draw(batch);

    }

    public void enterRoom(Wizard w, MapCoords oldRoomCoords, MapCoords entryCoords){

        this.wizard = w;
        wizard.setCurrentState(Wizard.STATE.IDLE);
        wizard.cancelMovementHorizontalSpeed();
        transition = false;

        boolean forbugs = true;


        System.out.println(roomExits.size + " The amount of exits");

        for(RoomExit r : roomExits){
            if(r.getLeaveCoords().sameCoords(oldRoomCoords)){
                if(r.getLeaveCoords().getX() < entryCoords.getX()){
                    wizard.setX(r.getBound().getX() + r.getBound().getWidth() + wizard.WIDTH);
                    wizard.setY(r.getBound().getY() + r.getBound().getHeight() / 2);
                    forbugs = false;
                } else {
                    wizard.setX(r.getBound().getX() - wizard.WIDTH);
                    wizard.setY(r.getBound().getY() + r.getBound().getHeight() / 2);
                    forbugs = false;
                }

                forbugs = false;
                break;
            }
        }

        System.out.println(oldRoomCoords);


        System.out.println("Entered Room Co-ordinates are ");

        for(MapCoords r : mapCoordsArray){
            System.out.println(r);
        }

        System.out.println(forbugs + "FORBUGS IS");

        if(forbugs){
            wizard.setX(WIDTH /2);
            wizard.setY(HEIGHT / 2);
        }

        for(RoomTeleporter r : roomTeleporters){
            if(r.getLeaveCoords().sameCoords(oldRoomCoords)){
                wizard.setCenterX(r.getCenterX());
                wizard.setCenterY(r.getCenterY());
            }
        }
    }

    public boolean isExitTransitionFinished(){
        return transition;
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
        groundBoundaries.addAll(platforms);

        ceiling.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH));
        ground.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH));

        roomWalls.addAll(ceiling);
        roomWalls.addAll(ground);

        for(RoomWall roomWall : ground){
            groundBoundaries.add(roomWall.getBounds());
        }

        roomBackground = new RoomBackground(PlayScreen.atlas.findRegions("backgrounds/wall"), 0, 0 , this.WIDTH, this.HEIGHT, Measure.units(10));

        roomEnemyWaves = new RoomEnemyWaves(this);

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


    public void replaceDoorwithWall(RoomExit exit){
        roomWalls.add(new RoomWall(exit.getBound().x, exit.getBound().y, WALLWIDTH, exit.getBound().getHeight(), WALLWIDTH));
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

    public boolean containsCoords(MapCoords mc){
        for(MapCoords m : mapCoordsArray){
            if(m.sameCoords(mc)){
                return true;
            }
        }
        return false;
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

    public MapCoords getLeaveEntryMapCoords() {
        return leaveEntryMapCoords;
    }

    public MapCoords getLeaveMapCoords() {
        return leaveMapCoords;
    }

    public MapCoords getWizardCoords() {
        return leaveMapCoords;
    }

    public Array<RoomTeleporter> getRoomTeleporters() {
        return roomTeleporters;
    }

    public Array<GrapplePoint> getGrapplePoints() {
        return grapplePoints;
    }
}
