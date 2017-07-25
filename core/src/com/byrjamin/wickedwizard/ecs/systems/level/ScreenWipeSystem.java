package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.UnpackableComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
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

    private static final float durationInSeconds = 0.25f;
    private static final float fadeDurationInSeconds = 0.25f;

    private float fadeElapsed = 0;

    private float screenWipeSpeedX = MainGame.GAME_WIDTH / durationInSeconds;
    private float screenWipeSpeedY =  MainGame.GAME_HEIGHT / durationInSeconds; //TODO mutiply by somethign to get the correct timings

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


        System.out.println("POSITION X" + transitionEntity.getComponent(PositionComponent.class).position.x);

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
            //exitAnimation(transitionEntity, transition, gamecam);
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
                TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(Color.BLACK)));

        System.out.println("WIDTH " + gamecam.viewportWidth);


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

    public void entryAnimation(Entity e, Transition transition, OrthographicCamera gamecam) {


        switch(transition){
            case LEFT_TO_RIGHT:
            default:
                e.getComponent(FollowPositionComponent.class).offsetX = -gamecam.viewportWidth / 2 - gamecam.viewportWidth + MainGame.GAME_BORDER;
                break;
            case RIGHT_TO_LEFT:
                e.getComponent(FollowPositionComponent.class).offsetX = -gamecam.viewportWidth / 2 + gamecam.viewportWidth - MainGame.GAME_BORDER;
                break;
            case TOP_TO_BOTTOM:
                e.getComponent(FollowPositionComponent.class).offsetY = -gamecam.viewportHeight / 2 + gamecam.viewportHeight - MainGame.GAME_BORDER;
                break;
            case BOTTOM_TO_TOP:
                e.getComponent(FollowPositionComponent.class).offsetY = -gamecam.viewportHeight / 2 - gamecam.viewportHeight + MainGame.GAME_BORDER;
                break;
            case FADE: e.getComponent(TextureRegionComponent.class).color.a = 0f;
                fadeElapsed = 0;
                break;
        }

    }


    public boolean performFirstTransition (Entity e){

        FollowPositionComponent followPositionComponent = e.getComponent(FollowPositionComponent.class);

        float targetOffsetX = -gamecam.viewportWidth / 2;
        float targetOffsetY = -gamecam.viewportHeight / 2;

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
            case FADE:
                fadeElapsed += world.getDelta();
                e.getComponent(TextureRegionComponent.class).color.a = Interpolation.fade.apply(fadeElapsed / fadeDurationInSeconds);
                if(fadeElapsed >= fadeDurationInSeconds) return true;
                break;
            default: return true;
        }

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

                followPositionComponent.offsetY =
                        (followPositionComponent.offsetY + screenWipeSpeedY * world.getDelta() >= targetOffsetY + gamecam.viewportHeight)
                                ? targetOffsetY + gamecam.viewportHeight : followPositionComponent.offsetY + screenWipeSpeedY * world.getDelta();

                if(followPositionComponent.offsetY >= targetOffsetY + gamecam.viewportHeight) return true;
                break;
            case TOP_TO_BOTTOM:
                followPositionComponent.offsetY =
                        (followPositionComponent.offsetY - screenWipeSpeedY * world.getDelta() <= targetOffsetY - gamecam.viewportHeight)
                                ? targetOffsetY - gamecam.viewportHeight : followPositionComponent.offsetY - screenWipeSpeedY * world.getDelta();
                if(followPositionComponent.offsetY <= targetOffsetY - gamecam.viewportHeight) return true;
                break;

            case FADE:
                fadeElapsed -= world.getDelta();
                e.getComponent(TextureRegionComponent.class).color.a = Interpolation.fade.apply(fadeElapsed / fadeDurationInSeconds);
                if(fadeElapsed <= 0) return true;
            default: return true;
        }

        return false;

    }

}
