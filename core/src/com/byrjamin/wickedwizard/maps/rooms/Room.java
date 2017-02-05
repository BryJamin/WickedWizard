package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.helper.collider.WizardCollider;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.components.GrapplePoint;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomPlatform;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomTeleporter;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;
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


    protected Array<? extends TextureRegion> wallTexture = PlayScreen.atlas.findRegions("brick");

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
        roomWalls.add(new RoomWall(0, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH, wallTexture));

        int xs = (int) WIDTH / (int) SECTION_WIDTH;
        int ys = (int) HEIGHT / (int) SECTION_HEIGHT;

        System.out.println(startCoords.getX() + xs);

        System.out.println("XS is " + xs);
        System.out.println("YS is " + ys);


        roomExits.add(new RoomExit(WIDTH - WALLWIDTH, groundHeight(), WALLWIDTH,
                Measure.units(20),
                new MapCoords(startCoords.getX() + xs - 1, startCoords.getY()),
                new MapCoords(startCoords.getX() + xs, startCoords.getY()),false));
        roomWalls.add(new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, WALLWIDTH, wallTexture));

        for(int i = 0; i < xs; i++){
            //mapCoordsArray.add(new MapCoords(startCoords.getX() + (i + 1), startCoords.getY()));
        }

        for(int i = 1; i < ys; i++){
            //mapCoordsArray.add(new MapCoords(startCoords.getX(), startCoords.getY()+ (i + 1)));

            roomExits.add(new RoomExit(WIDTH - WALLWIDTH, groundHeight() + SECTION_HEIGHT , WALLWIDTH,
                    Measure.units(20),
                    new MapCoords(startCoords.getX() + xs - 1, startCoords.getY() + i),
                    new MapCoords(startCoords.getX() + xs, startCoords.getY() + i),false));
            roomWalls.add(new RoomWall(WIDTH - WALLWIDTH, WALLWIDTH * 6 + SECTION_HEIGHT, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 6, WALLWIDTH, wallTexture));

            roomExits.add(new RoomExit(0, groundHeight() + SECTION_HEIGHT , WALLWIDTH,
                    Measure.units(20),
                    new MapCoords(startCoords.getX(), startCoords.getY() + i),
                    new MapCoords(startCoords.getX() - 1, startCoords.getY() + i),false));
            roomWalls.add(new RoomWall(0, WALLWIDTH * 6 + SECTION_HEIGHT, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 6, WALLWIDTH, wallTexture));

        }

        for(int i = 0; i < xs; i++){
            for(int j = 0; j < ys; j++){
                mapCoordsArray.add(new MapCoords(startCoords.getX() + i, startCoords.getY() + j));
                grapplePoints.add(new GrapplePoint(WIDTH / 2, (SECTION_HEIGHT / 2) + (SECTION_HEIGHT / 2) * j));
            }
        }

        for(MapCoords m : mapCoordsArray){
            System.out.println(m.toString());
        }


    }

    public Room(MapCoords startCoords){
        mapCoordsArray.add(startCoords);
        this.startCoords = startCoords;
    }

    public void update(float dt, OrthographicCamera gamecam){
        wizard.update(dt, gamecam, this);
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

        for(RoomExit r : roomExits){
            if(r.getLeaveCoords().sameCoords(oldRoomCoords)){
                if(r.getLeaveCoords().getX() < entryCoords.getX()){
                    wizard.setX(r.getBound().getX() + r.getBound().getWidth() + wizard.WIDTH);
                    wizard.setY(r.getBound().getY() + r.getBound().getHeight() / 2);
                } else {
                    wizard.setX(r.getBound().getX() - wizard.WIDTH);
                    wizard.setY(r.getBound().getY() + r.getBound().getHeight() / 2);
                }

                break;
            }
        }

        for(MapCoords r : mapCoordsArray){
            System.out.println(r);
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
        ceiling.add(new RoomWall(0,  HEIGHT - WALLWIDTH, WIDTH, WALLWIDTH, WALLWIDTH, wallTexture));
        ground.add(new RoomWall(0,  -WALLWIDTH, WIDTH, WALLWIDTH * 3, WALLWIDTH, wallTexture));

        roomWalls.addAll(ceiling);
        roomWalls.addAll(ground);

        for(RoomWall roomWall : ground){
            groundBoundaries.add(roomWall.getBounds());
        }


        roomBackground = new RoomBackground(PlayScreen.atlas.findRegions("backgrounds/wall"), 0, 0 , this.WIDTH, this.HEIGHT, Measure.units(15));

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
        roomWalls.add(new RoomWall(exit.getBound().x, exit.getBound().y, WALLWIDTH, exit.getBound().getHeight(), WALLWIDTH, wallTexture));
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


    public boolean containsExitWithCoords(MapCoords EntryCoords, MapCoords LeaveCoords){
        for(RoomExit re : roomExits){
            if(re.getRoomCoords().sameCoords(LeaveCoords) && re.getLeaveCoords().sameCoords(EntryCoords)){
                return true;
            }
        }

        for(RoomTeleporter re : roomTeleporters){
            if(re.getRoomCoords().sameCoords(LeaveCoords) && re.getLeaveCoords().sameCoords(EntryCoords)){
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


    public void add(RoomExit e){
        roomExits.add(e);
    }

    public void add(RoomTeleporter e){
        roomTeleporters.add(e);
    }

    public void add(RoomWall e){
        roomWalls.add(e);
    }

    public void setRoomBackground(RoomBackground e){
        roomBackground = e;
    }

    public MapCoords getStartCoords() {
        return startCoords;
    }

    public Array<RoomTeleporter> getRoomTeleporters() {
        return roomTeleporters;
    }

    public Array<GrapplePoint> getGrapplePoints() {
        return grapplePoints;
    }
}
