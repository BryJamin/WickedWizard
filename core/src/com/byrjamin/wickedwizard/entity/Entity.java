package com.byrjamin.wickedwizard.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Home on 09/01/2017.
 */
public abstract class Entity  {

    protected int health;
    public abstract void draw(SpriteBatch batch);
    public abstract void reduceHealth(float i);

    public abstract boolean isHit(Rectangle r);
}
