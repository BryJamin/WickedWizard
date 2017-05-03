package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaShellFactory;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;

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

    private static final float fixedAcceleration = Measure.units(5f);
    private static final float centerFollowAcceleration = Measure.units(15f);

    private float acceleration = Measure.units(5f);
    private float cameraMaxVelocity = Measure.units(130f);
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


        Arena a = world.getSystem(RoomTransitionSystem.class).getCurrentArena();

        //sets camera on room transfer
        if(currentArena != a){
            currentArena = a;
            int offsetY = (int) cbc.bound.getY() / (int) gamecam.viewportHeight;
            gamecam.position.set(cbc.getCenterX(), offsetY * gamecam.viewportHeight + Measure.units(30f), 0);
        }



        gamecam.position.x = cbc.getCenterX();

        if(cameraMode == CameraMode.CENTER_FOLLOW && cbc.getRecentCollisions().contains(Collider.Collision.TOP, true)){
            if(cbc.bound.y + ArenaShellFactory.SECTION_HEIGHT - Measure.units(10f) >= currentArena.getHeight()
                    || cbc.bound.y - ArenaShellFactory.SECTION_HEIGHT <= -Measure.units(30f)) {
                cameraMode = CameraMode.FIXED;
                acceleration = fixedAcceleration;
            }
        }

        if(cameraMode == CameraMode.FIXED) {
            if (cbc.bound.y + ArenaShellFactory.SECTION_HEIGHT - Measure.units(10f) >= currentArena.getHeight()
                    || cbc.bound.y - ArenaShellFactory.SECTION_HEIGHT <= -Measure.units(30f)) {
                int offsetY = (int) cbc.bound.getY() / (int) gamecam.viewportHeight;
                targetY = offsetY * gamecam.viewportHeight + Measure.units(30f);
            } else {
                cameraMode = CameraMode.CENTER_FOLLOW;
                acceleration = centerFollowAcceleration;
                transitioning = true;
            }
        }

        if(cameraMode == CameraMode.CENTER_FOLLOW){
            targetY = cbc.getCenterY();
        }
        //}


        //TODO transitioning boolean goes here.

        if(transitioning || cameraMode == CameraMode.FIXED) {
            if (gamecam.position.y >= targetY) {
                cameraVelocity.y = (cameraVelocity.y > 0) ? 0 : cameraVelocity.y;
                cameraVelocity.y = (cameraVelocity.y - acceleration <= -cameraMaxVelocity) ?
                        -cameraMaxVelocity : cameraVelocity.y - acceleration;

                boolean onTarget = (gamecam.position.y + cameraVelocity.y * world.delta < targetY);
                gamecam.position.y = onTarget ? targetY : gamecam.position.y + cameraVelocity.y * world.delta;

                if(onTarget) transitioning = false;

            } else {

                cameraVelocity.y = (cameraVelocity.y < 0) ? 0 : cameraVelocity.y;

                cameraVelocity.y = (cameraVelocity.y + acceleration >= cameraMaxVelocity) ?
                        cameraMaxVelocity : cameraVelocity.y + acceleration;

                boolean onTarget = (gamecam.position.y + cameraVelocity.y * world.delta > targetY);
                gamecam.position.y = onTarget ? targetY : gamecam.position.y + cameraVelocity.y * world.delta;

                if(onTarget) transitioning = false;
            }
        }

/*        System.out.println("Transitioning is currenty " + transitioning);
        System.out.println("Target Y is " + targetY);*/


        if(cameraMode == CameraMode.CENTER_FOLLOW && !transitioning) {
            gamecam.position.y = cbc.getCenterY();
        }


        //Camera max height and minium height bounds.
        if(gamecam.position.y <= Measure.units(30f)) {
            gamecam.position.y = Measure.units(30f);
        } else if (gamecam.position.y >= currentArena.getHeight() - MainGame.GAME_HEIGHT + Measure.units(30f)) {
            gamecam.position.y = currentArena.getHeight() - MainGame.GAME_HEIGHT + Measure.units(30f);
            cameraMode = CameraMode.FIXED;
        }

        //Camera max width and minium width bounds
        if(gamecam.position.x <= gamePort.getWorldWidth() / 2){
            gamecam.position.x = gamePort.getWorldWidth() / 2;
        } else if(gamecam.position.x + gamecam.viewportWidth / 2 >= a.getWidth()){
            gamecam.position.x = a.getWidth() - gamecam.viewportWidth / 2;
        }


/*        if(gamecam.position.y + ArenaShellFactory.SECTION_HEIGHT >= currentArena.getHeight()) {
            gamecam.position.y = currentArena.getHeight() - ArenaShellFactory.SECTION_HEIGHT;
        }*/

/*
        System.out.println(cameraMode);
        System.out.println(transitioning);
*/

        gamecam.update();


        world.getSystem(PlayerInputSystem.class).movementArea.setPosition(gamecam.position.x - gamePort.getWorldWidth() / 2,
                gamecam.position.y - gamePort.getWorldHeight() / 2);
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
}