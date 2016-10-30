package com.byrjamin.wickedwizard.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Home on 23/10/2016.
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

    public abstract void draw(SpriteBatch batch);

    public void reduceHealth(int i){
     if(EnemyStateManager.isTargetable()){
         this.setHealth(this.getHealth() - i);
         EnemyStateManager.setTargetable(false);
     }
    }


}
