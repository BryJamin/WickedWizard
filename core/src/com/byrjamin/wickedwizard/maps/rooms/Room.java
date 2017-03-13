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
import com.byrjamin.wickedwizard.maps.rooms.components.GrapplePoint;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomBackground;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomPlatform;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomGrate;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomLayout;
import com.byrjamin.wickedwizard.maps.rooms.spawns.RoomEnemyUpdater;
import com.byrjamin.wickedwizard.gameobject.player.Wizard;
import com.byrjamin.wickedwizard.gameobject.enemies.Enemy;

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

    private float WALLWIDTH = Measure.units(5);

    private RoomLayout.ROOM_LAYOUT layout;

    private float x = 0;
    private float y = 0;

    private boolean transition;

    private Array<MapCoords> mapCoordsArray = new Array<MapCoords>();
    private MapCoords startCoords;
    private RoomExit currentExit;

    protected Array<Item> items = new Array<Item>();
    protected Array<RoomDoor> roomDoors = new Array<RoomDoor>();
    protected Array<RoomGrate> roomGrates = new Array<RoomGrate>();
    protected Array<RoomWall> roomWalls = new Array<RoomWall>();
    protected Array<GrapplePoint> grapplePoints = new Array<GrapplePoint>();

    protected Array<RoomExit> roomExits = new Array<RoomExit>();

    private RoomEnemyUpdater roomEnemyUpdater = new RoomEnemyUpdater();
    protected Wizard wizard = new Wizard(WALLWIDTH * 2, 600);
    private Array<RoomPlatform> platforms = new Array<RoomPlatform>();

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


        for(Enemy e : getEnemies()){
            for(RoomWall rw : roomWalls) {
                e.applyCollision(Collider.collision(e.getCollisionBound(), e.getCollisionBound(), rw.getBounds()));
            }

            for(RoomDoor rd : roomDoors) {
                e.applyCollision(Collider.collision(e.getCollisionBound(), e.getCollisionBound(), rd.getBounds()));
            }
        }

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
            for (RoomExit re : roomExits) {
                if (re.hasEntered(wizard.getBounds())) {
                    transition = true;
                    currentExit = re;
                    if(re instanceof RoomGrate) {
                        ((RoomGrate) re).setActive(false);
                    }
                    break;
                }
            }
        }

        for (RoomPlatform r : platforms) {
            r.update(wizard);
        }

        for (RoomDoor r : roomDoors) {
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

        for(RoomExit re : roomExits) {
            re.draw(batch);
        }

        roomEnemyUpdater.draw(batch);

        for(Item item : items) {
            item.draw(batch);
            BoundsDrawer.drawBounds(batch, item.getBoundingRectangle());
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
        wizard.getActiveBullets().getBullets().clear();
        transition = false;

        for(RoomDoor r : roomDoors){
            if(r.getLeaveCoords().equals(oldRoomCoords)){
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

        for(RoomGrate r : roomGrates){
            if(r.getLeaveCoords().equals(oldRoomCoords)){
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
        for(RoomDoor exit : roomDoors){
            if(!exit.isUnlocked() && state != STATE.ENTRY){
                wallCollisionCheck(w, exit.getBounds(), dt);
            }
        }
    }

    public void wallCollisionCheck(Wizard w, Rectangle wallBound , float dt){
        WizardCollider.collisionCheck(w, wallBound, dt);
    }

    public void lock() {
        state = STATE.LOCKED;
        for(RoomDoor r : roomDoors){
            r.lock();
            r.lockAnimation();
        }
        for(RoomGrate r : roomGrates){
            r.lock();
        }
    }


    public void replaceDoorwithWall(RoomDoor exit){
        roomWalls.add(new RoomWall(exit.getBounds().x, exit.getBounds().y, WALLWIDTH, exit.getBounds().getHeight(), WALLWIDTH));
    }

    public void unlock() {
        state = STATE.UNLOCKED;
        for(RoomDoor r : roomDoors){
            r.unlock();
            r.unlockAnimation();
        }

        for(RoomGrate r : roomGrates){
            r.unlock();
        }
    }

    public boolean containsCoords(MapCoords mc){
        return mapCoordsArray.contains(mc, false);
    }


    public boolean containsExitWithCoords(MapCoords EntryCoords, MapCoords LeaveCoords){

        for(RoomExit re : roomExits) {
            if(re.getRoomCoords().equals(LeaveCoords) && re.getLeaveCoords().equals(EntryCoords)){
                return true;
            }
        }

        return false;
    }

    public void shiftCoordinatePosition(MapCoords newPosition){

        int diffX = newPosition.getX() - startCoords.getX();
        int diffY = newPosition.getY() - startCoords.getY();


        for(MapCoords m : mapCoordsArray) {
            m.addX(diffX);
            m.addY(diffY);
        }

        for(RoomDoor r : roomDoors){
            r.getLeaveCoords().add(diffX, diffY);
            r.getRoomCoords().add(diffX, diffY);
        }

        for(RoomGrate r : roomGrates){
            r.getLeaveCoords().add(diffX, diffY);
            r.getRoomCoords().add(diffX, diffY);
        }


    }

    public MapCoords getWizardLocation() {
        return new MapCoords(startCoords.getX() + (int) (wizard.getX() / SECTION_WIDTH), startCoords.getY() + (int) (wizard.getY() / SECTION_HEIGHT));
    }

    public Array<MapCoords> mockShiftCoordinatePosition(MapCoords newPosition){

        int diffX = newPosition.getX() - startCoords.getX();
        int diffY = newPosition.getY() - startCoords.getY();

        Array<MapCoords> mockCoords = new Array<MapCoords>();

        for(MapCoords mc : mapCoordsArray) {
            mockCoords.add(new MapCoords(mc));
        }

        for(MapCoords m : mockCoords) {
            m.addX(diffX);
            m.addY(diffY);
        }

        return mockCoords;
    }

    public Array<MapCoords> mockShiftCoordinatePositionAdjacent(MapCoords newPosition){

        int diffX = newPosition.getX() - startCoords.getX();
        int diffY = newPosition.getY() - startCoords.getY();

        Array<MapCoords> mockCoords = new Array<MapCoords>();

        for(MapCoords mc : getAdjacentMapCoords()) {
            mockCoords.add(new MapCoords(mc));
        }

        for(MapCoords m : mockCoords) {
            m.addX(diffX);
            m.addY(diffY);
        }

        return mockCoords;
    }

    public Array<RoomExit> getRoomExits() {
        return roomExits;
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

    public void addSpawningEnemy(Enemy e){
        roomEnemyUpdater.addSpawningEnemy(e);
    }

    public void addEnemyWave(Array<Enemy> e){
        roomEnemyUpdater.addWave(e);
    }

    public RoomEnemyUpdater getRoomEnemyUpdater() {
        return roomEnemyUpdater;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public Boolean isWizardonGround() {

        for(RoomWall rw : roomWalls){
            if(Collider.isOnTop(wizard.getBounds(), rw.getBounds())){
                return true;
            }
        }
        return false;

    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public float groundHeight(){
        return 200;
    }

    public Array<RoomDoor> getRoomDoors() {
        return roomDoors;
    }

    public Array<RoomWall> getRoomWalls() {
        return roomWalls;
    }

    public boolean isUnlocked(){
        return state == state.UNLOCKED;
    }

    public RoomExit getCurrentExit() {
        return currentExit;
    }

    public void add(RoomDoor e){
        roomDoors.add(e);
    }

    public void add(RoomGrate e){
        roomGrates.add(e);
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
        for(RoomDoor e : roomDoors){
            adjacentCoords.add(e.getLeaveCoords());
        }

        for(RoomGrate e : roomGrates){
            adjacentCoords.add(e.getLeaveCoords());
        }
        return adjacentCoords;
    }

    public void setRoomBackground(RoomBackground e){
        roomBackground = e;
    }

    public RoomBackground getRoomBackground() {
        return roomBackground;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public MapCoords getStartCoords() {
        return startCoords;
    }

    public Array<RoomGrate> getRoomGrates() {
        return roomGrates;
    }

    public Array<GrapplePoint> getGrapplePoints() {
        return grapplePoints;
    }


    public RoomLayout.ROOM_LAYOUT getLayout() {
        return layout;
    }

    public void setLayout(RoomLayout.ROOM_LAYOUT layout) {
        this.layout = layout;
    }
}
