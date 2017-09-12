package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.ecs.components.graphics.CameraShakeComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 23/03/2017.
 *
 * System used to control the movement of the camera when both following the players in small and large
 * rooms as well as on room and map transistions
 *
 * //TODO the camera movement isn't 100% smooth at the moment need to come back here at practice on it
 *
 */

public class CameraSystem extends EntitySystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<com.bryjamin.wickedwizard.ecs.components.object.WallComponent> wm;

    private Camera gamecam;
    private Viewport gamePort;

    private Arena currentArena;

    private boolean transitioning;



    private enum CameraMode {
        FIXED, CENTER_FOLLOW
    }

    private CameraMode cameraMode;

    //TODO add acceleration?

    private static final float fixedCameraLerpSpeed = 5.5f;
    private static final float followCameraLerpSpeed = 8.0f;


    private float acceleration = Measure.units(5f);
    private float cameramaxVelocity = Measure.units(30f);
    private float cameradefaultMaxVelocity = Measure.units(100f);
    private Vector2 cameraVelocity;

    private float targetX;
    private float targetY;

    private float max_width;
    private float max_height;

    private float min_width;
    private float min_height;




    @SuppressWarnings("unchecked")
    public CameraSystem(Viewport gamePort) {
        super(Aspect.all(CameraShakeComponent.class));
        this.gamecam = gamePort.getCamera();
        this.gamePort = gamePort;
        this.cameraVelocity = new Vector2();
        this.cameraMode = CameraMode.FIXED;
    }

    @Override
    protected void processSystem() {
        updateGamecam();
    }



    /**
     * Forces the camera to update to the collision boundary entered, even if the system is disabled
     * @param cbc - The collision boundary to update to
     */
    public void snapCameraUpdate(CollisionBoundComponent cbc){
        int offsetY = (int) cbc.bound.getY() / (int) com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT;
        gamecam.position.set(cbc.getCenterX(), offsetY * com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT + Measure.units(30f), 0);
    }

    private void lerp(Camera gamecam, float targetY){
        Vector3 position = gamecam.position;
        float lerp = cameraMode == CameraMode.FIXED ? fixedCameraLerpSpeed : followCameraLerpSpeed;
        //position.x += (Obj.x - position.x) * lerp * world.delta;
        position.y += (targetY - position.y) * lerp * world.delta;
    }

    //TODO need to break this method up. It is too large and confusing.
    public void updateGamecam() {
        CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);

        Arena currentArena = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem.class).getCurrentArena();

        //sets camera on room transfer
        if(this.currentArena != currentArena){
            this.currentArena = currentArena;
            int offsetY = (int) cbc.bound.getY() / (int) com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT;
            gamecam.position.set(cbc.getCenterX(), offsetY * com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT + Measure.units(30f), 0);
        }


        boolean isWithinFixedCameraBounds =  (cbc.bound.y + com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT - Measure.units(10f) >= this.currentArena.getHeight()
                || cbc.bound.y - com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT <= -Measure.units(30f));


        cameraMode  = isWithinFixedCameraBounds ? CameraMode.FIXED : CameraMode.CENTER_FOLLOW;

        switch (cameraMode){
            case FIXED:
                int offsetY = (int) cbc.bound.getY() / (int) com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT;
                targetY = offsetY * com.bryjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT + Measure.units(25) + com.bryjamin.wickedwizard.MainGame.GAME_BORDER;
                break;
            case CENTER_FOLLOW:
                targetY = cbc.getCenterY();
                break;
        }


        lerp(gamecam, targetY);

        //TODO transitioning boolean goes here.


        if(gamecam.position.y <= Measure.units(30f)) {
            gamecam.position.y = Measure.units(30f);
            cameraMode = CameraMode.FIXED;
        } else if (gamecam.position.y + com.bryjamin.wickedwizard.MainGame.GAME_BORDER >= this.currentArena.getHeight() - com.bryjamin.wickedwizard.MainGame.GAME_HEIGHT - com.bryjamin.wickedwizard.MainGame.GAME_UNITS+ Measure.units(30f)) {
            cameraMode = CameraMode.FIXED;
        }


        // calculating the X position of the camera
        gamecam.position.x = cbc.getCenterX();
        //Camera max width and minium width bounds
        if(gamecam.position.x <= gamePort.getWorldWidth() / 2 - com.bryjamin.wickedwizard.MainGame.GAME_BORDER){
            gamecam.position.x = gamePort.getWorldWidth() / 2 - com.bryjamin.wickedwizard.MainGame.GAME_BORDER;
        } else if(gamecam.position.x + gamecam.viewportWidth / 2 >= currentArena.getWidth() +  com.bryjamin.wickedwizard.MainGame.GAME_BORDER){
            gamecam.position.x = currentArena.getWidth() - gamecam.viewportWidth / 2 +  com.bryjamin.wickedwizard.MainGame.GAME_BORDER;
        }

        gamecam.update();


        world.getSystem(com.bryjamin.wickedwizard.ecs.systems.input.PlayerInputSystem.class).movementArea.setPosition(gamecam.position.x - gamePort.getWorldWidth() / 2  +  com.bryjamin.wickedwizard.MainGame.GAME_BORDER,
                gamecam.position.y - gamePort.getWorldHeight() / 2 +  com.bryjamin.wickedwizard.MainGame.GAME_BORDER);
    }






    public void setHorizontal(OrthographicCamera gamecam) {

    }



    public Camera getGamecam() {
        return gamecam;
    }


    public float getMax_width() {
        return max_width;
    }

    public void setMax_width(float max_width) {
        this.max_width = max_width;
    }

    public float getMax_height() {
        return max_height;
    }

    public void setMax_height(float max_height) {
        this.max_height = max_height;
    }

    public float getMin_width() {
        return min_width;
    }

    public void setMin_width(float min_width) {
        this.min_width = min_width;
    }

    public float getMin_height() {
        return min_height;
    }

    public void setMin_height(float min_height) {
        this.min_height = min_height;
    }


    public float getCameraX(){
        return gamecam.position.x - gamecam.viewportWidth / 2;
    }

    public float getCameraY(){
        return gamecam.position.y - gamecam.viewportHeight / 2;
    }


    public static boolean isOnCamera(Rectangle r, Camera gamecam){

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        boolean isOnX = r.getX() + r.getWidth() >= camX && r.getX() <= camX + gamecam.viewportWidth;
        boolean isOnY = r.getY() + r.getHeight() >= camY && r.getY() <= camY + gamecam.viewportHeight;
        return isOnX && isOnY;
    }


}