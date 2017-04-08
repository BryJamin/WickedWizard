package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.ecs.components.ActiveOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.factories.arenas.Arena;

/**
 * Created by Home on 23/03/2017.
 */

public class CameraSystem extends EntitySystem {

    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<ActiveOnTouchComponent> aotm;

    PositionComponent playerPosition;

    private OrthographicCamera gamecam;
    private Viewport gamePort;


    private float max_width;
    private float max_height;

    private float min_width;
    private float min_height;



    @SuppressWarnings("unchecked")
    public CameraSystem(OrthographicCamera gamecam, Viewport gamePort) {
        super(Aspect.all(PlayerComponent.class));
        this.gamecam = gamecam;
        this.gamePort = gamePort;
    }

    @Override
    protected void processSystem() {

        CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);

        gamecam.position.set((int) cbc.getCenterX(),(int) cbc.getCenterY(), 0);

        Arena a = world.getSystem(RoomTransitionSystem.class).getCurrentArena();

        if(gamecam.position.x <= gamePort.getWorldWidth() / 2){
            gamecam.position.set(gamePort.getWorldWidth() / 2,gamecam.position.y, 0);
        } else if(gamecam.position.x + gamecam.viewportWidth / 2 >= a.getWidth()){
            gamecam.position.set((int) (a.getWidth() - gamecam.viewportWidth / 2),(int) gamecam.position.y, 0);
        }

        if(gamecam.position.y <= gamePort.getWorldHeight() / 2){
            gamecam.position.set(gamecam.position.x, gamePort.getWorldHeight() / 2, 0);
        } else if(gamecam.position.y + gamecam.viewportHeight / 2 >= a.getHeight()){
            gamecam.position.set((int) gamecam.position.x,(int) (a.getHeight() - gamecam.viewportHeight / 2), 0);
        }

        gamecam.update();


        world.getSystem(PlayerInputSystem.class).movementArea.setPosition(gamecam.position.x - gamePort.getWorldWidth() / 2,
                gamecam.position.y - gamePort.getWorldHeight() / 2);

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