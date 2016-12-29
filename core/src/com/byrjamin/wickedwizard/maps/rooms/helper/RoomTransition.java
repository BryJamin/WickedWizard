package com.byrjamin.wickedwizard.maps.rooms.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Home on 29/12/2016.
 */
public class RoomTransition {

    private float WIDTH;
    private float HEIGHT;

    private float originX = 0;
    private float originY = 0;

    private RoomTransitionAnim transitionAnim;


    public RoomTransition(float WIDTH, float HEIGHT){
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }


    public void update(float dt){
        transitionAnim.update(dt);
    }

    public void draw(SpriteBatch batch){
        transitionAnim.draw(batch);
    }


    public void enterFromRight(){
        transitionAnim = new RoomTransitionAnim(originX, originY, WIDTH, originY, WIDTH, HEIGHT);
    }

    public void exitToRight(){
        transitionAnim = new RoomTransitionAnim(WIDTH, originY, originX, originY, WIDTH, HEIGHT);
    }

    public void enterFromLeft(){
        transitionAnim = new RoomTransitionAnim(originX, originY, -WIDTH, originY, WIDTH, HEIGHT);
    }

    public void exitToLeft(){
        transitionAnim = new RoomTransitionAnim(-WIDTH, originY, originX, originY, WIDTH, HEIGHT);
    }

    public boolean isFinished(){
        return transitionAnim.isFinished();
    }


    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }
}
