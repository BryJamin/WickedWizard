package com.byrjamin.wickedwizard.ecs.systems.level;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.utils.RoomTransition;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 30/05/2017.
 */

public class ScreenWipeSystem extends BaseSystem {

    public RoomTransition entryTransition;
    public RoomTransition exitTransition;

    public Task taskBefore;
    public Task taskToPerformInbetweenTransition;
    public Task taskAfter;

    private boolean isEntry = false;
    private boolean isExit = false;

    private boolean isFinished = false;
    private boolean processingFlag;


    private Direction direction;


    private SpriteBatch batch;
    private OrthographicCamera gamecam;

    public ScreenWipeSystem(SpriteBatch batch, OrthographicCamera gamecam){
        this.batch = batch;
        this.gamecam = gamecam;

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
            entryTransition.update(world.delta);
            entryTransition.draw(batch);

            if(entryTransition.isFinished()){
                taskToPerformInbetweenTransition.performAction(world, null);
                entryTransition.draw(batch);
                isEntry = false;
                return;
            }

        }

        if(!isEntry && !isExit) {
            entryTransition.draw(batch);
            exitAnimation(direction, gamecam);
            isExit = true;
        }

        if(isExit) {

            //entryTransition.draw(batch);
            exitTransition.update(world.delta);
            exitTransition.draw(batch);

            if(exitTransition.isFinished()){
                taskAfter.performAction(world, null);
                isFinished = true;
                isExit = false;
                processingFlag = false;
            }
        }
    }




    public void startScreenWipe(Direction start, Task taskToPerformInbetweenTransition){

        processingFlag = true;
        isEntry = true;
        isExit = false;
        this.direction = start;
        entryAnimation(start, gamecam);

        this.taskToPerformInbetweenTransition = taskToPerformInbetweenTransition;

        this.taskBefore = new Task() {
            @Override
            public void performAction(World world, Entity e) {
                for(BaseSystem s: world.getSystems()){
                    if(!(s instanceof RenderingSystem) && !(s instanceof ScreenWipeSystem)) {
                        s.setEnabled(false);
                    }
                }
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        this.taskAfter = new Task() {
            @Override
            public void performAction(World world, Entity e) {
                for(BaseSystem s: world.getSystems()){
                    s.setEnabled(true);
                }
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        };

        this.taskBefore.performAction(world, null);

    }




    /**
     * The direction the transition starts from
     * @param start
     * @param gamecam
     */
    public void entryAnimation(Direction start, OrthographicCamera gamecam) {

        float camX = gamecam.position.x - gamecam.viewportWidth / 2;
        float camY = gamecam.position.y - gamecam.viewportHeight / 2;

        float width = gamecam.viewportWidth;
        float height = gamecam.viewportHeight;

        switch(start){
            case LEFT:
            default:
                entryTransition = new RoomTransition(camX, camY, width, height);
                entryTransition.fromLeftToCenter();
                break;
            case RIGHT:
                entryTransition = new RoomTransition(camX, camY, width, height);
                entryTransition.fromRightToCenter();
                break;
            case UP:
                entryTransition = new RoomTransition(camX, camY, width, height);
                entryTransition.fromTopToCenter();
                break;
            case DOWN:
                entryTransition = new RoomTransition(camX, camY, width, height);
                entryTransition.fromBottomToCenter();
                break;
        }

    }


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

    }


}
