package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.deck.cards.spellanims.Projectile;
import com.byrjamin.wickedwizard.sprites.Wizard;
import com.sun.org.apache.xpath.internal.SourceTree;

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

    private int Health;

    private Vector3 position;

    private boolean isFlashing;
    private float flashTimer;

    public float time;

    private Animation dyingAnimation;


    public Enemy(float posX, float posY){
        sprite = new Sprite();
        position = new Vector3(posX, posY, 0);
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

    public abstract void update(float dt, Wizard wizard);

    public void setHealth(int health) {
        Health = health;
    }

    public int getHealth() {
        return Health;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getPosition() {
        return position;
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


    /**
     * Checks if the enemies health is less than 0,
     * if so it doesn't draw it.
     * This will most likely be moved into the abstract class.
     * @param batch
     */



}
