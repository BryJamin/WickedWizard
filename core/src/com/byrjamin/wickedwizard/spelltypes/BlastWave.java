package com.byrjamin.wickedwizard.spelltypes;

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
    //private float GROWTH_RATE = Measure.units(0.5f);

    private float centerPointX;
    private float centerPointY;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Dispellable.DISPELL dispelDirection;

    //
    private Rectangle right;
    private Rectangle left;
    private Rectangle top;
    private Rectangle bottom;

    private Color drawingColor;

    private float MOVEMENT = Measure.units(240f);
    //private float MOVEMENT = Measure.units(15f);

    public BlastWave(float centerPointX, float centerPointY, Dispellable.DISPELL dispelDirection){
        this.centerPointX = centerPointX;
        this.centerPointY = centerPointY;

        this.dispelDirection = dispelDirection;

        right = new Rectangle(0,0, SIZE, SIZE);
        right.setCenter(centerPointX, centerPointY);
        left  = new Rectangle(0,0, SIZE, SIZE);
        left.setCenter(centerPointX, centerPointY);
        top  = new Rectangle(0,0, SIZE, SIZE);
        top.setCenter(centerPointX, centerPointY);
        bottom  = new Rectangle(0,0, SIZE, SIZE);
        bottom.setCenter(centerPointX, centerPointY);

        if(dispelDirection == Dispellable.DISPELL.HORIZONTAL){
            drawingColor = Color.BLUE;
        } else if(dispelDirection == Dispellable.DISPELL.VERTICAL){
            drawingColor = Color.RED;
        }


    }


    public void update(float dt){

        Vector2 temp = new Vector2();


        right.setHeight(right.getHeight() + GROWTH_RATE);
        right.getPosition(temp);
        temp.add(MOVEMENT * dt * -1, 0);
        right.setPosition(temp.x, centerPointY - right.getHeight() / 2);

        left.setHeight(left.getHeight() + GROWTH_RATE);
        left.getPosition(temp);
        temp.add(MOVEMENT * dt, 0);
        left.setPosition(temp.x, centerPointY - left.getHeight() / 2);

        top.setWidth(top.getWidth() + GROWTH_RATE);
        top.getPosition(temp);
        temp.add(0, MOVEMENT * dt);
        top.setPosition(centerPointX - top.getWidth() / 2, temp.y);

        bottom.setWidth(bottom.getWidth() + GROWTH_RATE);
        bottom.getPosition(temp);
        temp.add(0, MOVEMENT * dt * -1);
        bottom.setPosition(centerPointX - bottom.getWidth() / 2, temp.y);


        CIRCLE_SIZE += GROWTH_RATE * 1.2;
/*
        System.out.println(left.x);*/

    }

    public void draw(SpriteBatch batch){
        Array<Rectangle> bounds = new Array<Rectangle>();
        bounds.add(right);
        bounds.add(left);
        bounds.add(top);
        bounds.add(bottom);
        BoundsDrawer.drawBounds(batch, bounds);

        batch.setColor(drawingColor);
        batch.draw(PlayScreen.atlas.findRegion("circle"), centerPointX - CIRCLE_SIZE / 2, centerPointY - CIRCLE_SIZE / 2, CIRCLE_SIZE, CIRCLE_SIZE);
        batch.setColor(Color.WHITE);
        /*
        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.circle(centerPointX, centerPointY, CIRCLE_SIZE / 2);
        shapeRenderer.end();

        batch.begin();
*/

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

    public Dispellable.DISPELL getDispelDirection() {
        return dispelDirection;
    }

    public void setDispelDirection(Dispellable.DISPELL dispelDirection) {
        this.dispelDirection = dispelDirection;
    }
}
