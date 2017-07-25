package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.UnpackableComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.RoomTransition;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 30/05/2017.
 */

public class ScreenWipeSystem extends BaseSystem {

    public Action taskBefore;
    public Action taskToPerformInbetweenTransition;
    public Action taskAfter;


    private float screenWipeSpeedX = Measure.units(200f);
    private float screenWipeSpeedY = Measure.units(200f); //TODO mutiply by somethign to get the correct timings
    private float durationInSeconds = 2f;

    private boolean isEntry = false;
    private boolean isExit = false;

    private boolean isFinished = false;
    private boolean processingFlag;

    private Entity transitionEntity;
    private Entity exitEntity;


    private Direction direction;


    public enum Transition {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP, FADE, FLASH
    }

    public Transition transition;

    private SpriteBatch batch;
    private OrthographicCamera gamecam;
    private AssetManager assetManager;

    public ScreenWipeSystem(SpriteBatch batch, AssetManager assetManager, OrthographicCamera gamecam){
        this.batch = batch;
        this.gamecam = gamecam;
        this.assetManager = assetManager;

    }

    @Override
    protected boolean checkProcessing() {
        if (processingFlag) {
            return true;
        }
        return false;
    }

    @Override
    protected void processSystem() {


        if(isEntry) {

            System.out.println(transition);
            boolean bool = performFirstTransition(transitionEntity);
            System.out.println(bool);
            if(bool){
                taskToPerformInbetweenTransition.performAction(world, null);
                //world.getSystem(CameraSystem.class).updateGamecam();
                isEntry = false;
                return;
            }

        }

        if(!isEntry && !isExit) {
           // exitAnimation(direction, gamecam);
            exitAnimation(transitionEntity, transition, gamecam);
            isExit = true; return;
        }

        if(isExit) {

            if(performSecondTransition(transitionEntity)){
                taskAfter.performAction(world, null);
                isFinished = true;
                isExit = false;
                processingFlag = false;
                transitionEntity.deleteFromWorld();
            }
        }
    }




    public void startScreenWipe(Transition transition, Action taskToPerformInbetweenTransition){


        processingFlag = true;
        isEntry = true;
        isExit = false;
        this.transition = transition;

        transitionEntity = world.createEntity();
        transitionEntity.edit().add(new PositionComponent());
        transitionEntity.edit().add(new MoveToPositionComponent());
        transitionEntity.edit().add(new FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, -gamecam.viewportHeight / 2));

        System.out.println(gamecam.position.x);

        transitionEntity.edit().add(new UnpackableComponent());
        transitionEntity.edit().add(new TextureRegionComponent(assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class).findRegion(TextureStrings.BLOCK),
                gamecam.viewportWidth, gamecam.viewportHeight,
                TextureRegionComponent.FOREGROUND_LAYER_NEAR, Color.BLACK));


        entryAnimation(transitionEntity, transition ,gamecam);

        this.taskToPerformInbetweenTransition = taskToPerformInbetweenTransition;

        this.taskBefore = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                for(BaseSystem s: world.getSystems()){
                    if(!(s instanceof RenderingSystem) && !(s instanceof ScreenWipeSystem) && !(s instanceof FollowPositionSystem) && !(s instanceof CameraSystem)) {
                        s.setEnabled(false);
                    }
                }
            }
        };

        this.taskAfter = new Action() {
            @Override
            public void performAction(World world, Entity e) {
                for(BaseSystem s: world.getSystems()){
                    s.setEnabled(true);
                }
            }
        };

        this.taskBefore.performAction(world, null);

    }


/*
    public Entity createStartingPositionAndTargetOfEntity(Entity e, float startX, float startY, float endX, float endY){
        e.getComponent(PositionComponent.class).position.set(startX, startY, 0);
        e.getComponent(MoveToPositionComponent.class).moveToPosition.set(endX, endY, 0);


        float differenceX = startX - endX;
        float differenceY = startY - endY;

        if(startX < endX && differenceX < 0 || startX > endX && differenceX > 0){
            differenceX *= -1;
        }

        if(startY < endY && differenceY < 0 || startY > endY && differenceY > 0){
            differenceY *= -1;
        }

        screenWipeSpeedX = differenceX / durationInSeconds;
        screenWipeSpeedY = differenceY / durationInSeconds;

        return e;
    }
*/


    public void entryAnimation(Entity e, Transition transition, OrthographicCamera gamecam) {


        switch(transition){
            case LEFT_TO_RIGHT:
            default:
                e.getComponent(FollowPositionComponent.class).offsetX = -gamecam.viewportWidth;
                break;
            case RIGHT_TO_LEFT:
                e.getComponent(FollowPositionComponent.class).offsetX = gamecam.viewportWidth;
                break;
            case TOP_TO_BOTTOM:
                e.getComponent(FollowPositionComponent.class).offsetY = gamecam.viewportHeight;
                break;
            case BOTTOM_TO_TOP:
                e.getComponent(FollowPositionComponent.class).offsetY = -gamecam.viewportHeight;
                break;
        }


        System.out.println(transition);
        System.out.println("Offset X: " + e.getComponent(FollowPositionComponent.class).offsetX);
        System.out.println("Offset Y: " + e.getComponent(FollowPositionComponent.class).offsetY);

    }


    public void exitAnimation(Entity e, Transition transition, OrthographicCamera gamecam) {


        float originX = gamecam.position.x - gamecam.viewportWidth / 2;
        float originY = gamecam.position.y - gamecam.viewportHeight / 2;

        float WIDTH = gamecam.viewportWidth;
        float HEIGHT = gamecam.viewportHeight;

/*        switch(transition){
            case LEFT_TO_RIGHT:
            default:
                createStartingPositionAndTargetOfEntity(e, originX, originY, originX + WIDTH, originY);
                break;
            case RIGHT_TO_LEFT:
                createStartingPositionAndTargetOfEntity(e, originX, originY, originX - WIDTH, originY);
                break;
            case TOP_TO_BOTTOM:
                createStartingPositionAndTargetOfEntity(e,originX, originY, originX, originY - HEIGHT);
                break;
            case BOTTOM_TO_TOP:
                createStartingPositionAndTargetOfEntity(e, originX, originY, originX, originY + HEIGHT);
                break;
        }*/

    }



    public boolean performFirstTransition (Entity e){

        FollowPositionComponent followPositionComponent = e.getComponent(FollowPositionComponent.class);

        float targetOffsetX = -gamecam.viewportWidth / 2;
        float targetOffsetY = -gamecam.viewportHeight / 2;

        System.out.println("Offset Y: " + e.getComponent(FollowPositionComponent.class).offsetY);

        System.out.println(screenWipeSpeedX);

        switch (transition){
            case LEFT_TO_RIGHT:
                followPositionComponent.offsetX = (followPositionComponent.offsetX + screenWipeSpeedX * world.getDelta() >= targetOffsetX) ? targetOffsetX : followPositionComponent.offsetX + screenWipeSpeedX * world.getDelta();
                if(followPositionComponent.offsetX >= targetOffsetX) return true;
                break;
            case RIGHT_TO_LEFT:
                followPositionComponent.offsetX = (followPositionComponent.offsetX - screenWipeSpeedX * world.getDelta() <= targetOffsetX) ? targetOffsetX : followPositionComponent.offsetX - screenWipeSpeedX * world.getDelta();
                if(followPositionComponent.offsetX <= targetOffsetX) return true;
                break;
            case BOTTOM_TO_TOP:
                followPositionComponent.offsetY = (followPositionComponent.offsetY + screenWipeSpeedY * world.getDelta() >= targetOffsetY) ? targetOffsetY : followPositionComponent.offsetY + screenWipeSpeedY * world.getDelta();
                if(followPositionComponent.offsetY >= targetOffsetY) return true;
                break;
            case TOP_TO_BOTTOM:
                followPositionComponent.offsetY = (followPositionComponent.offsetY - screenWipeSpeedY * world.getDelta() <= targetOffsetY) ? targetOffsetY : followPositionComponent.offsetY - screenWipeSpeedY * world.getDelta();
                if(followPositionComponent.offsetY <= targetOffsetY) return true;
                break;

            default: return true;
        }

        System.out.println("TRACKED POSITIONS X" + followPositionComponent.trackedPosition.x);
        System.out.println("TRACKED POSITIONS Y" + followPositionComponent.trackedPosition.y);

        return false;

    }

    public boolean performSecondTransition (Entity e){

        FollowPositionComponent followPositionComponent = e.getComponent(FollowPositionComponent.class);

        float targetOffsetX = -gamecam.viewportWidth / 2;
        float targetOffsetY = -gamecam.viewportHeight / 2;

        switch (transition){
            case LEFT_TO_RIGHT:
                followPositionComponent.offsetX =
                        (followPositionComponent.offsetX + screenWipeSpeedX * world.getDelta() >= targetOffsetX + gamecam.viewportWidth)
                                ? targetOffsetX + gamecam.viewportWidth : followPositionComponent.offsetX + screenWipeSpeedX * world.getDelta();
                if(followPositionComponent.offsetX >= targetOffsetX + gamecam.viewportWidth) return true;
                break;
            case RIGHT_TO_LEFT:
                followPositionComponent.offsetX =
                        (followPositionComponent.offsetX - screenWipeSpeedX * world.getDelta() <= targetOffsetX - gamecam.viewportWidth)
                                ? targetOffsetX - gamecam.viewportWidth : followPositionComponent.offsetX - screenWipeSpeedX * world.getDelta();
                if(followPositionComponent.offsetX <= targetOffsetX - gamecam.viewportWidth) return true;
                break;
            case BOTTOM_TO_TOP:

                System.out.println("END OFFSET Y " + followPositionComponent.offsetY);
                System.out.println(followPositionComponent.offsetY >= targetOffsetY + gamecam.viewportHeight);

                followPositionComponent.offsetY =
                        (followPositionComponent.offsetY + screenWipeSpeedY * world.getDelta() >= targetOffsetY + gamecam.viewportHeight)
                                ? targetOffsetY + gamecam.viewportHeight : followPositionComponent.offsetY + screenWipeSpeedY * world.getDelta();

                System.out.println("END OFFSET Y " + followPositionComponent.offsetY);
               // System.out.println("END OFFSET Y " + followPositionComponent.offsetY);

                System.out.println(followPositionComponent.offsetY >= targetOffsetY + gamecam.viewportHeight);

                if(followPositionComponent.offsetY >= targetOffsetY + gamecam.viewportHeight) return true;
                break;
            case TOP_TO_BOTTOM:
                followPositionComponent.offsetY =
                        (followPositionComponent.offsetY - screenWipeSpeedY * world.getDelta() <= targetOffsetY - gamecam.viewportHeight)
                                ? targetOffsetY - gamecam.viewportHeight : followPositionComponent.offsetY - screenWipeSpeedY * world.getDelta();
                if(followPositionComponent.offsetY <= targetOffsetY - gamecam.viewportHeight) return true;
                break;

            default: return true;
        }

        System.out.println(gamecam.position.x);

        System.out.println("TRACKED POSITIONS X" + followPositionComponent.trackedPosition.x);
        System.out.println("TRACKED POSITIONS Y" + followPositionComponent.trackedPosition.y);

        return false;

    }



/*
    public void exitAnimation(Direction start, OrthographicCamera gamecam) {

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        float width = gamecam.viewportWidth;
        float height = gamecam.viewportHeight;

        switch (start) {
            case LEFT:
            default:
                exitTransition = new RoomTransition(camX, camY, width, height);
                exitTransition.fromCenterToLeft();
                break;
            case RIGHT:
                exitTransition = new RoomTransition(camX, camY, width, height);
                exitTransition.fromCenterToRight();
                break;
            case UP:
                exitTransition = new RoomTransition(camX, camY, width, height);
                exitTransition.fromCenterToBottom();
                break;
            case DOWN:
                exitTransition = new RoomTransition(camX, camY, width, height);
                exitTransition.fromCenterToTop();
                break;
        }

    }*/


}
