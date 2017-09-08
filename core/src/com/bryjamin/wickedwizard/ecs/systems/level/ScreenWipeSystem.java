package com.bryjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.UnpackableComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.graphical.CameraSystem;

/**
 * Created by Home on 30/05/2017.
 */

public class ScreenWipeSystem extends BaseSystem {

    public Action taskBefore;
    public Action taskToPerformInbetweenTransition;
    public Action taskAfter;

    private static final float durationInSeconds = 0.25f;
    private static final float fadeDurationInSeconds = 0.25f;
    private static final float fadeOutDurationInSeconds = 0.50f;

    private float fadeElapsed = 0;

    private float screenWipeSpeedX = com.bryjamin.wickedwizard.MainGame.GAME_WIDTH / durationInSeconds;
    private float screenWipeSpeedY =  com.bryjamin.wickedwizard.MainGame.GAME_HEIGHT / durationInSeconds; //TODO mutiply by somethign to get the correct timings

    private boolean isEntry = false;
    private boolean isExit = false;

    private boolean isFinished = false;
    private boolean processingFlag;

    private Entity transitionEntity;


    public enum Transition {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM, BOTTOM_TO_TOP, FADE, FLASH, NONE
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
            if(performFirstTransition(transitionEntity)){
                taskToPerformInbetweenTransition.performAction(world, null);
                fadeElapsed = fadeOutDurationInSeconds;
                //transitionEntity.edit().add(new Exp)
                if(transition == Transition.FADE){
                    transitionEntity.edit().remove(FadeComponent.class);
                    transitionEntity.edit().add(new FadeComponent(false, fadeOutDurationInSeconds, false));
                }


                isEntry = false;
                isExit = true;
                return;
            }

        }

/*        if(!isEntry && !isExit) {
           // exitAnimation(direction, gamecam);
            //exitAnimation(transitionEntity, transition, gamecam);
            fadeElapsed = fadeOutDurationInSeconds;

            isExit = true; //return;
        }*/

        if(isExit) {

            if(performSecondTransition(transitionEntity)){
                taskAfter.performAction(world, null);
                isFinished = true;
                isExit = false;
                processingFlag = false;

                if(transitionEntity != null) transitionEntity.deleteFromWorld();
            }
        }
    }



    public void instantWipe(Action taskToPerformInbetweenTransition){
        taskBefore().performAction(world, null);
        taskToPerformInbetweenTransition.performAction(world, null);
        taskAfter().performAction(world, null);
    }


    private Action taskBefore(){
        return new Action() {
            @Override
            public void performAction(World world, Entity e) {
                for (BaseSystem s : world.getSystems()) {
                    if (!(s instanceof com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem) &&
                            !(s instanceof ScreenWipeSystem) &&
                            !(s instanceof com.bryjamin.wickedwizard.ecs.systems.ai.FollowPositionSystem) &&
                            !(s instanceof CameraSystem) &&
                            !(s instanceof com.bryjamin.wickedwizard.ecs.systems.graphical.UISystem) &&
                            !(s instanceof com.bryjamin.wickedwizard.ecs.systems.graphical.FadeSystem)) {
                        s.setEnabled(false);
                    }
                }
            }
        };
    }


    private Action taskAfter(){
        return new Action() {
            @Override
            public void performAction(World world, Entity e) {
                for (BaseSystem s : world.getSystems()) {
                    s.setEnabled(true);
                }
            }
        };
    }



    public void startScreenWipe(Transition transition, Action taskToPerformInbetweenTransition){


        processingFlag = true;
        isEntry = true;
        isExit = false;
        this.transition = transition;

       // if(transitionEntity != null) transitionEntity.deleteFromWorld();

        if(transition != Transition.NONE) {

            transitionEntity = world.createEntity();
            transitionEntity.edit().add(new com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent());
            transitionEntity.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.MoveToPositionComponent());
            transitionEntity.edit().add(new com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, -gamecam.viewportHeight / 2));

            transitionEntity.edit().add(new UnpackableComponent());
            transitionEntity.edit().add(new TextureRegionComponent(assetManager.get(com.bryjamin.wickedwizard.assets.FileLocationStrings.spriteAtlas, TextureAtlas.class).findRegion(TextureStrings.BLOCK),
                    gamecam.viewportWidth, gamecam.viewportHeight,
                    TextureRegionComponent.FOREGROUND_LAYER_NEAR, new Color(Color.BLACK)));
            transitionEntity.edit().add(new ExpireComponent(10f));


            startingTransitionSetup(transitionEntity, transition, gamecam);
        }

        this.taskToPerformInbetweenTransition = taskToPerformInbetweenTransition;

        this.taskBefore = taskBefore();

        this.taskAfter = taskAfter();

        this.taskBefore.performAction(world, null);

    }

    public void startingTransitionSetup(Entity e, Transition transition, OrthographicCamera gamecam) {


        switch(transition){
            case LEFT_TO_RIGHT:
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent.class).offsetX = -gamecam.viewportWidth / 2 - gamecam.viewportWidth + com.bryjamin.wickedwizard.MainGame.GAME_BORDER;
                break;
            case RIGHT_TO_LEFT:
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent.class).offsetX = -gamecam.viewportWidth / 2 + gamecam.viewportWidth - com.bryjamin.wickedwizard.MainGame.GAME_BORDER;
                break;
            case TOP_TO_BOTTOM:
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent.class).offsetY = -gamecam.viewportHeight / 2 + gamecam.viewportHeight - com.bryjamin.wickedwizard.MainGame.GAME_BORDER;
                break;
            case BOTTOM_TO_TOP:
                e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent.class).offsetY = -gamecam.viewportHeight / 2 - gamecam.viewportHeight + com.bryjamin.wickedwizard.MainGame.GAME_BORDER;
                break;
            case FADE: e.getComponent(TextureRegionComponent.class).color.a = 0f;
                e.edit().add(new FadeComponent(true, fadeDurationInSeconds, false));
                fadeElapsed = 0;
                break;
            default:
        }

    }


    private boolean performFirstTransition (Entity e){

        if(transition == Transition.NONE) return true;

        com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent followPositionComponent = e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent.class);

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
                if(e.getComponent(TextureRegionComponent.class).color.a >= 1f) return true;
                break;
            default: return true;
        }



        return false;

    }

    private boolean performSecondTransition (Entity e){

        if(transition == Transition.NONE) return true;

        com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent followPositionComponent = e.getComponent(com.bryjamin.wickedwizard.ecs.components.ai.FollowPositionComponent.class);

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
                if(e.getComponent(TextureRegionComponent.class).color.a <= 0f) return true;
                break;
            default: return true;
        }

        return false;

    }

}
