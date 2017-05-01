package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
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


    //TODO add acceleration?
    private float acceleration = Measure.units(15f);
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
    }

    @Override
    protected void processSystem() {
        updateGamecam();
    }

    public void updateGamecam() {
        CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);


        Arena a = world.getSystem(RoomTransitionSystem.class).getCurrentArena();

        if(currentArena != a){
            currentArena = a;
            int offsetY = (int) cbc.bound.getY() / (int) gamecam.viewportHeight;
            gamecam.position.set(cbc.getCenterX(), offsetY * gamecam.viewportHeight + Measure.units(30f), 0);
        }


        gamecam.position.x = cbc.getCenterX();

        targetY = cbc.getCenterY();


        //if(cbc.getRecentCollisions().contains(Collider.Collision.TOP, true)) {
            if (cbc.bound.y + ArenaShellFactory.SECTION_HEIGHT - Measure.units(10f) >= currentArena.getHeight()
                    || cbc.bound.y - ArenaShellFactory.SECTION_HEIGHT <= -Measure.units(30f)) {
                int offsetY = (int) cbc.bound.getY() / (int) gamecam.viewportHeight;
                targetY = offsetY * gamecam.viewportHeight + Measure.units(30f);
            }
        //}


        if(gamecam.position.x <= gamePort.getWorldWidth() / 2){
            gamecam.position.x = gamePort.getWorldWidth() / 2;
        } else if(gamecam.position.x + gamecam.viewportWidth / 2 >= a.getWidth()){
            gamecam.position.x = a.getWidth() - gamecam.viewportWidth / 2;
        }

        if(gamecam.position.y >= targetY) {

            cameraVelocity.y = (cameraVelocity.y > 0) ? 0 : cameraVelocity.y;

            cameraVelocity.y = (cameraVelocity.y - acceleration <= -cameraMaxVelocity) ?
                    -cameraMaxVelocity : cameraVelocity.y - acceleration;

            boolean onTarget = (gamecam.position.y + cameraVelocity.y * world.delta < targetY);
            gamecam.position.y = onTarget ? targetY : gamecam.position.y + cameraVelocity.y * world.delta;
        } else {

            cameraVelocity.y = (cameraVelocity.y < 0) ? 0 : cameraVelocity.y;

            cameraVelocity.y = (cameraVelocity.y + acceleration >= cameraMaxVelocity) ?
                    cameraMaxVelocity : cameraVelocity.y + acceleration;

            boolean onTarget = (gamecam.position.y + cameraVelocity.y * world.delta > targetY);
            gamecam.position.y = onTarget ? targetY : gamecam.position.y + cameraVelocity.y * world.delta;
        }


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


/*

            gamecam.position.set((int) map.getActiveRoom().getWizard().getCenterX(),(int) map.getActiveRoom().getWizard().getCenterY(), 0);

            if(gamecam.position.x <= gamePort.getWorldWidth() / 2){
                gamecam.position.set(gamePort.getWorldWidth() / 2,gamecam.position.y, 0);
            } else if(gamecam.position.x + gamecam.viewportWidth / 2 >= map.getActiveRoom().WIDTH){
                gamecam.position.set((int) (map.getActiveRoom().WIDTH - gamecam.viewportWidth / 2),(int) gamecam.position.y, 0);
            }

            if(gamecam.position.y <= gamePort.getWorldHeight() / 2){
                gamecam.position.set(gamecam.position.x, gamePort.getWorldHeight() / 2, 0);
            } else if(gamecam.position.y + gamecam.viewportHeight / 2 >= map.getActiveRoom().HEIGHT){
                gamecam.position.set((int) gamecam.position.x,(int) (map.getActiveRoom().HEIGHT - gamecam.viewportHeight / 2), 0);
            }

            gamecam.update();



 */