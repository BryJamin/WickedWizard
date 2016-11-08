package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.sprites.Player;

/**
 * Abstract class for enemies within the game
 * Most enemies will draw from this class.
 */
public abstract class Enemy {

    private Sprite sprite;

    private int Health;

    private Vector3 position;

    public Enemy(int posX, int posY){
        position = new Vector3(posX, posY, 0);
    }

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

    /**
     * Checks if the enemies health is less than 0,
     * if so it doesn't draw it.
     * This will most likely be moved into the abstract class.
     * @param batch
     */

    public void draw(SpriteBatch batch){
        if(this.getHealth() <= 0){
            return;
        }
        this.getSprite().draw(batch);
    };

    /**
     * Reduces the health of the enemy based on the number inserted,
     * but only if the enemy is targetable
     * @param i -
     */
    public void reduceHealth(int i){
        this.setHealth(this.getHealth() - i);
    }


    public abstract void update(float dt);

    public abstract void update(float dt, Player player);



}
