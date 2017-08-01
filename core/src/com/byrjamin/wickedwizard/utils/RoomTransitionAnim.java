package com.byrjamin.wickedwizard.utils;

/**
 * Created by Home on 30/04/2017.
 */

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Home on 27/12/2016.
 */
public class RoomTransitionAnim {


    private float WIDTH;
    private float HEIGHT;

    private boolean finished;
    private boolean started;

    private Vector2 position = new Vector2();

    private float endX;
    private float endY;

    private float startX;
    private float startY;

    private float differenceX;
    private float differenceY;

    private float distancePerSecondX;
    private float distancePerSecondY;

    private float durationInSeconds = 0.25f;

    private Vector2 velocity;

    private ShapeRenderer shapeRenderer;

    public RoomTransitionAnim(float startX, float startY, float endX, float endY, float WIDTH, float HEIGHT){
        position.x = startX;
        position.y = startY;

        this.startX = startX;
        this.startY = startY;

        this.endX = endX;
        this.endY = endY;

        this.durationInSeconds = durationInSeconds;

        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;

        differenceX = startX - endX;
        differenceY = startY - endY;

        if(startX < endX && differenceX < 0 || startX > endX && differenceX > 0){
            differenceX *= -1;
        }

        if(startY < endY && differenceY < 0 || startY > endY && differenceY > 0){
            differenceY *= -1;
        }

        distancePerSecondX = differenceX / this.durationInSeconds;
        distancePerSecondY = differenceY / this.durationInSeconds;

        finished = false;
        started = true;

    }


    public void updatePositions(){

    }


    public void update(float dt){


        if(!finished) {

            position.x += distancePerSecondX * dt;
            position.y += distancePerSecondY * dt;

            if (distancePerSecondX > startX) {
                if (position.x > endX) {
                    position.x = endX;
                    finished = true;
                }
            } else {
                if (position.x < endX) {
                    position.x = endX;
                    finished = true;
                }
            }


            if (distancePerSecondY > startY) {
                if (position.y > endY) {
                    position.y = endY;
                    finished = true;
                }
            } else {
                if (position.y < endY) {
                    position.y = endY;
                    finished = true;
                }
            }

        }

    }


    public void draw(SpriteBatch batch){

        if(batch.isDrawing()) {
            batch.end();
        }

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0,0,0,1f);
        shapeRenderer.rect(position.x, position.y, WIDTH, HEIGHT);
        shapeRenderer.end();

        if(!batch.isDrawing()) {
            batch.begin();
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public float getDurationInSeconds() {
        return durationInSeconds;
    }

    public boolean isStarted(){
        return started;
    }

    public void setDurationInSeconds(float durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
        distancePerSecondX = differenceX / this.durationInSeconds;
        distancePerSecondY = differenceY / this.durationInSeconds;
    }
}
