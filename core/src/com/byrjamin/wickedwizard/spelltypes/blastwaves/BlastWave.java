package com.byrjamin.wickedwizard.spelltypes.blastwaves;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.spelltypes.Dispellable;

/**
 * Created by Home on 21/12/2016.
 */
public class BlastWave {

    //Required Parameters
    private boolean dispellType;
    //private

    private float SIZE = Measure.units(10);
    private float CIRCLE_SIZE = Measure.units(3);

    private float GROWTH_RATE = Measure.units(8f);
    //private float GROWTH_RATE = Measure.units(0.5f); Testing Speeds

    private float MOVEMENT = Measure.units(240f);
    //private float MOVEMENT = Measure.units(15f); Testing Speeds

    private float speed = 1;

    private float centerPointX;
    private float centerPointY;

    private Rectangle right;
    private Rectangle left;
    private Rectangle top;
    private Rectangle bottom;

    private Vector2 temp;

    private Color drawingColor = Color.WHITE;

    private float damage;

    public BlastWave(float centerPointX, float centerPointY){
        this.centerPointX = centerPointX;
        this.centerPointY = centerPointY;

        right = new Rectangle(0,0, SIZE, SIZE);
        right.setCenter(centerPointX, centerPointY);
        left  = new Rectangle(0,0, SIZE, SIZE);
        left.setCenter(centerPointX, centerPointY);
        top  = new Rectangle(0,0, SIZE, SIZE);
        top.setCenter(centerPointX, centerPointY);
        bottom  = new Rectangle(0,0, SIZE, SIZE);
        bottom.setCenter(centerPointX, centerPointY);

    }


    public void update(float dt){

        right.setHeight(right.getHeight() + GROWTH_RATE * speed);
        right.x += MOVEMENT * dt * -1 * speed;
        right.y = centerPointY - right.getHeight() / 2;

        left.setHeight(left.getHeight() + GROWTH_RATE * speed);
        left.x += MOVEMENT * dt * speed;
        left.y = centerPointY - left.getHeight() / 2;

        top.setWidth(top.getWidth() + GROWTH_RATE * speed);
        top.x = centerPointX - top.getWidth() / 2;
        top.y += MOVEMENT * dt * speed;

        bottom.setWidth(bottom.getWidth() + GROWTH_RATE * speed);
        bottom.x = centerPointX - bottom.getWidth() / 2;
        bottom.y += MOVEMENT * dt * -1 * speed;

        CIRCLE_SIZE += GROWTH_RATE * 1.2 * speed;

    }

    public void draw(SpriteBatch batch){
        BoundsDrawer.drawBounds(batch, right, left, top, bottom);
        batch.setColor(drawingColor);
        batch.draw(PlayScreen.atlas.findRegion("circle"), centerPointX - CIRCLE_SIZE / 2, centerPointY - CIRCLE_SIZE / 2, CIRCLE_SIZE, CIRCLE_SIZE);
        batch.setColor(Color.WHITE);
    }

    public boolean collides(Rectangle r){
        return right.overlaps(r) || left.overlaps(r) || top.overlaps(r) || bottom.overlaps(r);
    }

    /**
     * Checks to see if the wave is outofBounds using the circle's radius
     * @param a - the current Arena
     * @return - true if outofBounds.
     */
    public boolean outOfBounds(Room a){
        return CIRCLE_SIZE / 2 > a.WIDTH && CIRCLE_SIZE / 2 > a.HEIGHT;
    }

    public Color getDrawingColor() {
        return drawingColor;
    }

    public void setDrawingColor(Color drawingColor) {
        this.drawingColor = drawingColor;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
