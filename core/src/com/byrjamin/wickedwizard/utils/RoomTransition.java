package com.byrjamin.wickedwizard.utils;

/**
 * Created by Home on 30/04/2017.
 */

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

    public RoomTransition(float originX, float originY, float WIDTH, float HEIGHT){
        this.originX = originX;
        this.originY = originY;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }


    public void update(float dt){
        transitionAnim.update(dt);
    }

    public void draw(SpriteBatch batch){
       // if(!isFinished()) {
            transitionAnim.draw(batch);
    //    }
    }

    public void fromCenterToLeft(){
        transitionAnim = new RoomTransitionAnim(originX, originY, originX + WIDTH, originY, WIDTH, HEIGHT);
    }


    public void fromCenterToRight(){
        transitionAnim = new RoomTransitionAnim(originX, originY, originX - WIDTH, originY, WIDTH, HEIGHT);
    }

    public void fromCenterToTop(){
        transitionAnim = new RoomTransitionAnim(originX, originY, originX, originY + HEIGHT, WIDTH, HEIGHT);
    }


    public void fromCenterToBottom(){
        transitionAnim = new RoomTransitionAnim(originX, originY, originX, originY - HEIGHT, WIDTH, HEIGHT);
    }

    public void fromRightToCenter(){
        transitionAnim = new RoomTransitionAnim(originX + WIDTH, originY, originX, originY, WIDTH, HEIGHT);
    }

    public void fromLeftToCenter(){
        transitionAnim = new RoomTransitionAnim(originX - WIDTH, originY, originX, originY, WIDTH, HEIGHT);
    }

    public void fromTopToCenter(){
        transitionAnim = new RoomTransitionAnim(originX, originY + HEIGHT, originX, originY, WIDTH, HEIGHT);
    }

    public void fromBottomToCenter(){
        transitionAnim = new RoomTransitionAnim(originX, originY - HEIGHT, originX, originY, WIDTH, HEIGHT);
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
