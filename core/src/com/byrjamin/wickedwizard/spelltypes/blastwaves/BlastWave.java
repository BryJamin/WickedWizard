package com.byrjamin.wickedwizard.spelltypes.blastwaves;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
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

    private float SIZE = Measure.units(5);
    private float CIRCLE_SIZE = Measure.units(3);

    private float GROWTH_RATE = Measure.units(8f);
    //private float GROWTH_RATE = Measure.units(0.5f); //Testing Speeds

    private float MOVEMENT = Measure.units(240f);
    //private float MOVEMENT = Measure.units(15f); //Testing Speeds

    private float speed = 1;

    private float centerPointX;
    private float centerPointY;

    private Circle wave;
    private Circle secondWave;

    private Color drawingColor = Color.WHITE;

    private float damage;
    private float time = 0.075f;

    public BlastWave(float centerPointX, float centerPointY){
        this.centerPointX = centerPointX;
        this.centerPointY = centerPointY;
        wave = new Circle(centerPointX,centerPointY, SIZE);
        secondWave = new Circle(centerPointX, centerPointY, SIZE);

    }


    public void update(float dt){
        time -= dt;
        wave.radius += MOVEMENT * dt * speed;
        if(time < 0) {
            secondWave.radius += MOVEMENT * dt * speed;
        }
        CIRCLE_SIZE += GROWTH_RATE * 1.1 * speed;

    }

    public void draw(SpriteBatch batch){
        BoundsDrawer.drawBounds(batch, wave, secondWave);
        batch.setColor(drawingColor);
        //batch.draw(PlayScreen.atlas.findRegion("circle"), centerPointX - CIRCLE_SIZE / 2, centerPointY - CIRCLE_SIZE / 2, CIRCLE_SIZE, CIRCLE_SIZE);
        batch.setColor(Color.WHITE);
    }

    public boolean collides(Rectangle r){
        return Intersector.overlaps(wave, r) && !Intersector.overlaps(secondWave, r);
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
