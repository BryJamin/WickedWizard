package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private MapCoords leaveMapCoords;
    private MapCoords roomCoords;

    private RoomExit currentExit;

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
                    roomCoords = r.getRoomCoords();
                    break;
                }
            }

            for (RoomTeleporter r : roomTeleporters) {
                if (r.hasEntered(wizard.getBounds())) {
                    transition = true;
                    leaveMapCoords = r.getLeaveCoords();
                    roomCoords = r.getRoomCoords();
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
                    wizard.setX(r.getBounds().getX() + r.getBounds().getWidth() + wizard.WIDTH);
                    wizard.setY(r.getBounds().getY() + r.getBounds().getHeight() / 2);
                } else {
                    wizard.setX(r.getBounds().getX() - wizard.WIDTH);
                    wizard.setY(r.getBounds().getY() + r.getBounds().getHeight() / 2);
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
                wallCollisionCheck(w, exit.getBounds(), dt);
            }
        }
    }

    public void wallCollisionCheck(Wizard w, Rectangle wallBound , float dt){
        WizardCollider.collisionCheck(w, wallBound, dt);
    }

    public void turnOnRoomEnemyWaves() {
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
        roomWalls.add(new RoomWall(exit.getBounds().x, exit.getBounds().y, WALLWIDTH, exit.getBounds().getHeight(), WALLWIDTH, wallTexture));
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

    public MapCoords getRoomCoords() {
        return roomCoords;
    }

    public MapCoords getLeaveMapCoords() {
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

    public void add(GrapplePoint e){
        grapplePoints.add(e);
    }

    public void addCoords(MapCoords e){
        mapCoordsArray.add(e);
    }




    public Array<MapCoords> getMapCoordsArray() {
        return mapCoordsArray;
    }


    public Array<MapCoords> getAdjacentMapCoords() {

        Array<MapCoords> adjacentCoords = new Array<MapCoords>();

        for(RoomExit e : roomExits){
            adjacentCoords.add(e.getLeaveCoords());
        }

        for(RoomTeleporter e : roomTeleporters){
            adjacentCoords.add(e.getLeaveCoords());
        }

        return adjacentCoords;

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
