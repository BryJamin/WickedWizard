package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.arenas.Arena;
import com.byrjamin.wickedwizard.sprites.Wizard;

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

    private boolean isFlashing;

    private float flashTimer;
    public float time;

    private Animation dyingAnimation;


    public Enemy(){
        sprite = new Sprite();
        state = STATE.ALIVE;
        isFlashing = false;
    }

    public void draw(SpriteBatch batch){

        if(isFlashing) {
            Color color = getSprite().getColor();
            this.getSprite().setColor(new Color(0.0f,0.0f,0.0f,0.95f));
            this.getSprite().draw(batch);
            batch.setColor(color);
            getSprite().setColor(color);
        } else {
            this.getSprite().draw(batch);
        }
    }


    /**
     * Reduces the health of the enemy based on the number inserted,
     * but only if the enemy is targetable
     * @param i -
     */
    public void reduceHealth(int i){
        System.out.println("Enemy took: "+i+" damage");
        this.setHealth(this.getHealth() - i);

        if(this.getHealth() <= 0 && getState() != STATE.DYING){
            time = 0;
            this.setState(STATE.DYING);
            //return;
        }
        resetFlashTimer();
    }

    public void flashTimer(float dt){
        if(flashTimer <= 0) {
            flashTimer = 0;
            isFlashing = false;
        } else {
            isFlashing = true;
            flashTimer -= dt;
        }
    }

    public void resetFlashTimer(){
        flashTimer += 0.05f;
    }


    public abstract void update(float dt);

    public abstract void update(float dt, Arena a);

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
