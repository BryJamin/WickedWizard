package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;

import sun.applet.Main;

/**
 * Created by Home on 23/03/2017.
 */

public class CameraSystem extends EntitySystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ActiveOnTouchComponent> aotm;
    ComponentMapper<WallComponent> wm;

    PositionComponent playerPosition;

    private OrthographicCamera gamecam;
    private Viewport gamePort;

    private Arena currentArena;

    private boolean transitioning;



    private enum CameraMode {
        FIXED, CENTER_FOLLOW
    }

    private CameraMode cameraMode;

    //TODO add acceleration?

    private static final float fixedAcceleration = Measure.units(2.5f);
    private static final float centerFollowAcceleration = Measure.units(10f);


    private float acceleration = Measure.units(5f);
    private float cameramaxVelocity = Measure.units(30f);
    private float cameradefaultMaxVelocity = Measure.units(100f);
    private Vector2 cameraVelocity;

    private Rectangle left = new Rectangle();
    private Rectangle right = new Rectangle();
    private Rectangle top = new Rectangle();
    private Rectangle bottom = new Rectangle();

    private float targetX;
    private float targetY;

    private float max_width;
    private float max_height;

    private float min_width;
    private float min_height;




    @SuppressWarnings("unchecked")
    public CameraSystem(OrthographicCamera gamecam, Viewport gamePort) {
        super(Aspect.all(PlayerComponent.class));
        this.gamecam = gamecam;
        this.gamePort = gamePort;
        this.cameraVelocity = new Vector2();
        this.cameraMode = CameraMode.FIXED;
    }

    @Override
    protected void processSystem() {
        updateGamecam();
    }



    public void fixedFollow(){




    }

    //TODO need to break this method up. It is too large and confusing.
    public void updateGamecam() {
        CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);

        VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);


        Arena currentArena = world.getSystem(RoomTransitionSystem.class).getCurrentArena();

        //sets camera on room transfer
        if(this.currentArena != currentArena){
            this.currentArena = currentArena;
            int offsetY = (int) cbc.bound.getY() / (int) ArenaShellFactory.SECTION_HEIGHT;
            gamecam.position.set(cbc.getCenterX(), offsetY * ArenaShellFactory.SECTION_HEIGHT + Measure.units(30f), 0);
        }


        if(cameraMode == CameraMode.CENTER_FOLLOW){
            if(cbc.bound.y + ArenaShellFactory.SECTION_HEIGHT - Measure.units(10f) >= this.currentArena.getHeight()
                    || cbc.bound.y - ArenaShellFactory.SECTION_HEIGHT <= -Measure.units(30f)) {
                cameraMode = CameraMode.FIXED;
                acceleration = fixedAcceleration;
            }
        }

        if(cameraMode == CameraMode.FIXED) {
            if (cbc.bound.y + ArenaShellFactory.SECTION_HEIGHT - Measure.units(10f) >= this.currentArena.getHeight()
                    || cbc.bound.y - ArenaShellFactory.SECTION_HEIGHT <= -Measure.units(30f)) {

                int offsetY = (int) cbc.bound.getY() / (int) ArenaShellFactory.SECTION_HEIGHT;
                targetY = offsetY * ArenaShellFactory.SECTION_HEIGHT + Measure.units(30f);
            } else {
                cameraMode = CameraMode.CENTER_FOLLOW;
                acceleration = centerFollowAcceleration;
                transitioning = true;
            }
        }

        if(cameraMode == CameraMode.CENTER_FOLLOW){
            targetY = cbc.getCenterY();
        }


        //TODO transitioning boolean goes here.

        if(transitioning || cameraMode == CameraMode.FIXED) {
            if (gamecam.position.y >= targetY) {
                cameraVelocity.y = (cameraVelocity.y > 0) ? 0 : cameraVelocity.y;
                cameraVelocity.y = (cameraVelocity.y - acceleration <= -cameradefaultMaxVelocity) ?
                        -cameradefaultMaxVelocity : cameraVelocity.y - acceleration;

                boolean onTarget = (gamecam.position.y + cameraVelocity.y * world.delta < targetY);
/*
                System.out.println("Upper on target " + onTarget);
                System.out.println(cameraVelocity.y);
                System.out.println(targetY);*/

                gamecam.position.y = onTarget ? targetY : gamecam.position.y + cameraVelocity.y * world.delta;

                if(onTarget) transitioning = false;

            } else {

                cameraVelocity.y = (cameraVelocity.y < 0) ? 0 : cameraVelocity.y;

                cameraVelocity.y = (cameraVelocity.y + acceleration >= cameradefaultMaxVelocity) ?
                        cameradefaultMaxVelocity : cameraVelocity.y + acceleration;

                boolean onTarget = (gamecam.position.y + cameraVelocity.y * world.delta > targetY);
                gamecam.position.y = onTarget ? targetY : gamecam.position.y + cameraVelocity.y * world.delta;

                if(onTarget) transitioning = false;
            }
        }

/*        System.out.println("Transitioning is currenty " + transitioning);
        System.out.println("Target Y is " + targetY);*/

        //System.out.println(transitioning);

        if(cameraMode == CameraMode.CENTER_FOLLOW && !transitioning) {
            gamecam.position.y = cbc.getCenterY();

        }


        //Camera max height and minium height bounds.
        if(gamecam.position.y <= Measure.units(30f)) {
            gamecam.position.y = Measure.units(30f);
        } else if (gamecam.position.y + MainGame.GAME_BORDER >= this.currentArena.getHeight() - MainGame.GAME_HEIGHT -MainGame.GAME_UNITS+ Measure.units(30f)) {
            //System.out.println("INSIDE IF");
            if(!transitioning) {
                //gamecam.position.y = this.currentArena.getHeight() - MainGame.GAME_HEIGHT + Measure.units(30f) -MainGame.GAME_UNITS;
                cameraMode = CameraMode.FIXED;
            }
        }




        // calculating the X position of the camera
        gamecam.position.x = cbc.getCenterX();
        //Camera max width and minium width bounds
        if(gamecam.position.x <= gamePort.getWorldWidth() / 2 - MainGame.GAME_BORDER){
            gamecam.position.x = gamePort.getWorldWidth() / 2 - MainGame.GAME_BORDER;
        } else if(gamecam.position.x + gamecam.viewportWidth / 2 >= currentArena.getWidth() +  MainGame.GAME_BORDER){
            gamecam.position.x = currentArena.getWidth() - gamecam.viewportWidth / 2 +  MainGame.GAME_BORDER;
        }

        gamecam.update();


        world.getSystem(PlayerInputSystem.class).movementArea.setPosition(gamecam.position.x - gamePort.getWorldWidth() / 2  +  MainGame.GAME_BORDER,
                gamecam.position.y - gamePort.getWorldHeight() / 2 +  MainGame.GAME_BORDER);
    }


    public void setHorizontal(OrthographicCamera gamecam) {

    }



    public OrthographicCamera getGamecam() {
        return gamecam;
    }

    public void setPlayerPosition(PositionComponent playerPosition) {
        this.playerPosition = playerPosition;
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


    public Rectangle getLeft() {
        return left;
    }

    public Rectangle getRight() {
        return right;
    }

    public Rectangle getTop() {
        return top;
    }

    public Rectangle getBottom() {
        return bottom;
    }


    public float getCameraX(){
        return gamecam.position.x - gamecam.viewportWidth / 2;
    }

    public float getCameraY(){
        return gamecam.position.y - gamecam.viewportHeight / 2;
    }


    //TODO may ned to factor in the border
    public boolean isOnCamera(Rectangle r){
        System.out.println(r.getX());
        boolean isOnX = r.getX() >= getCameraX() + MainGame.GAME_BORDER && r.getX() - r.getWidth() <= getCameraX() + gamecam.viewportWidth - MainGame.GAME_BORDER;
        boolean isOnY = r.getY() >= getCameraY() + MainGame.GAME_BORDER && r.getY() - r.getHeight() <= getCameraY() + ArenaShellFactory.SECTION_HEIGHT - MainGame.GAME_BORDER;
        return isOnX && isOnY;
    }
}