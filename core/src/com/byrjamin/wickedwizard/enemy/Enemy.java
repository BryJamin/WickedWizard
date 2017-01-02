package com.byrjamin.wickedwizard.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public STATE state;

    private Sprite sprite;

    private float health;

    public boolean isFlashing;

    private float flashTimer;
    public float time = 0;

    private Animation dyingAnimation;

    private Array<Rectangle> bounds = new Array<Rectangle>();

    private Vector2 position;


    public Enemy(){
        sprite = new Sprite();
        state = STATE.ALIVE;
        isFlashing = false;
    }

    public void draw(SpriteBatch batch){
        //If the enemy is flashing set the enemy to a darker color.

        if(isFlashing) {
            Color color = getSprite().getColor();
            this.getSprite().setColor(new Color(0.0f,0.0f,0.0f,0.95f));
            this.getSprite().draw(batch);
            batch.setColor(color);
            getSprite().setColor(color);
        } else {
            this.getSprite().draw(batch);
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
        return (state == STATE.ALIVE) && this.getSprite().getBoundingRectangle().overlaps(r);
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

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
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
        this.getSprite().setRegion(this.getDyingAnimation().getKeyFrame(time));
        if(this.getDyingAnimation().isAnimationFinished(time)){
            this.setState(STATE.DEAD);
        }
    }
}
