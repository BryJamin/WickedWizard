package com.byrjamin.wickedwizard.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;

/**
 * Abstract class for enemies within the game
 * Most enemies will draw from this class.
 */
public abstract class Enemy {

    public enum STATE {
        DEAD, DYING, ALIVE
    }

    protected float WIDTH;
    protected float HEIGHT;

    protected Vector2 position;

    public STATE state;

    private float health;

    public boolean isFlashing;

    private float flashTimer;
    protected float time = 0;

    private Animation dyingAnimation;

    protected Array<Rectangle> bounds = new Array<Rectangle>();


    protected Color drawingColor;

    protected TextureRegion currentFrame;


    public Enemy(){
        state = STATE.ALIVE;
        isFlashing = false;
    }



    public void draw(SpriteBatch batch){
        if(isFlashing) {
            Color color = batch.getColor();
            batch.setColor(new Color(0.0f,0.0f,0.0f,0.95f));
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
            batch.setColor(color);
        } else {
            batch.draw(currentFrame, position.x, position.y, WIDTH, HEIGHT);
        }
        BoundsDrawer.drawBounds(batch, bounds);
    }


    /**
     * Reduces the health of the enemy based on the number inserted,
     * but only if the enemy is targetable
     * @param i -
     */
    public void reduceHealth(float i){
        //System.out.println("Enemy took: "+i+" damage");
        this.setHealth(this.getHealth() - i);

        if(this.getHealth() <= 0 && getState() != STATE.DYING){
            time = 0;
            this.setState(STATE.DYING);
            //return;
        }
        resetFlashTimer();
    }

    /**
     * If the enemy has finished flashing set isFlashing to false.
     * @param dt
     */
    public void flashTimer(float dt){
        if(flashTimer <= 0) {
            flashTimer = 0;
            isFlashing = false;
        } else {
            isFlashing = true;
            flashTimer -= dt;
        }
    }

    /**
     * Adds 0.05f to the flashtimer so the enemy can start to flash.
     */
    public void resetFlashTimer(){
        flashTimer += 0.05f;
    }


    public boolean isHit(Rectangle r){
        for(Rectangle bound: bounds){
            if((state == STATE.ALIVE) && bound.overlaps(r)){
                return true;
            }
        }
        return false;
    }



    public void update(float dt, Room r){
        flashTimer(dt);
    };

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    public Animation getDyingAnimation() {
        return dyingAnimation;
    }

    public void setDyingAnimation(Animation dyingAnimation) {
        this.dyingAnimation = dyingAnimation;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public void dyingUpdate(float dt){
        time+=dt;

        currentFrame = this.getDyingAnimation().getKeyFrame(time);

        if(this.getDyingAnimation().isAnimationFinished(time)){
            this.setState(STATE.DEAD);
        }
    }

    public Array<Rectangle> getBounds() {
        return bounds;
    }
}
